package com.locadora.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.text.Normalizer;
import java.util.Locale;

public enum StatusUsuario {
    ATIVO("Ativo"),
    BLOQUEADO("Bloqueado"),
    EXCLUIDO("Excluído");

    private final String label;

    StatusUsuario(String label) {
        this.label = label;
    }

    @JsonValue
    public String getLabel() {
        return label;
    }

    @JsonCreator
    public static StatusUsuario from(String value) {
        if (value == null) return null;
        String normalized = normalize(value);
        for (StatusUsuario s : values()) {
            if (normalize(s.name()).equals(normalized) || normalize(s.label).equals(normalized)) {
                return s;
            }
        }
        throw new IllegalArgumentException("Unknown StatusUsuario: " + value);
    }

    private static String normalize(String input) {
        String noAccent = Normalizer.normalize(input, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "");
        return noAccent.toUpperCase(Locale.ROOT).replaceAll("[^A-Z0-9]", "");
    }

    @Override
    public String toString() {
        return this.label;
    }
}
