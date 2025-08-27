package com.cibertec.gestionmedica.dto;

import lombok.Data;

@Data
public class RegisterRequest {
    private String name;
    private String email;
    private String password;
    private String numeroIdentificacion;
    private String fechaNacimiento; // formato: "1990-05-10"
}
