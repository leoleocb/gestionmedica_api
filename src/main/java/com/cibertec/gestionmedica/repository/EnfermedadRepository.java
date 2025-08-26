package com.cibertec.gestionmedica.repository;

import com.cibertec.gestionmedica.model.Enfermedad;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnfermedadRepository extends JpaRepository<Enfermedad, Long> {
}
