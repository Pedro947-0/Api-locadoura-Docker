package com.locadora.presentation;

import com.locadora.domain.entity.Empresa;
import com.locadora.domain.repository.EmpresaRepository;
import com.locadora.application.service.EmpresaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import java.net.URI;
import java.util.List;
import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api/empresas")
public class EmpresaController {

    private final EmpresaRepository empresaRepository;
    private final EmpresaService empresaService;

    public EmpresaController(EmpresaRepository empresaRepository, EmpresaService empresaService) {
        this.empresaRepository = empresaRepository;
        this.empresaService = empresaService;
    }

    @PostMapping
    @Operation(summary = "Criar nova empresa")
    public ResponseEntity<Empresa> criar(@RequestBody Empresa empresa) {
        Empresa saved = empresaRepository.save(empresa);
        return ResponseEntity.created(URI.create("/api/empresas/" + saved.getId())).body(saved);
    }

    @GetMapping
    @Operation(summary = "Listar todas as empresas")
    public ResponseEntity<List<Empresa>> listar() {
        List<Empresa> todas = empresaRepository.findAll();
        return ResponseEntity.ok(todas);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar empresa por ID")
    public ResponseEntity<Empresa> buscarPorId(@PathVariable Long id) {
        return empresaService.buscarPorId(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Excluir empresa")
    public ResponseEntity<?> excluir(@PathVariable Long id) {
        try {
            boolean sucesso = empresaService.excluir(id);
            if (!sucesso) {
                // either not found or has associated users
                var empresaOpt = empresaService.buscarPorId(id);
                if (empresaOpt.isEmpty()) return ResponseEntity.notFound().build();
                return ResponseEntity.badRequest().body("Empresa possui usuários associados e não pode ser excluída");
            }
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro ao excluir empresa");
        }
    }
}
