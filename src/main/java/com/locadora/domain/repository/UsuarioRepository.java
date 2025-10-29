package com.locadora.domain.repository;

import com.locadora.domain.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);
   // Optional<Usuario> findByEmailAndSenha(String email, String senha);
    Optional<Usuario> findByEmailAndCpf(String email, String cpf);
    Optional<Usuario> findByIdAndStatusNot(Long id, com.locadora.domain.enums.StatusUsuario status);
}
