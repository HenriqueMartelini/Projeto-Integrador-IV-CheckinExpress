package com.checkinExpress.checkin_express.server;

public class ValidadorDeDados {
    public static boolean valida(String dados) {
        // Exemplo de regra de validação: verifica se o dado não está vazio e tem mais de 3 caracteres
        return dados != null && !dados.trim().isEmpty() && dados.length() > 3;
    }
}