package com.locadora.domain.enums;

public enum StatusVeiculo {

    DISPONIVEL("Disponível"),


    ALUGADO("Alugado"),


    MANUTENCAO("Manutenção"),


    BLOQUEADO("Bloqueado"),


    EXCLUIDO("Excluído");

    private final String descricao;

    StatusVeiculo(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    @Override
    public String toString() {
        return this.descricao;
    }
}