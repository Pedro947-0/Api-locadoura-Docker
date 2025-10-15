package com.locadora.domain.exception.validadores;

import org.springframework.stereotype.Component;

@Component
public class PlacaValidator {
    public boolean isValid(String placa) {
        if (placa == null || placa.trim().isEmpty()) {
            return false;
        }
        placa = placa.trim().toUpperCase();
        if (placa.length() != 7) {
            return false;
        }
        return isFormatoPadrao(placa) || isFormatoMercosul(placa);
    }
    private boolean isFormatoPadrao(String placa) {
        return placa.matches("^[A-Z]{3}[0-9]{4}$");
    }
    private boolean isFormatoMercosul(String placa) {
        return placa.matches("^[A-Z]{3}[0-9][A-Z][0-9]{2}$");
    }
    public String format(String placa) {
        if (placa == null) return null;
        return placa.replaceAll("[^A-Za-z0-9]", "").toUpperCase();
    }
}

