package com.cibertec.gestionmedica.controller;

import com.cibertec.gestionmedica.dto.*;
import com.cibertec.gestionmedica.model.*;
import com.cibertec.gestionmedica.repository.*;
import com.cibertec.gestionmedica.security.UserDetailsImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/expedientes")
public class ExpedienteController {

    private final PacienteRepository pacienteRepository;
    private final ExpedienteMedicoRepository expedienteRepository;
    private final EntradaHistorialRepository entradaRepository;
    private final MedicoRepository medicoRepository;

    public ExpedienteController(PacienteRepository pacienteRepository,
                                ExpedienteMedicoRepository expedienteRepository,
                                EntradaHistorialRepository entradaRepository,
                                MedicoRepository medicoRepository) {
        this.pacienteRepository = pacienteRepository;
        this.expedienteRepository = expedienteRepository;
        this.entradaRepository = entradaRepository;
        this.medicoRepository = medicoRepository;
    }

    @GetMapping("/mis-expedientes")
    public ResponseEntity<?> getMiExpediente(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        Paciente paciente = pacienteRepository.findByUsuarioId(userDetails.getId());
        if (paciente == null) {
            return ResponseEntity.status(404).body("No se encontró el expediente del paciente");
        }
        return ResponseEntity.ok(buildDTO(paciente));
    }

    @GetMapping("/paciente/{id}")
    public ResponseEntity<?> getExpediente(@PathVariable Long id,
                                           @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Paciente paciente = pacienteRepository.findById(id).orElseThrow();
        boolean esPaciente = userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_PACIENTE"));
        if (esPaciente && !paciente.getUsuario().getId().equals(userDetails.getId())) {
            return ResponseEntity.status(403).body("No tienes permiso para ver este expediente");
        }
        return ResponseEntity.ok(buildDTO(paciente));
    }

    @PostMapping("/paciente/{id}/entrada")
    public ResponseEntity<?> nuevaEntrada(@PathVariable Long id,
                                          @RequestBody EntradaHistorial entrada,
                                          @AuthenticationPrincipal UserDetailsImpl userDetails) {
        boolean autorizado = userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_MEDICO") || a.getAuthority().equals("ROLE_ADMIN"));
        if (!autorizado) {
            return ResponseEntity.status(403).body("No tienes permiso para añadir entradas al historial clínico");
        }

        Paciente paciente = pacienteRepository.findById(id).orElseThrow();
        ExpedienteMedico expediente = expedienteRepository.findByPacienteId(paciente.getId())
                .orElseGet(() -> {
                    ExpedienteMedico nuevo = new ExpedienteMedico();
                    nuevo.setPaciente(paciente);
                    return expedienteRepository.save(nuevo);
                });

        Medico medico = medicoRepository.findByUsuarioId(userDetails.getId());

        entrada.setFechaHora(LocalDateTime.now());
        entrada.setExpediente(expediente);
        entrada.setMedico(medico);

        EntradaHistorial guardada = entradaRepository.save(entrada);
        return ResponseEntity.ok(guardada);
    }

    private ExpedienteDTO buildDTO(Paciente paciente) {
        ExpedienteDTO dto = new ExpedienteDTO();
        dto.setNombrePaciente(paciente.getNombre());
        dto.setNumeroIdentificacion(paciente.getNumeroIdentificacion());
        dto.setFechaNacimiento(paciente.getFechaNacimiento() != null ? paciente.getFechaNacimiento().toString() : "");
        dto.setAlergias(paciente.getAlergias().stream().map(a -> {
            AlergiaDTO adto = new AlergiaDTO();
            adto.setId(a.getId());
            adto.setNombre(a.getNombre());
            adto.setDescripcion(a.getDescripcion());
            return adto;
        }).collect(Collectors.toList()));
        dto.setEnfermedades(paciente.getEnfermedades().stream().map(e -> {
            EnfermedadDTO edto = new EnfermedadDTO();
            edto.setId(e.getId());
            edto.setNombre(e.getNombre());
            edto.setDescripcion(e.getDescripcion());
            return edto;
        }).collect(Collectors.toList()));
        dto.setCitas(paciente.getCitas().stream().map(c -> {
            CitaDTO cdto = new CitaDTO();
            cdto.setId(c.getId());
            cdto.setFecha(c.getFecha() != null ? c.getFecha().toString() : "");
            cdto.setHora(c.getHora() != null ? c.getHora().toString() : "");
            cdto.setMotivo(c.getMotivo());
            cdto.setEstado(c.getEstado());
            return cdto;
        }).collect(Collectors.toList()));
        return dto;
    }
}
