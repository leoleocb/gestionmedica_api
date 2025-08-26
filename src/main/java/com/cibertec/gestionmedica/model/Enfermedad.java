package com.cibertec.gestionmedica.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "enfermedad")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Enfermedad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String descripcion;

    @ManyToOne
    @JoinColumn(name = "paciente_id")
    @JsonIgnoreProperties({"alergias","enfermedades","citas","recetas"})
    private Paciente paciente;
}
