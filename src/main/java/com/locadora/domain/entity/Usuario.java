package com.locadora.domain.entity;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.locadora.domain.enums.StatusUsuario;

@Entity
@Table(name = "usuarios")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Usuario {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    @Column(unique = true)
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
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
    @Column(nullable = false, columnDefinition = "varchar(255) default 'ATIVO'")
    private StatusUsuario status = StatusUsuario.ATIVO;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Locacao> locacoes = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "default_empresa_id", nullable = true,
            foreignKey = @ForeignKey(name = "fk_usuario_empresa"))
    @JsonIgnoreProperties("usuarios")
    private Empresa defaultEmpresa;

    public Usuario() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; } // codificar a senha no serviço antes de persistir

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public StatusUsuario getStatus() { return status; }
    public void setStatus(StatusUsuario status) { this.status = status; }

    public List<Locacao> getLocacoes() { return locacoes; }

    public Empresa getDefaultEmpresa() { return defaultEmpresa; }
    public void setDefaultEmpresa(Empresa defaultEmpresa) {
        this.defaultEmpresa = defaultEmpresa;
    }

    public void addLocacao(Locacao locacao) {
        if (locacao == null) return;
        locacoes.add(locacao);
        locacao.setUsuario(this);
    }

    public void removeLocacao(Locacao locacao) {
        if (locacao == null) return;
        locacoes.remove(locacao);
        locacao.setUsuario(null);
    }

    public void atualizarUsuarioFromDTO(com.locadora.application.dto.request.UsuarioCriarRequestDto dto) {
        if (dto.getNome() != null) setNome(dto.getNome());
        if (dto.getEmail() != null) setEmail(dto.getEmail());
        if (dto.getCpf() != null) setCpf(dto.getCpf());
        if (dto.getRole() != null) setRole(dto.getRole());
        if (dto.getSenha() != null) {

            setSenha(dto.getSenha());
        }

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Usuario)) return false;
        Usuario usuario = (Usuario) o;
        return id != null && Objects.equals(id, usuario.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}