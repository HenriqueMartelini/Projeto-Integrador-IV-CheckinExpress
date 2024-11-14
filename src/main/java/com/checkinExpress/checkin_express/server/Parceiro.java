package com.checkinExpress.checkin_express.server;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Parceiro {
    private Socket conexao;
    private ObjectOutputStream transmissor;
    private ObjectInputStream receptor;

    public Parceiro(Socket conexao) throws Exception {
        if (conexao == null)
            throw new Exception("Conexão ausente");

        this.conexao = conexao;
        this.transmissor = new ObjectOutputStream(conexao.getOutputStream());
        this.receptor = new ObjectInputStream(conexao.getInputStream());
    }

    public void receba(Object obj) throws Exception {
        transmissor.writeObject(obj);
        transmissor.flush();
    }

    public void adeus() throws Exception {
        transmissor.close();
        receptor.close();
        conexao.close();
    }

    public boolean validaDados(String dados) {
        return ValidadorDeDados.valida(dados);
    }
}