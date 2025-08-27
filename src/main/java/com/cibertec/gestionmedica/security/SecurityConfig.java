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
                            .hasAnyAuthority("ROLE_PACIENTE", "ROLE_ADMIN")
                        .requestMatchers("/api/pacientes/**").hasAnyAuthority("ROLE_ADMIN")

                        .requestMatchers(HttpMethod.POST, "/api/medicos/**").hasAnyAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/medicos/**").hasAnyAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/medicos/**").hasAnyAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/medicos/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_MEDICO")

                        .requestMatchers(HttpMethod.GET, "/api/citas/mis-citas")
                            .hasAnyAuthority("ROLE_PACIENTE", "ROLE_ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/citas/mis-citas-medico")
                            .hasAnyAuthority("ROLE_MEDICO", "ROLE_ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/citas/**")
                            .hasAnyAuthority("ROLE_ADMIN", "ROLE_PACIENTE")
                        .requestMatchers(HttpMethod.PUT, "/api/citas/**")
                            .hasAnyAuthority("ROLE_ADMIN", "ROLE_PACIENTE", "ROLE_MEDICO")
                        .requestMatchers(HttpMethod.DELETE, "/api/citas/**")
                            .hasAnyAuthority("ROLE_ADMIN", "ROLE_PACIENTE")
                        .requestMatchers(HttpMethod.GET, "/api/citas/**")
                            .hasAnyAuthority("ROLE_ADMIN")

                        .requestMatchers(HttpMethod.GET, "/api/expedientes/mis-expedientes")
                            .hasAnyAuthority("ROLE_PACIENTE", "ROLE_ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/expedientes/paciente/**")
                            .hasAnyAuthority("ROLE_ADMIN", "ROLE_MEDICO")
                        .requestMatchers(HttpMethod.POST, "/api/expedientes/**")
                            .hasAnyAuthority("ROLE_ADMIN", "ROLE_MEDICO")
                        .requestMatchers(HttpMethod.PUT, "/api/expedientes/**")
                            .hasAnyAuthority("ROLE_ADMIN", "ROLE_MEDICO")

                        .requestMatchers(HttpMethod.GET, "/api/recetas/mis-recetas")
                            .hasAnyAuthority("ROLE_PACIENTE", "ROLE_ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/recetas/**")
                            .hasAnyAuthority("ROLE_MEDICO")
                        .requestMatchers(HttpMethod.GET, "/api/recetas/**")
                            .hasAnyAuthority("ROLE_ADMIN", "ROLE_MEDICO")
                        .requestMatchers(HttpMethod.DELETE, "/api/recetas/**")
                            .hasAnyAuthority("ROLE_ADMIN")

                        .requestMatchers(HttpMethod.POST, "/api/medicamentos/**").hasAnyAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/medicamentos/**").hasAnyAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/medicamentos/**").hasAnyAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/medicamentos/**").permitAll()

                        .requestMatchers(HttpMethod.POST, "/api/alergias/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_MEDICO")
                        .requestMatchers(HttpMethod.PUT, "/api/alergias/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_MEDICO")
                        .requestMatchers(HttpMethod.DELETE, "/api/alergias/**").hasAnyAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/alergias/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_MEDICO", "ROLE_PACIENTE")

                        .requestMatchers(HttpMethod.POST, "/api/enfermedades/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_MEDICO")
                        .requestMatchers(HttpMethod.PUT, "/api/enfermedades/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_MEDICO")
                        .requestMatchers(HttpMethod.DELETE, "/api/enfermedades/**").hasAnyAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/enfermedades/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_MEDICO", "ROLE_PACIENTE")

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
