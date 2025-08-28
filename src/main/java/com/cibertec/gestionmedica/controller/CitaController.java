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

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
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

    @GetMapping
    public List<Cita> listar() {
        return citaRepository.findAll();
    }

    @GetMapping("/mis-citas")
    public List<Cita> misCitas(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        Paciente paciente = pacienteRepository.findByUsuarioId(userDetails.getId());
        return citaRepository.findByPacienteId(paciente.getId());
    }

    @GetMapping("/mis-citas-medico")
    public List<Cita> misCitasMedico(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        Medico medico = medicoRepository.findByUsuarioId(userDetails.getId());
        return citaRepository.findByMedicoId(medico.getId());
    }

    @GetMapping("/{id:[0-9]+}")
    public Cita obtener(@PathVariable Long id) {
        return citaRepository.findById(id).orElseThrow();
    }

    @PostMapping
    public Cita crear(@RequestBody Cita cita, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Paciente paciente = pacienteRepository.findByUsuarioId(userDetails.getId());

        long citasDelDia = citaRepository.findByPacienteId(paciente.getId()).stream()
                .filter(c -> c.getFecha().equals(cita.getFecha()))
                .count();
        if (citasDelDia >= 3) {
            throw new RuntimeException("El paciente ya tiene 3 citas programadas en ese día.");
        }

        boolean ocupado = citaRepository.findByMedicoId(cita.getMedico().getId()).stream()
                .anyMatch(c -> c.getFecha().equals(cita.getFecha()) && c.getHora().equals(cita.getHora()));
        if (ocupado) {
            throw new RuntimeException("El médico ya tiene una cita en ese horario.");
        }

        if (cita.getFecha().getDayOfWeek() == DayOfWeek.SATURDAY ||
                cita.getFecha().getDayOfWeek() == DayOfWeek.SUNDAY ||
                cita.getHora().isBefore(java.time.LocalTime.of(8, 0)) ||
                cita.getHora().isAfter(java.time.LocalTime.of(17, 0))) {
            throw new RuntimeException("Las citas solo pueden programarse de lunes a viernes entre 08:00 y 17:00.");
        }

        cita.setPaciente(paciente);
        cita.setEstado("PROGRAMADA");
        return citaRepository.save(cita);
    }

    @PutMapping("/{id:[0-9]+}")
    public Cita actualizar(@PathVariable Long id, @RequestBody Cita cita) {
        cita.setId(id);
        return citaRepository.save(cita);
    }

    @DeleteMapping("/{id:[0-9]+}")
    public void eliminar(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Cita cita = citaRepository.findById(id).orElseThrow();
        LocalDateTime ahora = LocalDateTime.now();
        LocalDateTime inicioCita = LocalDateTime.of(cita.getFecha(), cita.getHora());

        long horasDiferencia = Duration.between(ahora, inicioCita).toHours();
        if (horasDiferencia < 2) {
            throw new RuntimeException("No se puede cancelar la cita con menos de 2 horas de anticipación.");
        }

        citaRepository.deleteById(id);
    }

    @PutMapping("/{id:[0-9]+}/estado")
    public Cita actualizarEstado(@PathVariable Long id, @RequestParam String estado) {
        Cita cita = citaRepository.findById(id).orElseThrow();
        cita.setEstado(estado);
        return citaRepository.save(cita);
    }
}
