package com.cibertec.gestionmedica.controller;

import com.cibertec.gestionmedica.model.Medicamento;
import com.cibertec.gestionmedica.repository.MedicamentoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medicamentos")
public class MedicamentoController {

    private final MedicamentoRepository medicamentoRepository;

    public MedicamentoController(MedicamentoRepository medicamentoRepository) {
        this.medicamentoRepository = medicamentoRepository;
    }

    @GetMapping
    public List<Medicamento> listar() {
        return medicamentoRepository.findAll();
    }

    @GetMapping("/{id}")
    public Medicamento obtener(@PathVariable Long id) {
        return medicamentoRepository.findById(id).orElseThrow();
    }

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody Medicamento medicamento) {
        if (medicamentoRepository.findByNombre(medicamento.getNombre()).isPresent()) {
            return ResponseEntity.badRequest().body("El medicamento ya existe con ese nombre");
        }
        Medicamento nuevo = medicamentoRepository.save(medicamento);
        return ResponseEntity.ok(nuevo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody Medicamento medicamento) {
        if (medicamentoRepository.findByNombre(medicamento.getNombre())
                .filter(m -> !m.getId().equals(id))
                .isPresent()) {
            return ResponseEntity.badRequest().body("Ya existe otro medicamento con ese nombre");
        }
        medicamento.setId(id);
        Medicamento actualizado = medicamentoRepository.save(medicamento);
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        medicamentoRepository.deleteById(id);
    }
}
