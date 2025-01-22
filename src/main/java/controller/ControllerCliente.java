/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import util.Conexao;

public class ControllerCliente extends UnicastRemoteObject implements InterfaceCliente {

    public ControllerCliente() throws RemoteException {
        super();
    }

    @Override
    public void inserirCliente(String nome, String endereco, String cpf, String telefone, boolean habilitado) throws RemoteException {
        try {
            Conexao.conectar();
            Connection conexao = Conexao.con;

            if (conexao != null) {
                // Inserir na tabela pessoa
                String sqlPessoa = "INSERT INTO pessoa (nome, endereco, cpf, telefone) VALUES (?, ?, ?, ?)";
                PreparedStatement stmtPessoa = conexao.prepareStatement(sqlPessoa, Statement.RETURN_GENERATED_KEYS);
                stmtPessoa.setString(1, nome);
                stmtPessoa.setString(2, endereco);
                stmtPessoa.setString(3, cpf);
                stmtPessoa.setString(4, telefone);
                stmtPessoa.executeUpdate();

                // Obter o ID gerado para a tabela pessoa
                ResultSet rs = stmtPessoa.getGeneratedKeys();
                int idPessoa = 0;
                if (rs.next()) {
                    idPessoa = rs.getInt(1);
                }

                // Inserir na tabela clientes
                String sqlCliente = "INSERT INTO clientes (id_pessoa, habilitado) VALUES (?, ?)";
                PreparedStatement stmtCliente = conexao.prepareStatement(sqlCliente);
                stmtCliente.setInt(1, idPessoa);
                stmtCliente.setBoolean(2, habilitado);
                stmtCliente.executeUpdate();

                System.out.println("Cliente inserido com sucesso!");
            } else {
                System.out.println("Erro: conexão com o banco de dados não foi estabelecida.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RemoteException("Erro ao inserir cliente: " + e.getMessage());
        } finally {
            Conexao.desconectar();
        }
    }
}
