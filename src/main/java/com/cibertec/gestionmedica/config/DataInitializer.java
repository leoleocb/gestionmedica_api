package com.cibertec.gestionmedica.config;

import com.cibertec.gestionmedica.model.Role;
import com.cibertec.gestionmedica.model.Usuario;
import com.cibertec.gestionmedica.repository.RoleRepository;
import com.cibertec.gestionmedica.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initData(RoleRepository roleRepository,
                               UsuarioRepository usuarioRepository,
                               PasswordEncoder passwordEncoder) {
        return args -> {
            // Crear roles si no existen
            Role adminRole = roleRepository.findByName("ROLE_ADMIN")
                    .orElseGet(() -> roleRepository.save(new Role(null, "ROLE_ADMIN")));

            Role medicoRole = roleRepository.findByName("ROLE_MEDICO")
                    .orElseGet(() -> roleRepository.save(new Role(null, "ROLE_MEDICO")));

            Role pacienteRole = roleRepository.findByName("ROLE_PACIENTE")
                    .orElseGet(() -> roleRepository.save(new Role(null, "ROLE_PACIENTE")));

            // Crear admin si no existe
            usuarioRepository.findByEmail("admin@example.com").orElseGet(() -> {
                Usuario admin = new Usuario();
                admin.setEmail("admin@example.com");
                admin.setName("Admin Root");
                admin.setPassword(passwordEncoder.encode("123456"));
                admin.setRoles(Collections.singleton(adminRole));
                return usuarioRepository.save(admin);
            });
        };
    }
}
