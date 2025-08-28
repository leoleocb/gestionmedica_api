package com.cibertec.gestionmedica.repository;

import com.cibertec.gestionmedica.model.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PacienteRepository extends JpaRepository<Paciente, Long> {
    Paciente findByUsuarioId(Long usuarioId);

    @Query("SELECT DISTINCT c.paciente FROM Cita c WHERE c.medico.id = :medicoId")
    List<Paciente> findPacientesByMedicoId(Long medicoId);
}
