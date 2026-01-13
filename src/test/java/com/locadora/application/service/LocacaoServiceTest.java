package com.locadora.application.service;

import com.locadora.domain.entity.Locacao;
import com.locadora.domain.entity.Usuario;
import com.locadora.domain.entity.Veiculo;
import com.locadora.domain.enums.StatusVeiculo;
import com.locadora.domain.repository.LocacaoRepository;
import com.locadora.domain.repository.UsuarioRepository;
import com.locadora.domain.repository.VeiculoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LocacaoServiceTest {

    @Mock
    private LocacaoRepository locacaoRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private VeiculoRepository veiculoRepository;

    @InjectMocks
    private LocacaoService locacaoService;

    @Test
    void criarLocacao_quandoVeiculoDisponivel_deveRetornarTrueESalvar() {
        Long usuarioId = 1L;
        Long veiculoId = 10L;
        LocalDate inicio = LocalDate.now();
        LocalDate fim = inicio.plusDays(3);

        Usuario usuario = new Usuario();
        usuario.setId(usuarioId);

        Veiculo veiculo = new Veiculo();
        veiculo.setId(veiculoId);
        veiculo.setStatus(StatusVeiculo.DISPONIVEL);

        when(usuarioRepository.findById(usuarioId)).thenReturn(Optional.of(usuario));
        when(veiculoRepository.findById(veiculoId)).thenReturn(Optional.of(veiculo));
        when(locacaoRepository.save(any(Locacao.class))).thenAnswer(invocation -> {
            Locacao l = invocation.getArgument(0);
            l.setId(100L);
            return l;
        });
        when(veiculoRepository.save(any(Veiculo.class))).thenAnswer(invocation -> invocation.getArgument(0));


        boolean result = locacaoService.criarLocacao(usuarioId, veiculoId, inicio, fim);


        assertTrue(result);
        verify(locacaoRepository, times(1)).save(any(Locacao.class));
        verify(veiculoRepository, times(1)).save(any(Veiculo.class));
        assertEquals(StatusVeiculo.ALUGADO, veiculo.getStatus());
    }

    @Test
    void criarLocacao_quandoVeiculoIndisponivel_deveRetornarFalseENaoSalvar() {
        // Arrange
        Long usuarioId = 2L;
        Long veiculoId = 20L;
        LocalDate inicio = LocalDate.now();
        LocalDate fim = inicio.plusDays(2);

        Usuario usuario = new Usuario();
        usuario.setId(usuarioId);

        Veiculo veiculo = new Veiculo();
        veiculo.setId(veiculoId);
        veiculo.setStatus(StatusVeiculo.ALUGADO);

        when(usuarioRepository.findById(usuarioId)).thenReturn(Optional.of(usuario));
        when(veiculoRepository.findById(veiculoId)).thenReturn(Optional.of(veiculo));


        boolean result = locacaoService.criarLocacao(usuarioId, veiculoId, inicio, fim);


        assertFalse(result);
        verify(locacaoRepository, never()).save(any(Locacao.class));
        verify(veiculoRepository, never()).save(any(Veiculo.class));
    }
}

