package com.cibertec.gestionmedica.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "paciente")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Paciente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String numeroIdentificacion;
    private LocalDate fechaNacimiento;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @OneToMany(mappedBy = "paciente")
    @JsonIgnoreProperties("paciente")
    private List<Alergia> alergias;

    @OneToMany(mappedBy = "paciente")
    @JsonIgnoreProperties("paciente")
    private List<Enfermedad> enfermedades;

    @OneToMany(mappedBy = "paciente")
    @JsonIgnoreProperties("paciente")
    private List<Cita> citas;

    @OneToMany(mappedBy = "paciente")
    @JsonIgnoreProperties("paciente")
    private List<Receta> recetas;
}
