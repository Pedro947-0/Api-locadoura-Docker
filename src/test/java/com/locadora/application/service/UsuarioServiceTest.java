package com.locadora.application.service;

import com.locadora.application.dto.request.UsuarioCriarRequestDto;
import com.locadora.application.dto.request.UsuarioLoginRequest;
import com.locadora.application.dto.request.UsuarioRequest;
import com.locadora.application.dto.response.UsuarioResponse;
import com.locadora.domain.entity.Empresa;
import com.locadora.domain.entity.Usuario;
import com.locadora.domain.enums.StatusUsuario;
import com.locadora.domain.repository.EmpresaRepository;
import com.locadora.domain.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private EmpresaRepository empresaRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    private BCryptPasswordEncoder passwordEncoder;

    @BeforeEach
    void setup() {
        passwordEncoder = new BCryptPasswordEncoder();
    }

    @Test
    void registrar_happyPath_shouldSaveAndReturnResponse() {

        UsuarioRequest req = new UsuarioRequest();
        req.setNome("Fulano");
        req.setEmail("fulano@example.com");
        req.setSenha("senha123");
        req.setCpf("12345678909");
        req.setRole("USER");
        req.setDefaultEmpresaId(1L);

        Empresa emp = new Empresa();
        emp.setId(1L);

        when(usuarioRepository.findByEmail(req.getEmail())).thenReturn(Optional.empty());
        when(empresaRepository.findById(1L)).thenReturn(Optional.of(emp));
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocation -> {
            Usuario u = invocation.getArgument(0);
            u.setId(42L);
            return u;
        });


        UsuarioResponse resp = usuarioService.registrar(req);


        assertNotNull(resp);
        assertEquals(42L, resp.getId());
        assertEquals(req.getNome(), resp.getNome());
        assertEquals(req.getEmail(), resp.getEmail());
        assertEquals(req.getCpf(), resp.getCpf());
        assertEquals(1L, resp.getDefaultEmpresaId());
        verify(usuarioRepository).save(any(Usuario.class));
    }

    @Test
    void validarCredenciais_happyPath_shouldReturnTrue() {

        UsuarioLoginRequest login = new UsuarioLoginRequest();
        login.setEmail("joao@example.com");
        login.setCpf("11122233396");
        login.setSenha("minhasenha");

        Usuario u = new Usuario();
        u.setId(5L);
        u.setEmail(login.getEmail());
        u.setCpf(login.getCpf());
        u.setStatus(StatusUsuario.ATIVO);
        u.setSenha(passwordEncoder.encode(login.getSenha()));

        when(usuarioRepository.findByEmailAndCpfAndStatusNot(eq(login.getEmail()), eq(login.getCpf()), any()))
                .thenReturn(Optional.of(u));


        boolean valid = usuarioService.validarCredenciais(login);


        assertTrue(valid);
    }

    @Test
    void validarCredenciais_wrongPassword_shouldReturnFalse() {

        UsuarioLoginRequest login = new UsuarioLoginRequest();
        login.setEmail("maria@example.com");
        login.setCpf("11122233396");
        login.setSenha("senhaErrada");

        Usuario u = new Usuario();
        u.setId(6L);
        u.setEmail(login.getEmail());
        u.setCpf(login.getCpf());
        u.setStatus(StatusUsuario.ATIVO);
        u.setSenha(passwordEncoder.encode("outraSenha"));

        when(usuarioRepository.findByEmailAndCpfAndStatusNot(eq(login.getEmail()), eq(login.getCpf()), any()))
                .thenReturn(Optional.of(u));


        boolean valid = usuarioService.validarCredenciais(login);

        assertFalse(valid);
    }

    @Test
    void excluirUsuario_whenExists_shouldReturnTrueAndSetStatusExcluded() {

        Usuario u = new Usuario();
        u.setId(9L);
        u.setStatus(StatusUsuario.ATIVO);

        when(usuarioRepository.findByIdAndStatusNot(eq(9L), any())).thenReturn(Optional.of(u));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(u);


        boolean result = usuarioService.excluirUsuario(9L);


        assertTrue(result);
        assertEquals(StatusUsuario.EXCLUIDO, u.getStatus());
        verify(usuarioRepository).save(u);
    }

    @Test
    void excluirUsuario_whenNotFound_shouldReturnFalse() {
        when(usuarioRepository.findByIdAndStatusNot(eq(123L), any())).thenReturn(Optional.empty());

        boolean result = usuarioService.excluirUsuario(123L);

        assertFalse(result);
    }
}

