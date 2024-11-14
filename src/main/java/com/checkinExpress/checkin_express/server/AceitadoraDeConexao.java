package com.checkinExpress.checkin_express.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class AceitadoraDeConexao extends Thread {
    private String porta;
    private ArrayList<Parceiro> usuarios;

    public AceitadoraDeConexao(String porta, ArrayList<Parceiro> usuarios) {
        this.porta = porta;
        this.usuarios = usuarios;
    }

    public void run() {
        try (ServerSocket servidor = new ServerSocket(Integer.parseInt(porta))) {
            while (true) {
                Socket conexao = servidor.accept();
                Parceiro novoUsuario = new Parceiro(conexao);

                synchronized (usuarios) {
                    usuarios.add(novoUsuario);
                }

                System.out.println("Novo cliente conectado: " + conexao.getInetAddress().getHostAddress());
            }
        } catch (Exception erro) {
            System.err.println("Erro ao aceitar conexão: " + erro.getMessage());
        }
    }
}