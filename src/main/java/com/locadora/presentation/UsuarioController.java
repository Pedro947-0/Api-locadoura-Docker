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

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {
    private final UsuarioRepository usuarioRepository;
    private final UsuarioService usuarioService;
    @Autowired
    public UsuarioController(UsuarioRepository usuarioRepository, UsuarioService usuarioService) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioService = usuarioService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<Usuario> listar() {
        try {
            return usuarioRepository.findAll();
        } catch (Exception e) {
            System.err.println("Erro ao listar usuários: " + e.getMessage());
            throw new RuntimeException("Erro ao listar usuários");
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

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletar(@PathVariable Long id) {
        try {
            if (!usuarioRepository.existsById(id)) {
                return ResponseEntity.notFound().build();
            }
            usuarioRepository.deleteById(id);
            return ResponseEntity.ok("Usuário removido com sucesso");
        } catch (Exception e) {
            System.err.println("Erro ao remover usuário: " + e.getMessage());
            return ResponseEntity.internalServerError().body("Erro ao remover usuário");
        }
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ADMIN')")
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

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletarUsuario(@PathVariable Long id) {
        return usuarioService.excluirUsuario(id) ?
            ResponseEntity.noContent().build() :
            ResponseEntity.notFound().build();
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
