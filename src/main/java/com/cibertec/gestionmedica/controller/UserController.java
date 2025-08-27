package com.cibertec.gestionmedica.controller;

import com.cibertec.gestionmedica.security.UserDetailsImpl;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @GetMapping("/me")
    public Map<String, Object> datosUsuario(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        Map<String, Object> response = new HashMap<>();
        response.put("id", userDetails.getId());
        response.put("nombre", userDetails.getName());
        response.put("email", userDetails.getUsername());
        response.put("roles", userDetails.getAuthorities()
                .stream()
                .map(a -> a.getAuthority())
                .collect(Collectors.toList())); 
        return response;
    }
}
