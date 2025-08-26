package com.cibertec.gestionmedica.controller;

import com.cibertec.gestionmedica.model.Medicamento;
import com.cibertec.gestionmedica.repository.MedicamentoRepository;
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
    public Medicamento crear(@RequestBody Medicamento medicamento) {
        return medicamentoRepository.save(medicamento);
    }

    @PutMapping("/{id}")
    public Medicamento actualizar(@PathVariable Long id, @RequestBody Medicamento medicamento) {
        medicamento.setId(id);
        return medicamentoRepository.save(medicamento);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        medicamentoRepository.deleteById(id);
    }
}
