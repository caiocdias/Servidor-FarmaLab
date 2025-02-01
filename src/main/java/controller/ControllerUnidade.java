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
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import model.Tributo;
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
                    
                ResultSet rs = stmtUnidade.getGeneratedKeys();
                int idUnidade = 0;
                if (rs.next()) {
                    idUnidade = rs.getInt(1);
                }

                String sqlTributoInidade = "INSERT INTO tributo_unidade (id_tributo, id_unidade) VALUES (?, ?)";
                PreparedStatement stmtTributoUnidade = conexao.prepareStatement(sqlTributoInidade);
                
                for (Tributo tributo : unidade.getTributos()) {
                    stmtTributoUnidade.setInt(1, tributo.getId());
                    stmtTributoUnidade.setInt(2, idUnidade); 
                    stmtTributoUnidade.addBatch();
                }
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
                String sqlVenda = "UPDATE unidade SET habilitado = false, updated_at = CURRENT_TIMESTAMP WHERE id = ?";
                PreparedStatement stmt = conexao.prepareStatement(sqlVenda);
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
                String sql = "SELECT * FROM unidade WHERE id = ?";
                PreparedStatement stmt = conexao.prepareStatement(sql);
                stmt.setInt(1, id);
                ResultSet rs = stmt.executeQuery();
                
                List<Tributo> tributos = new ArrayList();
                List<Integer> tributosIds = new ArrayList<>();
                
                String sql2 = "SELECT id_tributo FROM unidade_tributo WHERE id_unidade = ?";
                PreparedStatement sentenca2 = conexao.prepareStatement(sql2);
                sentenca2.setInt(1, id);
                ResultSet resultado2 = sentenca2.executeQuery();
                
                while(resultado2.next()){
                    tributosIds.add(resultado2.getInt("id_tributo"));
                }
                
                ControllerTributo controllerTributo = new ControllerTributo();
                tributos = controllerTributo.obterTributo(tributosIds);
                
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
                            rs.getTimestamp("updated_at"),
                            tributos
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
                String sql = "SELECT id FROM unidade WHERE nome LIKE ? AND habilitado = 1";
                PreparedStatement stmt = conexao.prepareStatement(sql);
                stmt.setString(1, "%" + nome + "%");
                ResultSet rs = stmt.executeQuery();
                List<Integer> tributosIds = new ArrayList(); 
                while (rs.next()){
                    tributosIds.add(rs.getInt("id"));
                }
                unidades = obterUnidade(tributosIds);
            } 
        } catch (Exception e) {
            e.printStackTrace();
            throw new RemoteException("Erro ao buscar unidade por nome: " + e.getMessage());
        } finally {
            Conexao.desconectar();
        }
        return unidades;
    }

    @Override
    public List<Unidade> obterUnidade(List<Integer> ids) throws RemoteException {
        List<Unidade> unidades = new ArrayList();
        try {
            Conexao.conectar();
            Connection conexao = Conexao.con;

            if (conexao != null) {
                String sql = "SELECT * FROM unidade WHERE id = ?";
                PreparedStatement sentenca = conexao.prepareStatement(sql);
                String tributosIds = new String();
                for (int i = 0; i < ids.size(); i++) {
                    String tributoId = ids.get(i).toString();
                    tributosIds += tributoId;
                    if (i != ids.size() - 1) {
                        tributosIds += ",";
                    } 
                }
                sentenca.setString(1, tributosIds);
                ResultSet resultado = sentenca.executeQuery();
                
                while (resultado.next()) {
                    List<Integer> listTributosIds = new ArrayList<>();
                    List<Tributo> tributos = new ArrayList<>();
                    
                    String sql2 = "SELECT id_tributo FROM unidade_tributo WHERE id_unidade = ?";
                    PreparedStatement sentenca2 = conexao.prepareStatement(sql2);
                    sentenca2.setInt(1, resultado.getInt("id"));
                    ResultSet resultado2 = sentenca2.executeQuery();
                    
                    while(resultado2.next()){
                        listTributosIds.add(resultado2.getInt("tributo_id"));
                    }

                    ControllerTributo controllerTributo = new ControllerTributo();
                    tributos = controllerTributo.obterTributo(listTributosIds);
                    
                    Unidade unidade = new Unidade(
                        resultado.getInt("id"),
                        resultado.getString("nome"),
                        resultado.getString("cep"),
                        resultado.getString("cidade"),
                        resultado.getString("bairro"),
                        resultado.getString("rua"),
                        resultado.getString("complemento"),
                        resultado.getString("estado"),
                        resultado.getBoolean("habilitado"),
                        resultado.getTimestamp("created_at"),
                        resultado.getTimestamp("updated_at"),
                        tributos
                    );
                    unidades.add(unidade);
                }
            } else {
                System.out.println("Erro: conexão com o banco de dados não foi estabelecida.");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao obter o tributo: " + e.getMessage());
        } finally {
            Conexao.desconectar();
        }
        return unidades;
    }
    
    
}
