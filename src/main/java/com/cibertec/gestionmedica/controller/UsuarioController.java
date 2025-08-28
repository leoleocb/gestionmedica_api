package com.cibertec.gestionmedica.controller;

import com.cibertec.gestionmedica.model.Role;
import com.cibertec.gestionmedica.model.Usuario;
import com.cibertec.gestionmedica.repository.UsuarioRepository;
import com.cibertec.gestionmedica.repository.RoleRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioRepository usuarioRepository;
    private final RoleRepository roleRepository;

    public UsuarioController(UsuarioRepository usuarioRepository, RoleRepository roleRepository) {
        this.usuarioRepository = usuarioRepository;
        this.roleRepository = roleRepository;
    }

    @GetMapping
    public List<Usuario> listar() {
        return usuarioRepository.findAll();
    }

    @GetMapping("/{id}")
    public Usuario obtener(@PathVariable Long id) {
        return usuarioRepository.findById(id).orElseThrow();
    }

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody Usuario usuario) {
        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            return ResponseEntity.badRequest().body("El email ya está registrado");
        }
        Set<Role> roles = new HashSet<>();
        for (Role r : usuario.getRoles()) {
            Role role = roleRepository.findById(r.getId()).orElseThrow();
            roles.add(role);
        }
        usuario.setRoles(roles);
        Usuario nuevo = usuarioRepository.save(usuario);
        return ResponseEntity.ok(nuevo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody Usuario usuario) {
        Usuario existente = usuarioRepository.findById(id).orElseThrow();

        if (!existente.getEmail().equals(usuario.getEmail()) &&
                usuarioRepository.existsByEmail(usuario.getEmail())) {
            return ResponseEntity.badRequest().body("El email ya está registrado");
        }

        existente.setName(usuario.getName());
        existente.setEmail(usuario.getEmail());
        if (usuario.getPassword() != null && !usuario.getPassword().isBlank()) {
            existente.setPassword(usuario.getPassword());
        }

        Set<Role> roles = new HashSet<>();
        for (Role r : usuario.getRoles()) {
            Role role = roleRepository.findById(r.getId()).orElseThrow();
            roles.add(role);
        }
        existente.setRoles(roles);

        Usuario actualizado = usuarioRepository.save(existente);
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        usuarioRepository.deleteById(id);
    }
}
