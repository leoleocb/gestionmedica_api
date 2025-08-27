package com.cibertec.gestionmedica.controller;

import com.cibertec.gestionmedica.dto.*;
import com.cibertec.gestionmedica.model.Paciente;
import com.cibertec.gestionmedica.repository.PacienteRepository;
import com.cibertec.gestionmedica.security.UserDetailsImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/expedientes")
public class ExpedienteController {

    private final PacienteRepository pacienteRepository;

    public ExpedienteController(PacienteRepository pacienteRepository) {
        this.pacienteRepository = pacienteRepository;
    }

    @GetMapping("/mis-expedientes")
    public ResponseEntity<?> getMiExpediente(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        Paciente paciente = pacienteRepository.findByUsuarioId(userDetails.getId());
        if (paciente == null) {
            return ResponseEntity.status(404).body("⚠️ No se encontró el expediente del paciente");
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
            return ResponseEntity.status(403).body("⚠️ No tienes permiso para ver el expediente de otro paciente");
        }

        return ResponseEntity.ok(buildDTO(paciente));
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
