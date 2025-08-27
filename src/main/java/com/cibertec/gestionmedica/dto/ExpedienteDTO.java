package com.cibertec.gestionmedica.dto;

import lombok.Data;

import java.util.List;

@Data
public class ExpedienteDTO {
    private String nombrePaciente;
    private String numeroIdentificacion;
    private String fechaNacimiento;

    private List<AlergiaDTO> alergias;
    private List<EnfermedadDTO> enfermedades;
    private List<CitaDTO> citas;

}
