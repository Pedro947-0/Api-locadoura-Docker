package com.locadora.presentation;

import com.locadora.application.dto.request.VeiculoRequest;
import com.locadora.application.dto.response.VeiculoResponse;
import com.locadora.application.service.VeiculoService;
import com.locadora.application.dto.request.LocacaoRequest;
import com.locadora.application.service.LocacaoService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.time.LocalDate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


// listar, buscar, criar, alugar, devolver e remover veículos.

@RestController
@RequestMapping("/api/veiculos")
public class VeiculoController {
    @Autowired
    private VeiculoService veiculoService;

    @Autowired
    private LocacaoService locacaoService;

    private static final Logger logger = LoggerFactory.getLogger(VeiculoController.class);


    @GetMapping
    @Operation(summary = "Listar todos os veículos")
    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    public List<VeiculoResponse> listar() {
        try {
            return veiculoService.listarTodos();
        } catch (Exception e) {
            logger.error("Erro ao listar veículos", e);
            return List.of();
        }
    }


    @GetMapping("/alugados")
    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    @Operation(summary = "Listar veículos alugados")
    public List<VeiculoResponse> listarAlugados() {
        try {
            return veiculoService.listarAlugados();
        } catch (Exception e) {
            logger.error("Erro ao listar veículos alugados", e);
            return List.of();
        }
    }


    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    @Operation(summary = "Buscar veículo por ID")
    public ResponseEntity<VeiculoResponse> buscarPorId(@PathVariable Long id) {
        try {
            return veiculoService.buscarPorId(id)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            logger.error("Erro ao buscar veículo por id: {}", id, e);
            return ResponseEntity.internalServerError().build();
        }
    }


    @PostMapping("/novo")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Criar um novo veículo")
    public ResponseEntity<VeiculoResponse> criar(@RequestBody VeiculoRequest request) {
        try {
            VeiculoResponse salvo = veiculoService.criarVeiculo(request);
            return ResponseEntity.ok(salvo);
        } catch (Exception e) {
            logger.error("Erro ao criar veículo", e);
            return ResponseEntity.badRequest().body(null);
        }
    }


    @PostMapping("/{id}/alugar")
    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    @Operation(summary = "Alugar um veículo")
    public ResponseEntity<VeiculoResponse> alugar(@PathVariable Long id) {
        try {
            VeiculoResponse alugado = veiculoService.alugarVeiculo(id);
            return ResponseEntity.ok(alugado);
        } catch (Exception e) {
            logger.error("Erro ao alugar veículo: {}", id, e);
            return ResponseEntity.badRequest().body(null);
        }
    }


    @PostMapping("/{id}/devolver")
    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    @Operation(summary = "Devolver um veículo")
    public ResponseEntity<VeiculoResponse> devolver(@PathVariable Long id) {
        try {
            VeiculoResponse devolvido = veiculoService.devolverVeiculo(id);
            return ResponseEntity.ok(devolvido);
        } catch (Exception e) {
            logger.error("Erro ao devolver veículo: {}", id, e);
            return ResponseEntity.badRequest().body(null);
        }
    }


    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir veículo", description = "Método responsável por excluir veículo")
    public ResponseEntity<?> excluirVeiculo(@PathVariable Long id) {
        return veiculoService.excluirVeiculo(id) ?
            ResponseEntity.noContent().build() :
            ResponseEntity.notFound().build();
    }


    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Atualizar veículo", description = "Método responsável por atualizar veículo")
    public ResponseEntity<?> atualizarVeiculo(@PathVariable Long id, @RequestBody VeiculoRequest veiculo) {
        return veiculoService.atualizarVeiculo(id, veiculo) ?
            ResponseEntity.noContent().build() :
            ResponseEntity.notFound().build();
    }


    @PatchMapping("/{id}/bloquear")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Bloquear veículo", description = "Método responsável por bloquear veículo")
    public ResponseEntity<?> bloquearVeiculo(@PathVariable Long id) {
        return veiculoService.bloquearVeiculo(id) ?
            ResponseEntity.noContent().build() :
            ResponseEntity.notFound().build();
    }


    @PatchMapping("/{id}/desbloquear")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Desbloquear veículo", description = "Método responsável por desbloquear veículo")
    public ResponseEntity<?> desbloquearVeiculo(@PathVariable Long id) {
        return veiculoService.desbloquearVeiculo(id) ?
            ResponseEntity.noContent().build() :
            ResponseEntity.notFound().build();
    }


    @PostMapping("/locacoes")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Adicionar nova locação")
    public ResponseEntity<?> criarLocacao(@RequestBody LocacaoRequest body) {
        try {
            Long usuarioId = body.getUsuarioId();
            Long veiculoId = body.getVeiculoId();
            LocalDate inicio = body.getDataInicio();
            LocalDate fim = body.getDataFim();
            boolean sucesso = locacaoService.criarLocacao(usuarioId, veiculoId, inicio, fim);
            return sucesso ? ResponseEntity.noContent().build() : ResponseEntity.badRequest().body("Usuário, veículo inválido ou veículo indisponível");
        } catch (Exception e) {
            logger.error("Erro ao criar locação: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body("Erro ao criar locação");
        }
    }
}
