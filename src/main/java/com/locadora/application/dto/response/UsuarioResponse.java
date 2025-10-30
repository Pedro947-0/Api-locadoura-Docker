package com.locadora.application.dto.response;



public class UsuarioResponse {

    private Long id;
    private String nome;
    private String email;
    private String cpf;
    private Long defaultEmpresaId;


    public UsuarioResponse() {}


    public UsuarioResponse(Long id, String nome, String email, String cpf, Long defaultEmpresaId) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.cpf = cpf;
        this.defaultEmpresaId = defaultEmpresaId;
    }


    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }

    public Long getDefaultEmpresaId() { return defaultEmpresaId; }
    public void setDefaultEmpresaId(Long defaultEmpresaId) { this.defaultEmpresaId = defaultEmpresaId; }
}
