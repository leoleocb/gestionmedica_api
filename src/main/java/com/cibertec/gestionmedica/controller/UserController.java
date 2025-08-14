package com.cibertec.gestionmedica.controller;

import com.cibertec.gestionmedica.security.UserDetailsImpl;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @GetMapping("/me")
    public String datosUsuario(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return "🧑 Usuario ID: " + userDetails.getId() +
               ", Nombre: " + userDetails.getName() +
               ", Email: " + userDetails.getUsername() +
               ", Roles: " + userDetails.getAuthorities();
    }
}
