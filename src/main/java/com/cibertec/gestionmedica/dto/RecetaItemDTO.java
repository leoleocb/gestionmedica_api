package com.cibertec.gestionmedica.dto;

import lombok.Data;

@Data
public class RecetaItemDTO {
    private Long id;
    private String medicamento;
    private String dosis;
    private String frecuencia;
}
