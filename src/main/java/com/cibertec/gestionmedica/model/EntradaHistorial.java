package com.cibertec.gestionmedica.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EntradaHistorial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime fechaHora;
    private String diagnostico;
    private String tratamiento;

    @ManyToOne
    @JoinColumn(name = "expediente_id")
    private ExpedienteMedico expediente;

    @ManyToOne
    @JoinColumn(name = "medico_id")
    private Medico medico;
}
