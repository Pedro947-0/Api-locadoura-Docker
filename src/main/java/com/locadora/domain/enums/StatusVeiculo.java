package com.locadora.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.text.Normalizer;
import java.util.Locale;

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

    @JsonValue
    public String getDescricao() {
        return descricao;
    }

    @JsonCreator
    public static StatusVeiculo from(String value) {
        if (value == null) return null;
        String normalized = normalize(value);
        for (StatusVeiculo s : values()) {
            if (normalize(s.name()).equals(normalized) || normalize(s.descricao).equals(normalized)) {
                return s;
            }
        }
        throw new IllegalArgumentException("Unknown StatusVeiculo: " + value);
    }

    private static String normalize(String input) {
        String noAccent = Normalizer.normalize(input, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "");
        return noAccent.toUpperCase(Locale.ROOT).replaceAll("[^A-Z0-9]", "");
    }

    @Override
    public String toString() {
        return this.descricao;
    }
}