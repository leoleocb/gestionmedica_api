package com.cibertec.gestionmedica.dto;

import lombok.Data;

import java.util.List;

@Data
public class RecetaDTO {
    private Long id;
    private String fechaEmision;
    private String fechaCaducidad;
    private List<String> medicamentos;
}
