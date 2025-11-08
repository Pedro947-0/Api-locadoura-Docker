package com.locadora.application.dto.request;



import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UsuarioLoginRequest {
    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email deve ser válido")
    private String email;

    @NotBlank(message = "Senha é obrigatória")
    private String senha;

    @NotBlank(message = "CPF é obrigatório")
    @Size(min = 11, max = 11, message = "CPF deve ter 11 dígitos")
    private String cpf;


    private Long defaultEmpresaId;

    public UsuarioLoginRequest() {}

    public UsuarioLoginRequest(String email, String senha, String cpf) {
        this.email = email;
        this.senha = senha;
        this.cpf = cpf;
    }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }

    public Long getDefaultEmpresaId() { return defaultEmpresaId; }
    public void setDefaultEmpresaId(Long defaultEmpresaId) { this.defaultEmpresaId = defaultEmpresaId; }
}
