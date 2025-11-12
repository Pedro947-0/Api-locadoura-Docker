package com.locadora.application.service;

import com.locadora.application.dto.request.VeiculoRequest;
import com.locadora.application.dto.response.VeiculoResponse;
import com.locadora.domain.entity.Veiculo;
import com.locadora.domain.enums.StatusVeiculo;
import com.locadora.domain.repository.VeiculoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VeiculoService {
    @Autowired
    private VeiculoRepository veiculoRepository;

    public VeiculoResponse criarVeiculo(VeiculoRequest request) {
        if (veiculoRepository.existsByPlaca(request.getPlaca())) {
            throw new RuntimeException("Já existe um veículo com esta placa.");
        }
        Veiculo veiculo = new Veiculo();
        veiculo.setPlaca(request.getPlaca());
        veiculo.setModelo(request.getModelo());
        veiculo.setMarca(request.getMarca());
        veiculo.setAno(request.getAno());
        veiculo.setStatus(request.getStatus() != null ? request.getStatus() : StatusVeiculo.DISPONIVEL);
        Veiculo salvo = veiculoRepository.save(veiculo);
        return toResponse(salvo);
    }

    public List<VeiculoResponse> listarTodos() {
        return veiculoRepository.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    public Optional<VeiculoResponse> buscarPorId(Long id) {
        return veiculoRepository.findById(id).map(this::toResponse);
    }

    public VeiculoResponse alugarVeiculo(Long id) {
        Veiculo veiculo = veiculoRepository.findById(id).orElseThrow(() -> new RuntimeException("Veículo não encontrado"));
        if (veiculo.getStatus() != StatusVeiculo.DISPONIVEL) {
            throw new RuntimeException("Veículo não está disponível para aluguel");
        }
        veiculo.setStatus(StatusVeiculo.ALUGADO);
        Veiculo salvo = veiculoRepository.save(veiculo);
        return toResponse(salvo);
    }

    public VeiculoResponse devolverVeiculo(Long id) {
        Veiculo veiculo = veiculoRepository.findById(id).orElseThrow(() -> new RuntimeException("Veículo não encontrado"));
        if (veiculo.getStatus() != StatusVeiculo.ALUGADO) {
            throw new RuntimeException("Veículo não está alugado");
        }
        veiculo.setStatus(StatusVeiculo.DISPONIVEL);
        Veiculo salvo = veiculoRepository.save(veiculo);
        return toResponse(salvo);
    }

    public boolean excluirVeiculo(Long id) {
        try {
            var veiculoBanco = veiculoRepository.findById(id).orElse(null);
            if (veiculoBanco == null || veiculoBanco.getStatus() == StatusVeiculo.EXCLUIDO) {
                return false;
            }
            veiculoBanco.setStatus(StatusVeiculo.EXCLUIDO);
            veiculoRepository.save(veiculoBanco);
            return true;
        } catch (Exception e) {
            System.out.println("Erro ao excluir veículo: " + e.getMessage());
            return false;
        }
    }

    public boolean bloquearVeiculo(Long id) {
        try {
            var veiculoBanco = veiculoRepository.findById(id).orElse(null);
            if (veiculoBanco == null || veiculoBanco.getStatus() == StatusVeiculo.EXCLUIDO) {
                return false;
            }
            veiculoBanco.setStatus(StatusVeiculo.BLOQUEADO);
            veiculoRepository.save(veiculoBanco);
            return true;
        } catch (Exception e) {
            System.out.println("Erro ao bloquear veículo: " + e.getMessage());
            return false;
        }
    }

    public boolean desbloquearVeiculo(Long id) {
        try {
            var veiculoBanco = veiculoRepository.findById(id).orElse(null);
            if (veiculoBanco == null || veiculoBanco.getStatus() == StatusVeiculo.EXCLUIDO) {
                return false;
            }
            veiculoBanco.setStatus(StatusVeiculo.DISPONIVEL);
            veiculoRepository.save(veiculoBanco);
            return true;
        } catch (Exception e) {
            System.out.println("Erro ao desbloquear veículo: " + e.getMessage());
            return false;
        }
    }

    public boolean atualizarVeiculo(Long id, VeiculoRequest request) {
        try {
            var veiculoBanco = veiculoRepository.findById(id).orElse(null);
            if (veiculoBanco == null || veiculoBanco.getStatus() == StatusVeiculo.EXCLUIDO) {
                return false;
            }
            veiculoBanco.atualizarVeiculoFromDTO(request);
            veiculoRepository.save(veiculoBanco);
            return true;
        } catch (Exception e) {
            System.out.println("Erro ao atualizar veículo: " + e.getMessage());
            return false;
        }
    }

    public VeiculoResponse toResponse(Veiculo veiculo) {
        return new VeiculoResponse(
                veiculo.getId(),
                veiculo.getPlaca(),
                veiculo.getModelo(),
                veiculo.getStatus()
        );
    }


    public List<VeiculoResponse> listarAlugados() {
        return veiculoRepository.findByStatus(StatusVeiculo.ALUGADO)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
}
