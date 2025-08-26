package com.cibertec.gestionmedica.controller;

import com.cibertec.gestionmedica.model.Cita;
import com.cibertec.gestionmedica.repository.CitaRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/citas")
public class CitaController {

    private final CitaRepository citaRepository;

    public CitaController(CitaRepository citaRepository) {
        this.citaRepository = citaRepository;
    }

    // listar
    @GetMapping
    public List<Cita> listar() {
        return citaRepository.findAll();
    }

    // obtener por id
    @GetMapping("/{id}")
    public Cita obtener(@PathVariable Long id) {
        return citaRepository.findById(id).orElseThrow();
    }

    // crear cita
    @PostMapping
    public Cita crear(@RequestBody Cita cita) {
        return citaRepository.save(cita);
    }

    // actualizar
    @PutMapping("/{id}")
    public Cita actualizar(@PathVariable Long id, @RequestBody Cita cita) {
        cita.setId(id);
        return citaRepository.save(cita);
    }

    // eliminar
    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        citaRepository.deleteById(id);
    }

    // actaulizar estado de la cita
    @PutMapping("/{id}/estado")
    public Cita actualizarEstado(@PathVariable Long id, @RequestParam String estado) {
        Cita cita = citaRepository.findById(id).orElseThrow();
        cita.setEstado(estado);
        return citaRepository.save(cita);
    }
}
