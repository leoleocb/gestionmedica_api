package com.cibertec.gestionmedica.controller;

import com.cibertec.gestionmedica.model.Alergia;
import com.cibertec.gestionmedica.model.Paciente;
import com.cibertec.gestionmedica.repository.AlergiaRepository;
import com.cibertec.gestionmedica.repository.PacienteRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/alergias")
public class AlergiaController {

    private final AlergiaRepository alergiaRepository;
    private final PacienteRepository pacienteRepository;

    public AlergiaController(AlergiaRepository alergiaRepository, PacienteRepository pacienteRepository) {
        this.alergiaRepository = alergiaRepository;
        this.pacienteRepository = pacienteRepository;
    }

    @GetMapping
    public List<Alergia> listar() {
        return alergiaRepository.findAll();
    }

    @GetMapping("/{id}")
    public Alergia obtener(@PathVariable Long id) {
        return alergiaRepository.findById(id).orElseThrow();
    }

    @PostMapping
    public Alergia crear(@RequestBody Alergia alergia) {
        return alergiaRepository.save(alergia);
    }

    @PutMapping("/{id}")
    public Alergia actualizar(@PathVariable Long id, @RequestBody Alergia alergia) {
        alergia.setId(id);
        return alergiaRepository.save(alergia);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        alergiaRepository.deleteById(id);
    }

    @GetMapping("/paciente/{pacienteId}")
    public List<Alergia> listarPorPaciente(@PathVariable Long pacienteId) {
        return alergiaRepository.findAll()
                .stream()
                .filter(a -> a.getPaciente() != null && a.getPaciente().getId().equals(pacienteId))
                .toList();
    }

    @PostMapping("/paciente/{pacienteId}")
    public Alergia asignarAlergia(@PathVariable Long pacienteId, @RequestBody Alergia alergia) {
        Paciente paciente = pacienteRepository.findById(pacienteId).orElseThrow();
        alergia.setPaciente(paciente);
        return alergiaRepository.save(alergia);
    }
}
