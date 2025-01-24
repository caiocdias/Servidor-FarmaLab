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
import model.Unidade;
import util.Conexao;

/**
 *
 * @author Rafae
 */
public class ControllerUnidade extends UnicastRemoteObject implements InterfaceUnidade {
    
    public ControllerUnidade() throws RemoteException {
        super();
    }

    @Override
    public void inserirUnidade(Unidade unidade) throws RemoteException{
            
            try{
                Conexao.conectar();
                Connection conexao = Conexao.con;

                if (conexao != null) {
                    String sqlUnidade = "INSERT INTO unidade (nome, cep, cidade, bairro, rua, complemento, estado, habilitado) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                    PreparedStatement stmtUnidade = conexao.prepareStatement(sqlUnidade, Statement.RETURN_GENERATED_KEYS);
                    stmtUnidade.setString(1, unidade.getNome());
                    stmtUnidade.setString(2, unidade.getCep());
                    stmtUnidade.setString(3, unidade.getCidade());
                    stmtUnidade.setString(4, unidade.getBairro());
                    stmtUnidade.setString(5, unidade.getRua());
                    stmtUnidade.setString(6, unidade.getComplemento());
                    stmtUnidade.setString(7, unidade.getEstado());
                    stmtUnidade.setBoolean(8, unidade.isHabilitado());
                    stmtUnidade.executeUpdate();

                    System.out.println("Unidade inserida com sucesso!");
                } else {
                    System.out.println("Erro: conexão com o banco de dados não foi estabelecida.");
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new RemoteException("Erro ao inserir a unidade: " + e.getMessage());
            } finally {
                Conexao.desconectar();
            }
    }

    @Override
    public void atualizarUnidade(Unidade unidade) throws RemoteException {
        try {
            Conexao.conectar();
            Connection conexao = Conexao.con;

            if (conexao != null) {
                String sqlUnidade = "UPDATE unidade SET nome = ?, cep = ?, cidade = ?, bairro = ?, rua = ?, complemento = ?, estado = ?, updated_at = CURRENT_TIMESTAMP WHERE id = ?";
                PreparedStatement stmtUnidade = conexao.prepareStatement(sqlUnidade, Statement.RETURN_GENERATED_KEYS);
                    stmtUnidade.setString(1, unidade.getNome());
                    stmtUnidade.setString(2, unidade.getCep());
                    stmtUnidade.setString(3, unidade.getCidade());
                    stmtUnidade.setString(4, unidade.getBairro());
                    stmtUnidade.setString(5, unidade.getRua());
                    stmtUnidade.setString(6, unidade.getComplemento());
                    stmtUnidade.setString(7, unidade.getEstado());
                    stmtUnidade.setInt(8, unidade.getId());
                    stmtUnidade.executeUpdate();

                System.out.println("Unidade atualizada com sucesso!");
            } else {
                System.out.println("Erro: conexão com o banco de dados não foi estabelecida.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RemoteException("Erro ao atualizar a unidade: " + e.getMessage());
        } finally {
            Conexao.desconectar();
        }
    }

    @Override
    public void desativarUnidade(int id) throws RemoteException {
        try {
            Conexao.conectar();
            Connection conexao = Conexao.con;

            if (conexao != null) {
                String sqlCliente = "UPDATE unidade SET habilitado = false, updated_at = CURRENT_TIMESTAMP WHERE id_pessoa = ?";
                PreparedStatement stmt = conexao.prepareStatement(sqlCliente);
                stmt.setInt(1, id);
                int linhasAfetadas = stmt.executeUpdate();

                if (linhasAfetadas > 0) {
                    System.out.println("Unidade desativada com sucesso!");
                } else {
                    System.out.println("Nenhuma unidade encontrado com o ID fornecido.");
                }
            } else {
                System.out.println("Erro: conexão com o banco de dados não foi estabelecida.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RemoteException("Erro ao desativar a unidade: " + e.getMessage());
        } finally {
            Conexao.desconectar();
        }
    }

    @Override
    public Unidade obterUnidade(int id) throws RemoteException {
        Unidade unidade = null;
        try {
            Conexao.conectar();
            Connection conexao = Conexao.con;

            if (conexao != null) {
                String sql = "SELECT id, cep, endereco, cidade, bairro, rua, estado, habilitado, created_at, updated_at FROM unidade WHERE id = ?";
                
                PreparedStatement stmt = conexao.prepareStatement(sql);
                stmt.setInt(1, id);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    unidade = new Unidade(
                            rs.getInt("id"),
                            rs.getString("nome"),
                            rs.getString("cep"),
                            rs.getString("cidade"),
                            rs.getString("bairro"),
                            rs.getString("rua"),
                            rs.getString("complemento"),
                            rs.getString("estado"),
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
            throw new RemoteException("Erro ao pesquisar a unidade: " + e.getMessage());
        } finally {
            Conexao.desconectar();
        }
        return unidade;
    }

    @Override
    public List<Unidade> buscarUnidadePorNome(String nome) throws RemoteException {
        List<Unidade> unidades = new ArrayList<>();
        try {
            Conexao.conectar();
            Connection conexao = Conexao.con;

            if (conexao != null) {
                String sql = "SELECT id, cep, endereco, cidade, bairro, rua, estado, habilitado, created_at, updated_at FROM unidade WHERE nome LIKE ?";
                PreparedStatement stmt = conexao.prepareStatement(sql);
                stmt.setString(1, "%" + nome + "%");

                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    Unidade unidade = new Unidade(
                            rs.getInt("id"),
                            rs.getString("nome"),
                            rs.getString("cep"),
                            rs.getString("cidade"),
                            rs.getString("bairro"),
                            rs.getString("rua"),
                            rs.getString("complemento"),
                            rs.getString("estado"),
                            rs.getBoolean("habilitado"),
                            rs.getTimestamp("created_at"),
                            rs.getTimestamp("updated_at")
                    );
                    unidades.add(unidade);
                }
            } else {
                System.out.println("Erro: conexão com o banco de dados não foi estabelecida.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RemoteException("Erro ao buscar unidades por nome: " + e.getMessage());
        } finally {
            Conexao.desconectar();
        }
        return unidades;
    }
    
    
}
