package com.cibertec.gestionmedica.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @GetMapping("/publico")
    public String publico() {
        return "🌐 Ruta pública accesible sin autenticación.";
    }

    @GetMapping("/paciente")
    @PreAuthorize("hasRole('PACIENTE')")
    public String soloPaciente() {
        return "👨‍⚕️ Bienvenido paciente autenticado.";
    }

    @GetMapping("/medico")
    @PreAuthorize("hasRole('MEDICO')")
    public String soloMedico() {
        return "👨‍⚕️ Bienvenido médico autenticado.";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String soloAdmin() {
        return "🔐 Bienvenido administrador autenticado.";
    }

    @GetMapping("/medico-o-admin")
    @PreAuthorize("hasAnyRole('MEDICO','ADMIN')")
    public String medicoOAdmin() {
        return "🩺 Esta ruta es accesible para MÉDICO o ADMIN.";
    }
}
