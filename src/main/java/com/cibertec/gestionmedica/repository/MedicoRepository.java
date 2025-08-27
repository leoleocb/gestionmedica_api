package com.cibertec.gestionmedica.repository;

import com.cibertec.gestionmedica.model.Medico;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicoRepository extends JpaRepository<Medico, Long> {
    Medico findByUsuarioId(Long usuarioId);
}
