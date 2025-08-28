package com.cibertec.gestionmedica.repository;

import com.cibertec.gestionmedica.model.ExpedienteMedico;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpedienteRepository extends JpaRepository<ExpedienteMedico, Long> {
}
