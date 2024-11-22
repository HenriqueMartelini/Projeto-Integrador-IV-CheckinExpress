package com.checkinExpress.checkin_express.service;

import java.util.regex.Pattern;

public class CPFValidator {

    private static final Pattern CPF_PATTERN = Pattern.compile("\\d{11}");

    public static boolean isValid(String cpf) {
        if (cpf == null || !CPF_PATTERN.matcher(cpf).matches()) {
            return false;
        }

        // Verifica se todos os dígitos são iguais (CPF inválido)
        if (cpf.chars().distinct().count() == 1) {
            return false;
        }

        // Valida os dígitos verificadores
        return validateDigits(cpf);
    }

    private static boolean validateDigits(String cpf) {
        int[] weightsFirstDigit = {10, 9, 8, 7, 6, 5, 4, 3, 2};
        int[] weightsSecondDigit = {11, 10, 9, 8, 7, 6, 5, 4, 3, 2};

        // Calcula o primeiro dígito verificador
        int firstCheck = calculateDigit(cpf.substring(0, 9), weightsFirstDigit);

        // Calcula o segundo dígito verificador
        int secondCheck = calculateDigit(cpf.substring(0, 10), weightsSecondDigit);

        // Compara os dígitos verificadores calculados com os fornecidos
        return cpf.charAt(9) == (char) (firstCheck + '0') && cpf.charAt(10) == (char) (secondCheck + '0');
    }

    private static int calculateDigit(String base, int[] weights) {
        int sum = 0;
        for (int i = 0; i < base.length(); i++) {
            sum += (base.charAt(i) - '0') * weights[i];
        }

        int remainder = sum % 11;
        return remainder < 2 ? 0 : 11 - remainder;
    }
}