package com.cibertec.gestionmedica.controller;

import com.cibertec.gestionmedica.model.Enfermedad;
import com.cibertec.gestionmedica.model.Paciente;
import com.cibertec.gestionmedica.repository.EnfermedadRepository;
import com.cibertec.gestionmedica.repository.PacienteRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/enfermedades")
public class EnfermedadController {

    private final EnfermedadRepository enfermedadRepository;
    private final PacienteRepository pacienteRepository;

    public EnfermedadController(EnfermedadRepository enfermedadRepository, PacienteRepository pacienteRepository) {
        this.enfermedadRepository = enfermedadRepository;
        this.pacienteRepository = pacienteRepository;
    }

    @GetMapping
    public List<Enfermedad> listar() {
        return enfermedadRepository.findAll();
    }

    @GetMapping("/{id}")
    public Enfermedad obtener(@PathVariable Long id) {
        return enfermedadRepository.findById(id).orElseThrow();
    }

    @PostMapping
    public Enfermedad crear(@RequestBody Enfermedad enfermedad) {
        return enfermedadRepository.save(enfermedad);
    }

    @PutMapping("/{id}")
    public Enfermedad actualizar(@PathVariable Long id, @RequestBody Enfermedad enfermedad) {
        enfermedad.setId(id);
        return enfermedadRepository.save(enfermedad);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        enfermedadRepository.deleteById(id);
    }

    @GetMapping("/paciente/{pacienteId}")
    public List<Enfermedad> listarPorPaciente(@PathVariable Long pacienteId) {
        return enfermedadRepository.findAll()
                .stream()
                .filter(e -> e.getPaciente() != null && e.getPaciente().getId().equals(pacienteId))
                .toList();
    }

    @PostMapping("/paciente/{pacienteId}")
    public Enfermedad asignarEnfermedad(@PathVariable Long pacienteId, @RequestBody Enfermedad enfermedad) {
        Paciente paciente = pacienteRepository.findById(pacienteId).orElseThrow();
        enfermedad.setPaciente(paciente);
        return enfermedadRepository.save(enfermedad);
    }

}
