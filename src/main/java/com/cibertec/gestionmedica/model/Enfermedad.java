package com.cibertec.gestionmedica.model;

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
    private Paciente paciente;
}
