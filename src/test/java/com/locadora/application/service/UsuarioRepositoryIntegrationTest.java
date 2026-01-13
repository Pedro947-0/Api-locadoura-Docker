package com.locadora.application.service;

import com.locadora.domain.entity.Usuario;
import com.locadora.domain.enums.StatusUsuario;
import com.locadora.domain.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class UsuarioRepositoryIntegrationTest {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @BeforeEach
    void setup() {
        usuarioRepository.deleteAll();

        Usuario u1 = new Usuario();
        u1.setNome("Fulano");
        u1.setEmail("fulano@example.com");
        u1.setCpf("12345678909");
        u1.setSenha("senha1");
        usuarioRepository.save(u1);

        Usuario u2 = new Usuario();
        u2.setNome("Maria");
        u2.setEmail("maria@example.com");
        u2.setCpf("11122233396");
        u2.setSenha("senha2");
        usuarioRepository.save(u2);
    }

    @Test
    void whenFindAllByStatusNot_thenReturnUsers() {
        List<Usuario> users = usuarioRepository.findAllByStatusNot(StatusUsuario.EXCLUIDO);
        assertNotNull(users);
        assertEquals(2, users.size());
    }

    @Test
    void whenFindByEmail_thenReturnOptional() {
        Optional<Usuario> opt = usuarioRepository.findByEmail("fulano@example.com");
        assertTrue(opt.isPresent());
        assertEquals("Fulano", opt.get().getNome());
    }
}

