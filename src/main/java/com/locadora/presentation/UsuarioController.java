package com.locadora.presentation;

import com.locadora.domain.entity.Usuario;
import com.locadora.domain.repository.UsuarioRepository;
import com.locadora.application.dto.request.UsuarioRequest;
import com.locadora.application.dto.request.UsuarioLoginRequest;
import com.locadora.application.dto.response.UsuarioResponse;
import com.locadora.application.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import jakarta.validation.Valid;
import java.util.List;
import io.swagger.v3.oas.annotations.Hidden;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Hidden
@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {
    private final UsuarioRepository usuarioRepository;
    private final UsuarioService usuarioService;
    private static final Logger logger = LoggerFactory.getLogger(UsuarioController.class);
    @Autowired
    public UsuarioController(UsuarioRepository usuarioRepository, UsuarioService usuarioService) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioService = usuarioService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UsuarioResponse>> listar() {
        try {
            List<UsuarioResponse> usuarios = usuarioService.listarTodos();
            return ResponseEntity.ok(usuarios);
        } catch (Exception e) {
            logger.error("Erro ao listar usuários", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/registrar")
    public ResponseEntity<?> registrar(@Valid @RequestBody UsuarioRequest request) {
        try {
            UsuarioResponse response = usuarioService.registrar(request);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro ao registrar usuário");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody UsuarioLoginRequest request) {
        try {
            boolean valido = usuarioService.validarCredenciais(request);
            if (valido) {
                return ResponseEntity.ok("Login realizado com sucesso");
            } else {
                return ResponseEntity.status(401).body("Credenciais inválidas");
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro ao realizar login");
        }
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UsuarioResponse>> listarTodosUsuarios() {
        try {
            List<UsuarioResponse> usuarios = usuarioService.listarTodos();
            return ResponseEntity.ok(usuarios);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarUsuario(@PathVariable Long id, @RequestBody com.locadora.application.dto.request.UsuarioCriarRequestDto usuario) {
        return usuarioService.atualizarUsuario(id, usuario) ?
            ResponseEntity.noContent().build() :
            ResponseEntity.notFound().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletarUsuario(@PathVariable Long id) {
        try {
            return usuarioService.excluirUsuario(id) ?
                ResponseEntity.noContent().build() :
                ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error("Erro ao remover usuário id={}", id, e);
            return ResponseEntity.internalServerError().body("Erro ao remover usuário");
        }
    }

    @PatchMapping("/{id}/bloquear")
    public ResponseEntity<?> atualizarBloquear(@PathVariable Long id) {
        return usuarioService.bloquearUsuario(id) ?
            ResponseEntity.noContent().build() :
            ResponseEntity.notFound().build();
    }

    @PatchMapping("/{id}/desbloquear")
    public ResponseEntity<?> atualizarDesbloquear(@PathVariable Long id) {
        return usuarioService.desbloquearUsuario(id) ?
            ResponseEntity.noContent().build() :
            ResponseEntity.notFound().build();
    }
}
