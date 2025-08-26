package com.cibertec.gestionmedica.controller;

import com.cibertec.gestionmedica.model.Alergia;
import com.cibertec.gestionmedica.repository.AlergiaRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/alergias")
public class AlergiaController {

    private final AlergiaRepository alergiaRepository;

    public AlergiaController(AlergiaRepository alergiaRepository) {
        this.alergiaRepository = alergiaRepository;
    }

    // 📌 Listar todas las alergias
    @GetMapping
    public List<Alergia> listar() {
        return alergiaRepository.findAll();
    }

    // 📌 Obtener alergia por ID
    @GetMapping("/{id}")
    public Alergia obtener(@PathVariable Long id) {
        return alergiaRepository.findById(id).orElseThrow();
    }

    // 📌 Crear nueva alergia
    @PostMapping
    public Alergia crear(@RequestBody Alergia alergia) {
        return alergiaRepository.save(alergia);
    }

    // 📌 Actualizar alergia
    @PutMapping("/{id}")
    public Alergia actualizar(@PathVariable Long id, @RequestBody Alergia alergia) {
        alergia.setId(id);
        return alergiaRepository.save(alergia);
    }

    // 📌 Eliminar alergia
    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        alergiaRepository.deleteById(id);
    }

    // 📌 Listar alergias por paciente
    @GetMapping("/paciente/{pacienteId}")
    public List<Alergia> listarPorPaciente(@PathVariable Long pacienteId) {
        return alergiaRepository.findAll()
                .stream()
                .filter(a -> a.getPaciente() != null && a.getPaciente().getId().equals(pacienteId))
                .toList();
    }
}
