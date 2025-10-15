package com.locadora.application.service;

import com.locadora.domain.entity.Locacao;
import com.locadora.domain.entity.Usuario;
import com.locadora.domain.entity.Veiculo;
import com.locadora.domain.enums.StatusVeiculo;
import com.locadora.domain.repository.LocacaoRepository;
import com.locadora.domain.repository.UsuarioRepository;
import com.locadora.domain.repository.VeiculoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class LocacaoService {
    @Autowired
    private LocacaoRepository locacaoRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private VeiculoRepository veiculoRepository;

    public List<Locacao> listarTodas() {
        return locacaoRepository.findAll();
    }

    public boolean criarLocacao(Long usuarioId, Long veiculoId, LocalDate inicio, LocalDate fim) {
        try {
            Usuario u = usuarioRepository.findById(usuarioId).orElse(null);
            Veiculo v = veiculoRepository.findById(veiculoId).orElse(null);
            if (u == null || v == null) return false;
            if (v.getStatus() != StatusVeiculo.DISPONIVEL) return false;

            Locacao loc = new Locacao();
            loc.setUsuario(u);
            loc.setVeiculo(v);
            loc.setDataInicio(inicio);
            loc.setDataFim(fim);
            loc.setStatus("ATIVA");

            v.setStatus(StatusVeiculo.ALUGADO);
            veiculoRepository.save(v);
            locacaoRepository.save(loc);
            return true;
        } catch (Exception e) {
            System.out.println("Erro ao criar locação: " + e.getMessage());
            return false;
        }
    }
}

