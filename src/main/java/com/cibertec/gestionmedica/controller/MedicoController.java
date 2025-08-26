package com.cibertec.gestionmedica.controller;

import com.cibertec.gestionmedica.model.Medico;
import com.cibertec.gestionmedica.repository.MedicoRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medicos")
public class MedicoController {

    private final MedicoRepository medicoRepository;

    public MedicoController(MedicoRepository medicoRepository) {
        this.medicoRepository = medicoRepository;
    }

    @GetMapping
    public List<Medico> listar() {
        return medicoRepository.findAll();
    }

    @GetMapping("/{id}")
    public Medico obtener(@PathVariable Long id) {
        return medicoRepository.findById(id).orElseThrow();
    }

    @PostMapping
    public Medico crear(@RequestBody Medico medico) {
        return medicoRepository.save(medico);
    }

    @PutMapping("/{id}")
    public Medico actualizar(@PathVariable Long id, @RequestBody Medico medico) {
        medico.setId(id);
        return medicoRepository.save(medico);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        medicoRepository.deleteById(id);
    }
}
