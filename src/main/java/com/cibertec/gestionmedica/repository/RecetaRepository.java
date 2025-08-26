package com.cibertec.gestionmedica.repository;

import com.cibertec.gestionmedica.model.Receta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecetaRepository extends JpaRepository<Receta, Long> {
}
