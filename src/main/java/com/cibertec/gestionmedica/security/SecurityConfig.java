package com.cibertec.gestionmedica.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final UserDetailsServiceImpl userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // 🔓 Endpoints públicos
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/home", "/public/**").permitAll()

                        // 👤 Pacientes: solo admin crea/elimina, paciente puede consultar su info
                        .requestMatchers(HttpMethod.POST, "/api/pacientes/**").hasAnyAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/pacientes/**").hasAnyAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/pacientes/**").hasAnyAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/pacientes/**").hasAnyAuthority("ROLE_ADMIN","ROLE_PACIENTE")

                        // 🩺 Médicos: solo admin gestiona, médico puede consultar su perfil
                        .requestMatchers(HttpMethod.POST, "/api/medicos/**").hasAnyAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/medicos/**").hasAnyAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/medicos/**").hasAnyAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/medicos/**").hasAnyAuthority("ROLE_ADMIN","ROLE_MEDICO")

                        // 📅 Citas: admin y paciente gestionan, médico puede consultar
                        .requestMatchers(HttpMethod.POST, "/api/citas/**").hasAnyAuthority("ROLE_ADMIN","ROLE_PACIENTE")
                        .requestMatchers(HttpMethod.PUT, "/api/citas/**").hasAnyAuthority("ROLE_ADMIN","ROLE_PACIENTE")
                        .requestMatchers(HttpMethod.DELETE, "/api/citas/**").hasAnyAuthority("ROLE_ADMIN","ROLE_PACIENTE")
                        .requestMatchers(HttpMethod.GET, "/api/citas/**").hasAnyAuthority("ROLE_ADMIN","ROLE_PACIENTE","ROLE_MEDICO")

                        // 📋 Expedientes: solo admin y médico
                        .requestMatchers("/api/expedientes/**").hasAnyAuthority("ROLE_ADMIN","ROLE_MEDICO")

                        // 💊 Recetas: médico crea, todos consultan
                        .requestMatchers(HttpMethod.POST, "/api/recetas/**").hasAnyAuthority("ROLE_MEDICO")
                        .requestMatchers(HttpMethod.GET, "/api/recetas/**").hasAnyAuthority("ROLE_ADMIN","ROLE_MEDICO","ROLE_PACIENTE")

                        // 🧪 Medicamentos: solo admin gestiona, todos consultan
                        .requestMatchers(HttpMethod.POST, "/api/medicamentos/**").hasAnyAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/medicamentos/**").hasAnyAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/medicamentos/**").hasAnyAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/medicamentos/**").permitAll()

                        // 🚨 Todo lo demás requiere estar autenticado
                        .anyRequest().authenticated()
                )
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
