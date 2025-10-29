package com.locadora.service;

import com.locadora.domain.entity.Empresa;
import com.locadora.domain.repository.EmpresaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class EmpresaService {

    private final EmpresaRepository repo;

    public EmpresaService(EmpresaRepository repo) {
        this.repo = repo;
    }

    public Empresa criar(Empresa empresa) {
        return repo.save(empresa);
    }

    @Transactional(readOnly = true)
    public Optional<Empresa> buscarPorId(Long id) {
        return repo.findById(id);
    }

    public Empresa atualizar(Long id, Empresa input) {
        Empresa e = repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Empresa não encontrada"));
        e.setNome(input.getNome());
        return repo.save(e);
    }
}

