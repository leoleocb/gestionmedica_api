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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final UserDetailsServiceImpl userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .cors().and()
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/home", "/public/**").permitAll()

                        .requestMatchers(HttpMethod.GET, "/api/pacientes/perfil")
                            .hasAnyRole("PACIENTE", "ADMIN")
                        .requestMatchers("/api/pacientes/**").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.POST, "/api/medicos/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/medicos/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/medicos/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/medicos/especialidad/**").hasAnyRole("PACIENTE","ADMIN","MEDICO")
                        .requestMatchers(HttpMethod.GET, "/api/medicos/especialidades").hasAnyRole("PACIENTE","ADMIN","MEDICO")
                        .requestMatchers(HttpMethod.GET, "/api/medicos/**").hasAnyRole("ADMIN", "MEDICO")

                        .requestMatchers(HttpMethod.GET, "/api/citas/mis-citas")
                            .hasAnyRole("PACIENTE", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/citas/mis-citas-medico")
                            .hasAnyRole("MEDICO", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/citas/disponibilidad/**")
                            .hasAnyRole("PACIENTE", "MEDICO", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/citas/**")
                            .hasAnyRole("ADMIN", "PACIENTE")
                        .requestMatchers(HttpMethod.PUT, "/api/citas/**")
                            .hasAnyRole("ADMIN", "PACIENTE", "MEDICO")
                        .requestMatchers(HttpMethod.DELETE, "/api/citas/**")
                            .hasAnyRole("ADMIN", "PACIENTE")
                        .requestMatchers(HttpMethod.GET, "/api/citas/**")
                            .hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/api/expedientes/mis-expedientes")
                            .hasAnyRole("PACIENTE", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/expedientes/paciente/**")
                            .hasAnyRole("ADMIN", "MEDICO")
                        .requestMatchers(HttpMethod.POST, "/api/expedientes/**")
                            .hasAnyRole("ADMIN", "MEDICO")
                        .requestMatchers(HttpMethod.PUT, "/api/expedientes/**")
                            .hasAnyRole("ADMIN", "MEDICO")

                        .requestMatchers(HttpMethod.GET, "/api/recetas/mis-recetas")
                            .hasAnyRole("PACIENTE", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/recetas/**")
                            .hasRole("MEDICO")
                        .requestMatchers(HttpMethod.GET, "/api/recetas/**")
                            .hasAnyRole("ADMIN", "MEDICO")
                        .requestMatchers(HttpMethod.DELETE, "/api/recetas/**")
                            .hasRole("ADMIN")

                        .requestMatchers(HttpMethod.POST, "/api/medicamentos/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/medicamentos/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/medicamentos/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/medicamentos/**").permitAll()

                        .requestMatchers(HttpMethod.POST, "/api/alergias/**").hasAnyRole("ADMIN", "MEDICO")
                        .requestMatchers(HttpMethod.PUT, "/api/alergias/**").hasAnyRole("ADMIN", "MEDICO")
                        .requestMatchers(HttpMethod.DELETE, "/api/alergias/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/alergias/**").hasAnyRole("ADMIN", "MEDICO", "PACIENTE")

                        .requestMatchers(HttpMethod.POST, "/api/enfermedades/**").hasAnyRole("ADMIN", "MEDICO")
                        .requestMatchers(HttpMethod.PUT, "/api/enfermedades/**").hasAnyRole("ADMIN", "MEDICO")
                        .requestMatchers(HttpMethod.DELETE, "/api/enfermedades/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/enfermedades/**").hasAnyRole("ADMIN", "MEDICO", "PACIENTE")

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

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:4200"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
