package com.cibertec.gestionmedica.controller;

import com.cibertec.gestionmedica.model.Medico;
import com.cibertec.gestionmedica.model.Paciente;
import com.cibertec.gestionmedica.repository.MedicoRepository;
import com.cibertec.gestionmedica.repository.PacienteRepository;
import com.cibertec.gestionmedica.security.UserDetailsImpl;
import com.cibertec.gestionmedica.service.MedicoService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medicos")
public class MedicoController {

    private final MedicoService medicoService;
    private final PacienteRepository pacienteRepository;
    private final MedicoRepository medicoRepository;

    public MedicoController(MedicoService medicoService,
                            PacienteRepository pacienteRepository,
                            MedicoRepository medicoRepository) {
        this.medicoService = medicoService;
        this.pacienteRepository = pacienteRepository;
        this.medicoRepository = medicoRepository;
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

    @GetMapping("/mis-pacientes")
    public List<Paciente> misPacientes(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        Medico medico = medicoRepository.findByUsuarioId(userDetails.getId());
        return pacienteRepository.findPacientesByMedicoId(medico.getId());
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
