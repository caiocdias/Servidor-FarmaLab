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
import model.Tributo;
import util.Conexao;

/**
 *
 * @author Rafae
 */
public class ControllerTributo extends UnicastRemoteObject implements InterfaceTributo{
    
    public ControllerTributo() throws RemoteException {
        super();
    }

    @Override
    public void inserirTributo(Tributo tributo) throws RemoteException {
        try{
                Conexao.conectar();
                Connection conexao = Conexao.con;

                if (conexao != null) {
                    String sqlTributo = "INSERT INTO tributo (estado, nome_imposto, porcentagem, habilitado) VALUES (?, ?, ?, ?)";
                    PreparedStatement stmtTributo = conexao.prepareStatement(sqlTributo, Statement.RETURN_GENERATED_KEYS);
                    stmtTributo.setString(1, tributo.getEstado());
                    stmtTributo.setString(2, tributo.getNome_imposto());
                    stmtTributo.setFloat(3, tributo.getPorcentagem());
                    stmtTributo.setBoolean(4, tributo.isHabilitado());
                    stmtTributo.executeUpdate();

                    System.out.println("Tributo inserido com sucesso!");
                } else {
                    System.out.println("Erro: conexão com o banco de dados não foi estabelecida.");
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new RemoteException("Erro ao inserir a Tributo: " + e.getMessage());
            } finally {
                Conexao.desconectar();
            }
    }

    @Override
    public void atualizarTributo(Tributo tributo) throws RemoteException {
        try {
            Conexao.conectar();
            Connection conexao = Conexao.con;

            if (conexao != null) {
                String sqlTributo = "UPDATE tributo SET estado = ?, nome_imposto = ?, porcentagem = ?, updated_at = CURRENT_TIMESTAMP WHERE id = ?";
                PreparedStatement stmtTributo = conexao.prepareStatement(sqlTributo, Statement.RETURN_GENERATED_KEYS);
                    stmtTributo.setString(1, tributo.getEstado());
                    stmtTributo.setString(2, tributo.getNome_imposto());
                    stmtTributo.setFloat(3, tributo.getPorcentagem());
                    stmtTributo.setInt(4, tributo.getId());
                    stmtTributo.executeUpdate();

                System.out.println("Tributo atualizado com sucesso!");
            } else {
                System.out.println("Erro: conexão com o banco de dados não foi estabelecida.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RemoteException("Erro ao atualizar o tributo: " + e.getMessage());
        } finally {
            Conexao.desconectar();
        }
    }

    @Override
    public void desativarTributo(int id) throws RemoteException {
        try {
            Conexao.conectar();
            Connection conexao = Conexao.con;

            if (conexao != null) {
                String sqlTributo = "UPDATE tributo SET habilitado = false, updated_at = CURRENT_TIMESTAMP WHERE id = ?";
                PreparedStatement stmt = conexao.prepareStatement(sqlTributo);
                stmt.setInt(1, id);
                int linhasAfetadas = stmt.executeUpdate();

                if (linhasAfetadas > 0) {
                    System.out.println("Tributo desativado com sucesso!");
                } else {
                    System.out.println("Nenhum tributo encontrado com o ID fornecido.");
                }
            } else {
                System.out.println("Erro: conexão com o banco de dados não foi estabelecida.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RemoteException("Erro ao desativar o tributo: " + e.getMessage());
        } finally {
            Conexao.desconectar();
        }
    }

    @Override
    public Tributo obterTributo(int id) throws RemoteException {
        Tributo tributo = null;
        try {
            Conexao.conectar();
            Connection conexao = Conexao.con;

            if (conexao != null) {
                String sql = "SELECT id, estado, nome_imposto, porcentagem, habilitado, created_at, updated_at FROM tributo WHERE id = ?";
                
                PreparedStatement stmt = conexao.prepareStatement(sql);
                stmt.setInt(1, id);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    tributo = new Tributo(
                            rs.getInt("id"),
                            rs.getString("estado"),
                            rs.getString("nome_imposto"),
                            rs.getFloat("porcentagem"),
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
            throw new RemoteException("Erro ao pesquisar o tributo: " + e.getMessage());
        } finally {
            Conexao.desconectar();
        }
        return tributo;
    }

    @Override
    public List<Tributo> buscarTributoPorNome(String nome) throws RemoteException {
        List<Tributo> tributos = new ArrayList<>();
        try {
            Conexao.conectar();
            Connection conexao = Conexao.con;

            if (conexao != null) {
                String sql = "SELECT id, estado, nome_imposto, porcentagem, habilitado, created_at, updated_at FROM tributo WHERE nome LIKE ?";
                PreparedStatement stmt = conexao.prepareStatement(sql);
                stmt.setString(1, "%" + nome + "%");

                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    Tributo tributo = new Tributo(
                            rs.getInt("id"),
                            rs.getString("estado"),
                            rs.getString("nome_imposto"),
                            rs.getFloat("porcentagem"),
                            rs.getBoolean("habilitado"),
                            rs.getTimestamp("created_at"),
                            rs.getTimestamp("updated_at")
                    );
                    tributos.add(tributo);
                }
            } else {
                System.out.println("Erro: conexão com o banco de dados não foi estabelecida.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RemoteException("Erro ao buscar tributos por nome: " + e.getMessage());
        } finally {
            Conexao.desconectar();
        }
        return tributos;
    }

    @Override
    public List<Tributo> obterTributo(List<Integer> ids) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
}
