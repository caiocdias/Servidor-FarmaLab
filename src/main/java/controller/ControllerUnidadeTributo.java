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
import util.Conexao;

/**
 *
 * @author Isabely
 */
public class ControllerUnidadeTributo extends UnicastRemoteObject implements InterfaceUnidadeTributo {
    
    public ControllerUnidadeTributo() throws RemoteException {
        super();
    }

    @Override
    public void inserirRelacionamento(int idUnidade, int idTributo) throws RemoteException {
        try {
            Conexao.conectar();
            Connection conexao = Conexao.con;

            if (conexao != null) {
                String sql = "INSERT INTO unidade_tributo (id_unidade, id_tributo) VALUES (?, ?)";
                PreparedStatement stmt = conexao.prepareStatement(sql);
                stmt.setInt(1, idUnidade);
                stmt.setInt(2, idTributo);
                stmt.executeUpdate();

                System.out.println("Relacionamento inserido com sucesso!");
            } else {
                System.out.println("Erro: conexão com o banco de dados não foi estabelecida.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RemoteException("Erro ao inserir relacionamento: " + e.getMessage());
        } finally {
            Conexao.desconectar();
        }
    }

    @Override
    public boolean verificarRelacionamento(int idUnidade, int idTributo) throws RemoteException {
        boolean existe = false;
        try {
            Conexao.conectar();
            Connection conexao = Conexao.con;

            if (conexao != null) {
                String sql = "SELECT COUNT(*) FROM unidade_tributo WHERE id_unidade = ? AND id_tributo = ?";
                PreparedStatement stmt = conexao.prepareStatement(sql);
                stmt.setInt(1, idUnidade);
                stmt.setInt(2, idTributo);
                ResultSet rs = stmt.executeQuery();

                if (rs.next() && rs.getInt(1) > 0) {
                    existe = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RemoteException("Erro ao verificar relacionamento: " + e.getMessage());
        } finally {
            Conexao.desconectar();
        }
    return existe;
    }
}
    

