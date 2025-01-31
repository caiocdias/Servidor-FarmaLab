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
import java.util.ArrayList;
import java.util.List;
import model.Estoque;
import util.Conexao;

/**
 *
 * @author Rafae
 */
public class ControllerEstoque extends UnicastRemoteObject implements InterfaceEstoque{

    public ControllerEstoque() throws RemoteException {
        super();
    }
    
    @Override
    public void inserirEstoque(Estoque estoque) throws RemoteException {
        try{
                Conexao.conectar();
                Connection conexao = Conexao.con;
                
                if (conexao != null) {
                    String sqlEstoque = "INSERT INTO estoque (nome, habilitado, id_unidade) VALUES (?, ?, ?)";
                    PreparedStatement stmtEstoque = conexao.prepareStatement(sqlEstoque, Statement.RETURN_GENERATED_KEYS);
                    stmtEstoque.setString(1, estoque.getNome());
                    stmtEstoque.setBoolean(2, estoque.isHabilitado());
                    stmtEstoque.setInt(3, estoque.getUnidade().getId());
                    stmtEstoque.executeUpdate();

                    System.out.println("Estoque inserido com sucesso!");
                } else {
                    System.out.println("Erro: conexão com o banco de dados não foi estabelecida.");
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new RemoteException("Erro ao inserir o estoque: " + e.getMessage());
            } finally {
                Conexao.desconectar();
            }
    }

    @Override
    public void atualizarEstoque(Estoque estoque) throws RemoteException {
        try {
            Conexao.conectar();
            Connection conexao = Conexao.con;

            if (conexao != null) {
                String sqlEstoque = "UPDATE estoque SET nome = ?, habilitado = ?, id_unidade = ?, updated_at = CURRENT_TIMESTAMP WHERE id = ?";
                PreparedStatement stmtEstoque = conexao.prepareStatement(sqlEstoque, Statement.RETURN_GENERATED_KEYS);
                    stmtEstoque.setString(1, estoque.getNome());
                    stmtEstoque.setBoolean(2, estoque.isHabilitado());
                    stmtEstoque.setInt(3, estoque.getUnidade().getId());
                    stmtEstoque.setInt(4, estoque.getId());
                    stmtEstoque.executeUpdate();

                System.out.println("Estoque atualizado com sucesso!");
            } else {
                System.out.println("Erro: conexão com o banco de dados não foi estabelecida.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RemoteException("Erro ao atualizar o estoque: " + e.getMessage());
        } finally {
            Conexao.desconectar();
        }
    }

    @Override
    public void desativarEstoque(int id) throws RemoteException {
        try {
            Conexao.conectar();
            Connection conexao = Conexao.con;

            if (conexao != null) {
                String sqlEstoque = "UPDATE estoque SET habilitado = false, updated_at = CURRENT_TIMESTAMP WHERE id = ?";
                PreparedStatement stmt = conexao.prepareStatement(sqlEstoque);
                stmt.setInt(1, id);
                int linhasAfetadas = stmt.executeUpdate();

                if (linhasAfetadas > 0) {
                    System.out.println("Estoque desativada com sucesso!");
                } else {
                    System.out.println("Nenhum estoque encontrado com o ID fornecido.");
                }
            } else {
                System.out.println("Erro: conexão com o banco de dados não foi estabelecida.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RemoteException("Erro ao desativar a estoque: " + e.getMessage());
        } finally {
            Conexao.desconectar();
        } 
    }

    @Override
    public Estoque obterEstoque(int id) throws RemoteException {
        Estoque estoque = null;
        try {
            Conexao.conectar();
            Connection conexao = Conexao.con;
            ControllerUnidade controllerUnidade = new ControllerUnidade();
            
            if (conexao != null) {
                String sql = "SELECT id, nome, endereco, habilitado, created_at, updated_at FROM estoque WHERE id = ?";
                
                PreparedStatement stmt = conexao.prepareStatement(sql);
                stmt.setInt(1, id);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    estoque = new Estoque(
                            rs.getInt("id"),
                            controllerUnidade.obterUnidade(rs.getInt("id_unidade")),
                            rs.getString("nome"),
                            rs.getBoolean("habilitado"),
                            rs.getTimestamp("created_at"),
                            rs.getTimestamp("updated_at")
                    );
                }
            } else {
                System.out.println("Erro: conexão com o banco de dados não foi estabelecida.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RemoteException("Erro ao pesquisar o estoque: " + e.getMessage());
        } finally {
            Conexao.desconectar();
        }
        return estoque; 
    }

    @Override
    public List<Estoque> buscarEstoquePorNome(String nome) throws RemoteException {
        List<Estoque> estoques = new ArrayList<>();
        try {
            Conexao.conectar();
            Connection conexao = Conexao.con;
            ControllerUnidade controllerUnidade = new ControllerUnidade();


            if (conexao != null) {
                String sql = "SELECT id, nome, id_unidade, habilitado, created_at, updated_at FROM estoque WHERE nome LIKE ? AND habilitado = 1";
                PreparedStatement stmt = conexao.prepareStatement(sql);
                stmt.setString(1, "%" + nome + "%");

                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    Estoque estoque = new Estoque(
                            rs.getInt("id"),
                            controllerUnidade.obterUnidade(rs.getInt("id_unidade")),
                            rs.getString("nome"),
                            rs.getBoolean("habilitado"),
                            rs.getTimestamp("created_at"),
                            rs.getTimestamp("updated_at")
                    );
                    estoques.add(estoque);
                }
            } else {
                System.out.println("Erro: conexão com o banco de dados não foi estabelecida.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RemoteException("Erro ao buscar estoques por nome: " + e.getMessage());
        } finally {
            Conexao.desconectar();
        }
        return estoques; 
    }
    
}
