package com.cibertec.gestionmedica.controller;

import com.cibertec.gestionmedica.model.Receta;
import com.cibertec.gestionmedica.repository.RecetaRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recetas")
public class RecetaController {

    private final RecetaRepository recetaRepository;

    public RecetaController(RecetaRepository recetaRepository) {
        this.recetaRepository = recetaRepository;
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
    public Receta crear(@RequestBody Receta receta) {
        return recetaRepository.save(receta);
    }

    @PutMapping("/{id}")
    public Receta actualizar(@PathVariable Long id, @RequestBody Receta receta) {
        receta.setId(id);
        return recetaRepository.save(receta);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        recetaRepository.deleteById(id);
    }
}
