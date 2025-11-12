package com.locadora.application.dto.request;

public class UsuarioCriarRequestDto {
    private String nome;
    private String email;
    private String cpf;
    private String senha;
    private String role;
    // novo campo para permitir atualizar/definir a empresa padrão pelo id
    private Long defaultEmpresaId;

    public UsuarioCriarRequestDto() {}

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }

    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public Long getDefaultEmpresaId() { return defaultEmpresaId; }
    public void setDefaultEmpresaId(Long defaultEmpresaId) { this.defaultEmpresaId = defaultEmpresaId; }
}
