package com.cibertec.gestionmedica.controller;

import com.cibertec.gestionmedica.model.Usuario;
import com.cibertec.gestionmedica.repository.UsuarioRepository;
import com.cibertec.gestionmedica.security.UserDetailsImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/perfil")
public class PerfilController {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public PerfilController(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

   
    @GetMapping
    public ResponseEntity<?> getPerfil(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        Usuario usuario = usuarioRepository.findById(userDetails.getId()).orElseThrow();
        usuario.setPassword(null); // nunca devolver contraseña
        return ResponseEntity.ok(usuario);
    }

    
    @PutMapping("/password")
    public ResponseEntity<?> cambiarPassword(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody Map<String, String> request) {

        String actual = request.get("actual");
        String nueva = request.get("nueva");

        Usuario usuario = usuarioRepository.findById(userDetails.getId()).orElseThrow();

        if (!passwordEncoder.matches(actual, usuario.getPassword())) {
            return ResponseEntity.badRequest().body(Map.of("message", "❌ La contraseña actual no es correcta"));
        }

        usuario.setPassword(passwordEncoder.encode(nueva));
        usuarioRepository.save(usuario);

        return ResponseEntity.ok(Map.of("message", "✅ Contraseña actualizada correctamente"));
    }
}
