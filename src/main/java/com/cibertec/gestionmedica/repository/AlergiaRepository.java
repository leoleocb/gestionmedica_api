package com.cibertec.gestionmedica.repository;

import com.cibertec.gestionmedica.model.Alergia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlergiaRepository extends JpaRepository<Alergia, Long> {
    List<Alergia> findByPacienteId(Long pacienteId);
}
