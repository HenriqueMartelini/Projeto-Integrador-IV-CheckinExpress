package com.checkinExpress.checkin_express.server;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Component;

import java.net.ServerSocket;
import java.util.ArrayList;

@Component
public class Servidor implements InitializingBean, DisposableBean {
    public static final String PORTA_PADRAO = "3000";
    private Thread servidorThread;
    private boolean rodando = false;

    @Override
    public void afterPropertiesSet() {
        servidorThread = new Thread(() -> {
            String porta = PORTA_PADRAO;
            ArrayList<Parceiro> usuarios = new ArrayList<>();
            AceitadoraDeConexao aceitadoraDeConexao;

            try {
                aceitadoraDeConexao = new AceitadoraDeConexao(porta, usuarios);
                aceitadoraDeConexao.start();
                rodando = true;
                System.out.println("Servidor iniciado na porta " + porta);

                while (rodando) {
                    ServerSocket serverSocket = new ServerSocket();
                    System.out.println("Servidor iniciado na porta: " + porta);
                    System.out.println("O servidor está ativo! Para desativá-lo, use o comando \"desativar\".");
                    System.out.print("> ");

                    String comando;
                    try {
                        comando = Teclado.getUmString();
                    } catch (Exception erro) {
                        continue; // Continua o loop caso ocorra erro na entrada
                    }

                    if ("desativar".equalsIgnoreCase(comando)) {
                        synchronized (usuarios) {
                            ComunicadoDeDesligamento comunicadoDeDesligamento = new ComunicadoDeDesligamento();
                            for (Parceiro usuario : usuarios) {
                                try {
                                    usuario.receba(comunicadoDeDesligamento);
                                    usuario.adeus();
                                } catch (Exception erro) {
                                    // Ignorar erros durante o desligamento
                                }
                            }
                        }
                        rodando = false;
                        System.out.println("O servidor foi desativado!");
                    } else {
                        System.err.println("Comando inválido!");
                    }
                }
            } catch (Exception erro) {
                System.err.println("Erro ao iniciar o servidor: " + erro.getMessage());
            }
        });

        servidorThread.start();
    }

    @Override
    public void destroy() {
        System.out.println("Servidor está sendo finalizado...");
        rodando = false;
        if (servidorThread != null) {
            try {
                servidorThread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("Erro ao finalizar o servidor: " + e.getMessage());
            }
        }
    }
}