package com.locadora.application.dto.response;

import com.locadora.domain.enums.StatusVeiculo;



public class VeiculoResponse {
    private Long id;
    private String placa;
    private String modelo;
    private StatusVeiculo status;

    public VeiculoResponse() {}

    public VeiculoResponse(Long id, String placa, String modelo, StatusVeiculo status) {
        this.id = id;
        this.placa = placa;
        this.modelo = modelo;
        this.status = status;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getPlaca() { return placa; }
    public void setPlaca(String placa) { this.placa = placa; }
    public String getModelo() { return modelo; }
    public void setModelo(String modelo) { this.modelo = modelo; }
    public StatusVeiculo getStatus() { return status; }
    public void setStatus(StatusVeiculo status) { this.status = status; }
}
