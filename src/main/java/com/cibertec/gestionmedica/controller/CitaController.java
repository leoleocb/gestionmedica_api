package com.cibertec.gestionmedica.controller;

import com.cibertec.gestionmedica.model.Cita;
import com.cibertec.gestionmedica.model.Paciente;
import com.cibertec.gestionmedica.model.Medico;
import com.cibertec.gestionmedica.repository.CitaRepository;
import com.cibertec.gestionmedica.repository.PacienteRepository;
import com.cibertec.gestionmedica.repository.MedicoRepository;
import com.cibertec.gestionmedica.security.UserDetailsImpl;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/citas")
public class CitaController {

    private final CitaRepository citaRepository;
    private final PacienteRepository pacienteRepository;
    private final MedicoRepository medicoRepository;

    public CitaController(CitaRepository citaRepository,
                          PacienteRepository pacienteRepository,
                          MedicoRepository medicoRepository) {
        this.citaRepository = citaRepository;
        this.pacienteRepository = pacienteRepository;
        this.medicoRepository = medicoRepository;
    }

    // 🔹 ADMIN: listar todas
    @GetMapping
    public List<Cita> listar() {
        return citaRepository.findAll();
    }

    // 🔹 PACIENTE: sus propias citas
    @GetMapping("/mis-citas")
    public List<Cita> misCitas(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        Paciente paciente = pacienteRepository.findByUsuarioId(userDetails.getId());
        return citaRepository.findByPacienteId(paciente.getId());
    }

    // 🔹 MÉDICO: sus citas asignadas
    @GetMapping("/mis-citas-medico")
    public List<Cita> misCitasMedico(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        Medico medico = medicoRepository.findByUsuarioId(userDetails.getId());
        return citaRepository.findByMedicoId(medico.getId());
    }

    // obtener por id (Admin o Médico)
    @GetMapping("/{id:[0-9]+}")
    public Cita obtener(@PathVariable Long id) {
        return citaRepository.findById(id).orElseThrow();
    }

    // crear cita (Paciente)
    @PostMapping
    public Cita crear(@RequestBody Cita cita, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Paciente paciente = pacienteRepository.findByUsuarioId(userDetails.getId());
        cita.setPaciente(paciente);
        cita.setEstado("PROGRAMADA");
        return citaRepository.save(cita);
    }

    // actualizar cita (Admin/Médico)
    @PutMapping("/{id:[0-9]+}")
    public Cita actualizar(@PathVariable Long id, @RequestBody Cita cita) {
        cita.setId(id);
        return citaRepository.save(cita);
    }

    // eliminar cita (Admin/Paciente)
    @DeleteMapping("/{id:[0-9]+}")
    public void eliminar(@PathVariable Long id) {
        citaRepository.deleteById(id);
    }

    // actualizar estado (Admin/Médico)
    @PutMapping("/{id:[0-9]+}/estado")
    public Cita actualizarEstado(@PathVariable Long id, @RequestParam String estado) {
        Cita cita = citaRepository.findById(id).orElseThrow();
        cita.setEstado(estado);
        return citaRepository.save(cita);
    }
}
