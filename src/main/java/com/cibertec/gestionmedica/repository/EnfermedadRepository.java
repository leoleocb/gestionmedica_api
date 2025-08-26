package com.cibertec.gestionmedica.repository;

import com.cibertec.gestionmedica.model.Enfermedad;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EnfermedadRepository extends JpaRepository<Enfermedad, Long> {
    List<Enfermedad> findByPacienteId(Long pacienteId);
}
