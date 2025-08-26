package com.cibertec.gestionmedica.controller;

import com.cibertec.gestionmedica.model.Paciente;
import com.cibertec.gestionmedica.repository.PacienteRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pacientes")
public class PacienteController {

    private final PacienteRepository pacienteRepository;

    public PacienteController(PacienteRepository pacienteRepository) {
        this.pacienteRepository = pacienteRepository;
    }

    @GetMapping
    public List<Paciente> listar() {
        return pacienteRepository.findAll();
    }

    @GetMapping("/{id}")
    public Paciente obtener(@PathVariable Long id) {
        return pacienteRepository.findById(id).orElseThrow();
    }

    @PostMapping
    public Paciente crear(@RequestBody Paciente paciente) {
        return pacienteRepository.save(paciente);
    }

    @PutMapping("/{id}")
    public Paciente actualizar(@PathVariable Long id, @RequestBody Paciente paciente) {
        paciente.setId(id);
        return pacienteRepository.save(paciente);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        pacienteRepository.deleteById(id);
    }
}
