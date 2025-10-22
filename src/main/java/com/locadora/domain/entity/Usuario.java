package com.locadora.domain.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

import jdk.jfr.DataAmount;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;


@Entity
@Table(name = "usuarios")
public class Usuario {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    @Column(unique = true)
    private String email;
    private String senha;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(unique = true, nullable = false, length = 11)
    private String cpf;

    @Column(nullable = false)
    private String role = "USER";

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private com.locadora.domain.enums.StatusUsuario status = com.locadora.domain.enums.StatusUsuario.ATIVO;


    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public com.locadora.domain.enums.StatusUsuario getStatus() { return status; }
    public void setStatus(com.locadora.domain.enums.StatusUsuario status) { this.status = status; }

    public void atualizarUsuarioFromDTO(com.locadora.application.dto.request.UsuarioCriarRequestDto dto) {
        if (dto.getNome() != null) setNome(dto.getNome());
        if (dto.getEmail() != null) setEmail(dto.getEmail());
        if (dto.getCpf() != null) setCpf(dto.getCpf());
        if (dto.getRole() != null) setRole(dto.getRole());

    }
}
