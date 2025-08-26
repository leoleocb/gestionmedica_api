package com.cibertec.gestionmedica.controller;

import com.cibertec.gestionmedica.model.ExpedienteMedico;
import com.cibertec.gestionmedica.repository.ExpedienteMedicoRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/expedientes")
public class ExpedienteController {

    private final ExpedienteMedicoRepository expedienteRepository;

    public ExpedienteController(ExpedienteMedicoRepository expedienteRepository) {
        this.expedienteRepository = expedienteRepository;
    }

    @GetMapping
    public List<ExpedienteMedico> listar() {
        return expedienteRepository.findAll();
    }

    @GetMapping("/{id}")
    public ExpedienteMedico obtener(@PathVariable Long id) {
        return expedienteRepository.findById(id).orElseThrow();
    }

    @PostMapping
    public ExpedienteMedico crear(@RequestBody ExpedienteMedico expediente) {
        return expedienteRepository.save(expediente);
    }

    @PutMapping("/{id}")
    public ExpedienteMedico actualizar(@PathVariable Long id, @RequestBody ExpedienteMedico expediente) {
        expediente.setId(id);
        return expedienteRepository.save(expediente);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        expedienteRepository.deleteById(id);
    }
}
