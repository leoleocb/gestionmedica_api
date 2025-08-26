package com.cibertec.gestionmedica.controller;

import com.cibertec.gestionmedica.model.Enfermedad;
import com.cibertec.gestionmedica.repository.EnfermedadRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/enfermedades")
public class EnfermedadController {

    private final EnfermedadRepository enfermedadRepository;

    public EnfermedadController(EnfermedadRepository enfermedadRepository) {
        this.enfermedadRepository = enfermedadRepository;
    }

    // 📌 Listar todas las enfermedades
    @GetMapping
    public List<Enfermedad> listar() {
        return enfermedadRepository.findAll();
    }

    // 📌 Obtener enfermedad por ID
    @GetMapping("/{id}")
    public Enfermedad obtener(@PathVariable Long id) {
        return enfermedadRepository.findById(id).orElseThrow();
    }

    // 📌 Crear nueva enfermedad
    @PostMapping
    public Enfermedad crear(@RequestBody Enfermedad enfermedad) {
        return enfermedadRepository.save(enfermedad);
    }

    // 📌 Actualizar enfermedad
    @PutMapping("/{id}")
    public Enfermedad actualizar(@PathVariable Long id, @RequestBody Enfermedad enfermedad) {
        enfermedad.setId(id);
        return enfermedadRepository.save(enfermedad);
    }

    // 📌 Eliminar enfermedad
    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        enfermedadRepository.deleteById(id);
    }

    // 📌 Listar enfermedades por paciente
    @GetMapping("/paciente/{pacienteId}")
    public List<Enfermedad> listarPorPaciente(@PathVariable Long pacienteId) {
        return enfermedadRepository.findAll()
                .stream()
                .filter(e -> e.getPaciente() != null && e.getPaciente().getId().equals(pacienteId))
                .toList();
    }
}
