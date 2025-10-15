package com.locadora.application.dto.request;

import com.locadora.domain.enums.StatusVeiculo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;


public class VeiculoRequest {

    @NotBlank(message = "Placa é obrigatória")
    @Pattern(regexp = "^[A-Z]{3}[0-9][A-Z0-9][0-9]{2}$|^[A-Z]{3}-[0-9]{4}$",
            message = "Placa deve estar no formato ABC1234 ou ABC-1234")
    private String placa;

    @NotBlank(message = "Modelo é obrigatório")
    @Size(min = 2, max = 100, message = "Modelo deve ter entre 2 e 100 caracteres")
    private String modelo;

    @NotBlank(message = "Marca é obrigatória")
    private String marca;

    private Integer ano;

    private StatusVeiculo status;


    public VeiculoRequest() {}

    public VeiculoRequest(String placa, String modelo) {
        this.placa = placa;
        this.modelo = modelo;
        this.status = StatusVeiculo.DISPONIVEL;
    }

    public VeiculoRequest(String placa, String modelo, String marca, Integer ano, StatusVeiculo status) {
        this.placa = placa;
        this.modelo = modelo;
        this.marca = marca;
        this.ano = ano;
        this.status = status;
    }


    public String getPlaca() { return placa; }
    public void setPlaca(String placa) { this.placa = placa; }

    public String getModelo() { return modelo; }
    public void setModelo(String modelo) { this.modelo = modelo; }

    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }

    public Integer getAno() { return ano; }
    public void setAno(Integer ano) { this.ano = ano; }

    public StatusVeiculo getStatus() { return status; }
    public void setStatus(StatusVeiculo status) { this.status = status; }

    @Override
    public String toString() {
        return "VeiculoRequest{" +
                "placa='" + placa + '\'' +
                ", modelo='" + modelo + '\'' +
                ", marca='" + marca + '\'' +
                ", ano=" + ano +
                ", status=" + status +
                '}';
    }
}