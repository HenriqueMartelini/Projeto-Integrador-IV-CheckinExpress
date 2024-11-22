package com.checkinExpress.checkin_express.service;

import java.util.regex.Pattern;

public class PassportValidator {

    private static final Pattern PASSPORT_PATTERN = Pattern.compile("[A-Za-z0-9]+");

    public static boolean isValid(String passportNumber) {
        if (passportNumber == null || !PASSPORT_PATTERN.matcher(passportNumber).matches()) {
            return false;
        }

        // Caso necessário, adicione validações mais específicas para o número do passaporte

        return true;
    }
}

