package com.cibertec.gestionmedica.controller;

import com.cibertec.gestionmedica.model.Medico;
import com.cibertec.gestionmedica.service.MedicoService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medicos")
public class MedicoController {

    private final MedicoService medicoService;

    public MedicoController(MedicoService medicoService) {
        this.medicoService = medicoService;
    }

    @GetMapping
    public List<Medico> listar() {
        return medicoService.listar();
    }

    @GetMapping("/{id}")
    public Medico obtener(@PathVariable Long id) {
        return medicoService.obtener(id);
    }

    @GetMapping("/especialidad")
    public List<Medico> listarPorEspecialidad(@RequestParam String nombre) {
        return medicoService.listar().stream()
                .filter(m -> m.getEspecialidad().equalsIgnoreCase(nombre) && m.isDisponible())
                .toList();
    }

    @PostMapping
    public Medico crear(@RequestBody Medico medico) {
        return medicoService.crear(medico);
    }

    @PutMapping("/{id}")
    public Medico actualizar(@PathVariable Long id, @RequestBody Medico medico) {
        return medicoService.actualizar(id, medico);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        medicoService.eliminar(id);
    }
}
