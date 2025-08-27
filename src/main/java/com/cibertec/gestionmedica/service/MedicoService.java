package com.cibertec.gestionmedica.service;

import com.cibertec.gestionmedica.model.Medico;
import com.cibertec.gestionmedica.model.Usuario;
import com.cibertec.gestionmedica.model.Role;
import com.cibertec.gestionmedica.repository.MedicoRepository;
import com.cibertec.gestionmedica.repository.UsuarioRepository;
import com.cibertec.gestionmedica.repository.RoleRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class MedicoService {

    private final MedicoRepository medicoRepository;
    private final UsuarioRepository usuarioRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public MedicoService(MedicoRepository medicoRepository,
                         UsuarioRepository usuarioRepository,
                         RoleRepository roleRepository,
                         PasswordEncoder passwordEncoder) {
        this.medicoRepository = medicoRepository;
        this.usuarioRepository = usuarioRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Medico> listar() {
        return medicoRepository.findAll();
    }

    public Medico obtener(Long id) {
        return medicoRepository.findById(id).orElseThrow();
    }

    public Medico crear(Medico medico) {
        if (medico.getEmail() == null || medico.getEmail().isBlank()) {
            throw new RuntimeException("El médico debe tener un email válido");
        }

        Usuario usuario = new Usuario();
        usuario.setName(medico.getNombre() + " " + medico.getApellido());
        usuario.setEmail(medico.getEmail());
        usuario.setPassword(passwordEncoder.encode("123456")); // contraseña default

        Role rolMedico = roleRepository.findByName("ROLE_MEDICO")
                .orElseThrow(() -> new RuntimeException("Rol ROLE_MEDICO no encontrado"));

        usuario.setRoles(Set.of(rolMedico));

        Usuario savedUsuario = usuarioRepository.save(usuario);

        medico.setUsuario(savedUsuario);
        return medicoRepository.save(medico);
    }

    public Medico actualizar(Long id, Medico medico) {
        medico.setId(id);
        return medicoRepository.save(medico);
    }

    public void eliminar(Long id) {
        medicoRepository.deleteById(id);
    }
}

