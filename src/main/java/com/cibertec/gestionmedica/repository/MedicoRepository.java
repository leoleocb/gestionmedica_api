package com.cibertec.gestionmedica.repository;

import com.cibertec.gestionmedica.model.Medico;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MedicoRepository extends JpaRepository<Medico, Long> {
    Optional<Medico> findByNumeroLicencia(String numeroLicencia);
}
