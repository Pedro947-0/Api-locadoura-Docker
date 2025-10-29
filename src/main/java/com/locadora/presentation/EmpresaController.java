package com.locadora.presentation;

import com.locadora.domain.entity.Empresa;
import com.locadora.service.EmpresaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/empresas")
public class EmpresaController {

    private final EmpresaService service;

    public EmpresaController(EmpresaService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Empresa> criar(@RequestBody Empresa empresa) {
        Empresa salvo = service.criar(empresa);
        return ResponseEntity.ok(salvo);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Empresa> buscar(@PathVariable Long id) {
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Empresa> atualizar(@PathVariable Long id, @RequestBody Empresa empresa) {
        Empresa atualizado = service.atualizar(id, empresa);
        return ResponseEntity.ok(atualizado);
    }
}

