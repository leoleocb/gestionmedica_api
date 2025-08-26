package com.cibertec.gestionmedica.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "receta_item")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecetaItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String dosis;
    private String frecuencia;

    @ManyToOne
    @JoinColumn(name = "receta_id")
    @JsonIgnoreProperties("items")
    private Receta receta;

    @ManyToOne
    @JoinColumn(name = "medicamento_id")
    @JsonIgnoreProperties("recetaItems")
    private Medicamento medicamento;
}
