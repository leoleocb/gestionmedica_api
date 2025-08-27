package com.cibertec.gestionmedica.service;

import com.cibertec.gestionmedica.dto.*;
import com.cibertec.gestionmedica.model.Paciente;
import com.cibertec.gestionmedica.model.Role;
import com.cibertec.gestionmedica.model.Usuario;
import com.cibertec.gestionmedica.repository.PacienteRepository;
import com.cibertec.gestionmedica.repository.RoleRepository;
import com.cibertec.gestionmedica.repository.UsuarioRepository;
import com.cibertec.gestionmedica.security.JwtUtils;
import com.cibertec.gestionmedica.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authManager;
    private final UsuarioRepository usuarioRepo;
    private final RoleRepository roleRepo;
    private final PacienteRepository pacienteRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    public JwtResponse login(LoginRequest request) {
        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        Usuario usuario = usuarioRepo.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

       
        UserDetailsImpl userDetails = UserDetailsImpl.build(usuario);
        String token = jwtUtils.generateToken(userDetails);

        List<String> roles = usuario.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toList());

        return new JwtResponse(
                token,
                "Bearer",
                usuario.getId(),
                usuario.getName(),
                usuario.getEmail(),
                roles
        );
    }

    public MessageResponse register(RegisterRequest request) {
        if (usuarioRepo.existsByEmail(request.getEmail())) {
            return new MessageResponse("Error: el email ya está en uso");
        }

     
        Usuario usuario = new Usuario();
        usuario.setName(request.getName());
        usuario.setEmail(request.getEmail());
        usuario.setPassword(passwordEncoder.encode(request.getPassword()));

        Role pacienteRole = roleRepo.findByName("ROLE_PACIENTE")
                .orElseThrow(() -> new RuntimeException("Error: rol no encontrado"));
        usuario.setRoles(Set.of(pacienteRole));

        usuarioRepo.save(usuario);

      
        Paciente paciente = new Paciente();
        paciente.setNombre(request.getName());
        paciente.setNumeroIdentificacion(request.getNumeroIdentificacion());
        paciente.setFechaNacimiento(LocalDate.parse(request.getFechaNacimiento()));
        paciente.setUsuario(usuario);
        pacienteRepo.save(paciente);

        return new MessageResponse("✅ Usuario y perfil de paciente creados correctamente");
    }
}
