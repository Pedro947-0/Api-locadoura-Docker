package com.locadora.presentation;
import com.locadora.application.dto.request.UsuarioLoginRequest;
import com.locadora.application.dto.request.UsuarioRequest;
import com.locadora.application.dto.response.LoginResponse;
import com.locadora.application.dto.response.UsuarioResponse;
import com.locadora.application.service.JwtService;
import com.locadora.application.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.v3.oas.annotations.Operation;



@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private JwtService jwtService;

    @Operation(summary = "Adcionar novo usuário")
    @PostMapping("/register")
    public ResponseEntity<UsuarioResponse> register(@Valid @RequestBody UsuarioRequest request) {
        try {
            UsuarioResponse response = usuarioService.registrar(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println("Erro ao registrar usuário: " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @Operation(summary = "login de usuário")
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody UsuarioLoginRequest request) {
        try {
            if (!usuarioService.validarCredenciais(request)) {
                return ResponseEntity.status(401).build();
            }

            return usuarioService.buscarPorEmailECpf(request.getEmail(), request.getCpf())
                    .map(usuario -> {
                        String token = jwtService.gerarToken(usuario);
                        UsuarioResponse usuarioResponse = usuarioService.toResponse(usuario);
                        return ResponseEntity.ok(new LoginResponse(token, usuarioResponse));
                    })
                    .orElse(ResponseEntity.status(401).build());
        } catch (Exception e) {
            System.err.println("Erro ao realizar login: " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }


    @Operation(summary = " listar todos os usuários")
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/users")
    public ResponseEntity<List<UsuarioResponse>> listarUsuarios() {
        try {
            List<UsuarioResponse> usuarios = usuarioService.listarTodos();
            return ResponseEntity.ok(usuarios);
        } catch (Exception e) {
            System.err.println("Erro ao listar usuários: " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }


    @Operation(summary = "buscar usuário por id")
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/users/{id}")
    public ResponseEntity<UsuarioResponse> buscarUsuarioPorId(@PathVariable Long id) {
        try {
            return usuarioService.buscarPorId(id)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            System.err.println("Erro ao buscar usuário por id: " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @Operation(summary = "excluir usuario")
    @DeleteMapping("/users/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> excluirUsuario(@PathVariable Long id) {
        try {
            return usuarioService.excluirUsuario(id) ?
                    ResponseEntity.noContent().build() :
                    ResponseEntity.notFound().build();
        } catch (Exception e) {
            System.err.println("Erro ao excluir usuário: " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @Operation(summary = "atualizar usuário")
    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/users/{id}")
    public ResponseEntity<?> atualizarUsuario(@PathVariable Long id, @RequestBody com.locadora.application.dto.request.UsuarioCriarRequestDto usuario) {
        try {
            return usuarioService.atualizarUsuario(id, usuario) ?
                    ResponseEntity.noContent().build() :
                    ResponseEntity.notFound().build();
        } catch (Exception e) {
            System.err.println("Erro ao atualizar usuário: " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
}
