package com.cibertec.gestionmedica.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String email;     // 🔑 este campo debe usarse en Postman
    private String password;
}
