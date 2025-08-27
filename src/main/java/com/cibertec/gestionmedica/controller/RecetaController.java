package com.cibertec.gestionmedica.controller;

import com.cibertec.gestionmedica.dto.RecetaDTO;
import com.cibertec.gestionmedica.dto.RecetaItemDTO;
import com.cibertec.gestionmedica.model.*;
import com.cibertec.gestionmedica.repository.*;
import com.cibertec.gestionmedica.security.UserDetailsImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recetas")
public class RecetaController {

    private final RecetaRepository recetaRepository;
    private final PacienteRepository pacienteRepository;
    private final AlergiaRepository alergiaRepository;
    private final MedicamentoRepository medicamentoRepository;

    public RecetaController(RecetaRepository recetaRepository,
                            PacienteRepository pacienteRepository,
                            AlergiaRepository alergiaRepository,
                            MedicamentoRepository medicamentoRepository) {
        this.recetaRepository = recetaRepository;
        this.pacienteRepository = pacienteRepository;
        this.alergiaRepository = alergiaRepository;
        this.medicamentoRepository = medicamentoRepository;
    }

  
@GetMapping("/mis-recetas")
public List<RecetaDTO> misRecetas(@AuthenticationPrincipal UserDetailsImpl userDetails) {
    Paciente paciente = pacienteRepository.findByUsuarioId(userDetails.getId());

    return recetaRepository.findByPacienteId(paciente.getId())
            .stream()
            .map(r -> {
                RecetaDTO dto = new RecetaDTO();
                dto.setId(r.getId());
                dto.setFechaEmision(r.getFechaEmision().toString());
                dto.setFechaCaducidad(r.getFechaCaducidad().toString());
                dto.setMedicoNombre(r.getMedico().getNombre());

                // 🔹 Convertir los items
                List<RecetaItemDTO> items = r.getItems().stream().map(it -> {
                    RecetaItemDTO idto = new RecetaItemDTO();
                    idto.setId(it.getId());
                    idto.setMedicamento(it.getMedicamento().getNombre());
                    idto.setDosis(it.getDosis());
                    idto.setFrecuencia(it.getFrecuencia());
                    return idto;
                }).toList();

                dto.setItems(items);

                return dto;
            })
            .toList();
}

 
    @GetMapping
    public List<Receta> listar() {
        return recetaRepository.findAll();
    }

    @GetMapping("/{id}")
    public Receta obtener(@PathVariable Long id) {
        return recetaRepository.findById(id).orElseThrow();
    }

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody Receta receta) {
        Paciente paciente = pacienteRepository.findById(receta.getPaciente().getId())
                .orElseThrow();

        List<Alergia> alergias = alergiaRepository.findAll()
                .stream()
                .filter(a -> a.getPaciente() != null && a.getPaciente().getId().equals(paciente.getId()))
                .toList();

        for (RecetaItem item : receta.getItems()) {
            Medicamento med = medicamentoRepository.findById(item.getMedicamento().getId())
                    .orElseThrow();

            boolean esAlergico = alergias.stream()
                    .anyMatch(a -> a.getNombre().equalsIgnoreCase(med.getNombre()));

            if (esAlergico) {
                return ResponseEntity.badRequest().body(
                        "⚠️ El paciente es alérgico a " + med.getNombre() + ", receta no permitida"
                );
            }

            item.setReceta(receta);
        }

        Receta nueva = recetaRepository.save(receta);
        return ResponseEntity.ok(nueva);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        recetaRepository.deleteById(id);
    }
}
