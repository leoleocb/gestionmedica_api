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

    @GetMapping("/doctor")
    @PreAuthorize("hasRole('DOCTOR')")
    public String soloDoctor() {
        return "👨‍🔬 Bienvenido doctor autenticado.";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String soloAdmin() {
        return "🔐 Bienvenido administrador autenticado.";
    }

    @GetMapping("/doctor-o-admin")
    @PreAuthorize("hasAnyRole('DOCTOR','ADMIN')")
    public String doctorOAdmin() {
        return "🩺 Esta ruta es accesible para DOCTOR o ADMIN.";
    }
}
