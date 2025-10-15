package com.locadora.presentation;

import com.locadora.domain.entity.Locacao;
import com.locadora.domain.entity.Usuario;
import com.locadora.domain.entity.Veiculo;
import com.locadora.domain.enums.StatusVeiculo;
import com.locadora.domain.repository.LocacaoRepository;


import com.locadora.domain.repository.UsuarioRepository;
import com.locadora.domain.repository.VeiculoRepository;
import com.locadora.application.service.LocacaoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/api/locacoes")
public class LocacaoController {
    private final LocacaoRepository locacaoRepository;
    private final UsuarioRepository usuarioRepository;
    private final VeiculoRepository veiculoRepository;
    private final LocacaoService locacaoService;

    public LocacaoController(LocacaoRepository locacaoRepository,
                             UsuarioRepository usuarioRepository,
                             VeiculoRepository veiculoRepository,
                             LocacaoService locacaoService) {
        this.locacaoRepository = locacaoRepository;
        this.usuarioRepository = usuarioRepository;
        this.veiculoRepository = veiculoRepository;
        this.locacaoService = locacaoService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<Locacao> listar() {
        try {
            return locacaoService.listarTodas();
        } catch (Exception e) {
            System.err.println("Erro ao listar locações: " + e.getMessage());
            throw new RuntimeException("Erro ao listar locações");
        }
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> criar(@RequestBody Map<String, String> body) {
        try {
            Long usuarioId = Long.valueOf(body.get("usuarioId"));
            Long veiculoId = Long.valueOf(body.get("veiculoId"));
            LocalDate inicio = LocalDate.parse(body.get("dataInicio"));
            LocalDate fim = LocalDate.parse(body.get("dataFim"));
            boolean sucesso = locacaoService.criarLocacao(usuarioId, veiculoId, inicio, fim);
            return sucesso ? ResponseEntity.noContent().build() : ResponseEntity.badRequest().body("Usuário, veículo inválido ou veículo indisponível");
        } catch (Exception e) {
            System.err.println("Erro ao criar locação: " + e.getMessage());
            return ResponseEntity.badRequest().body("Erro ao criar locação");
        }
    }
}
