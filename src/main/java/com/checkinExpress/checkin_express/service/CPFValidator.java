package com.checkinExpress.checkin_express.service;

import java.util.regex.Pattern;

public class CPFValidator {

    private static final Pattern CPF_PATTERN = Pattern.compile("\\d{11}");

    public static boolean isValid(String cpf) {
        if (cpf == null || !CPF_PATTERN.matcher(cpf).matches()) {
            return false;
        }

        // Aqui você pode adicionar a lógica para validar o CPF completo (verificação dos dígitos verificadores)
        // Essa verificação pode ser feita de forma manual ou utilizando uma biblioteca

        return true;
    }
}
