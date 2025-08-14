package com.cibertec.gestionmedica.dto;

import lombok.Data;

import java.util.Set;

@Data
public class RegisterRequest {
    private String name;
    private String email;
    private String password;
    private Set<String> roles; // Ej: ["ROLE_PACIENTE"]
}
