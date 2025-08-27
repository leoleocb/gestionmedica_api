package com.cibertec.gestionmedica.repository;

import com.cibertec.gestionmedica.model.Receta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecetaRepository extends JpaRepository<Receta, Long> {
    List<Receta> findByPacienteId(Long pacienteId);  
}
