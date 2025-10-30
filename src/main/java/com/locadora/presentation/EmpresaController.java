package com.locadora.presentation;

import com.locadora.domain.entity.Empresa;
import com.locadora.domain.repository.EmpresaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/empresas")
public class EmpresaController {

    private final EmpresaRepository empresaRepository;

    public EmpresaController(EmpresaRepository empresaRepository) {
        this.empresaRepository = empresaRepository;
    }

    @PostMapping
    public ResponseEntity<Empresa> criar(@RequestBody Empresa empresa) {
        Empresa saved = empresaRepository.save(empresa);
        return ResponseEntity.created(URI.create("/api/empresas/" + saved.getId())).body(saved);
    }

    @GetMapping
    public ResponseEntity<List<Empresa>> listar() {
        List<Empresa> todas = empresaRepository.findAll();
        return ResponseEntity.ok(todas);
    }
}
