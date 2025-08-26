package com.cibertec.gestionmedica.repository;

import com.cibertec.gestionmedica.model.ExpedienteMedico;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ExpedienteMedicoRepository extends JpaRepository<ExpedienteMedico, Long> {
    Optional<ExpedienteMedico> findByPacienteId(Long pacienteId);
}
