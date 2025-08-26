package com.cibertec.gestionmedica.dto;

import lombok.Data;

@Data
public class CitaDTO {
    private Long id;
    private String fecha;
    private String hora;
    private String motivo;
    private String estado;
}
