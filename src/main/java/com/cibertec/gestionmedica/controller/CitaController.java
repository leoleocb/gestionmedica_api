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

    @GetMapping
    public List<Cita> listar() {
        return citaRepository.findAll();
    }

    @GetMapping("/{id}")
    public Cita obtener(@PathVariable Long id) {
        return citaRepository.findById(id).orElseThrow();
    }

    @PostMapping
    public Cita crear(@RequestBody Cita cita) {
        return citaRepository.save(cita);
    }

    @PutMapping("/{id}")
    public Cita actualizar(@PathVariable Long id, @RequestBody Cita cita) {
        cita.setId(id);
        return citaRepository.save(cita);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        citaRepository.deleteById(id);
    }
}
