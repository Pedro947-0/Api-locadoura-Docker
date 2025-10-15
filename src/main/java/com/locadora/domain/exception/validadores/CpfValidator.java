package com.locadora.domain.exception.validadores;

public class CpfValidator {
    public static boolean isValid(String cpf) {
        if (cpf == null) return false;
        cpf = cpf.replaceAll("[.\\-]", "");
        if (cpf.length() != 11 || cpf.chars().distinct().count() == 1) {
            return false;
        }
        try {
            int sum1 = 0, sum2 = 0;
            for (int i = 0; i < 9; i++) {
                int digit = Character.getNumericValue(cpf.charAt(i));
                sum1 += digit * (10 - i);
                sum2 += digit * (11 - i);
            }
            int check1 = 11 - (sum1 % 11);
            check1 = (check1 >= 10) ? 0 : check1;
            sum2 += check1 * 2;
            int check2 = 11 - (sum2 % 11);
            check2 = (check2 >= 10) ? 0 : check2;
            return check1 == Character.getNumericValue(cpf.charAt(9)) && check2 == Character.getNumericValue(cpf.charAt(10));
        } catch (Exception e) {
            return false;
        }
    }
}

