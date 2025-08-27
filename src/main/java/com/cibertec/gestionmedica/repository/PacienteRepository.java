package com.cibertec.gestionmedica.repository;
import com.cibertec.gestionmedica.model.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PacienteRepository extends JpaRepository<Paciente, Long> {
    Paciente findByUsuarioId(Long usuarioId);
}
