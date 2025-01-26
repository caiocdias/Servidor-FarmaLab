package controller;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import util.Conexao;
import model.TipoInsumo;
import model.TipoProduto;

/**
 *
 * @author Thaíssa
 */

public class ControllerTipoProduto extends UnicastRemoteObject implements InterfaceTipoProduto {

    public ControllerTipoProduto() throws RemoteException {
        super();
    }

    @Override
    public void inserirTipoProduto(TipoProduto tipoProduto) throws RemoteException {
        try {
            Conexao.conectar();
            Connection conexao = Conexao.con;

            if (conexao != null) {
                String sql = "INSERT INTO tipo_produto (nome, instrucoes, habilitado, intrucoes) VALUES (?, ?, ?, ?)";
                PreparedStatement stmtTipoProduto = conexao.prepareStatement(sql);

                stmtTipoProduto.setString(1, tipoProduto.getNome());
                stmtTipoProduto.setString(2, tipoProduto.getInstrucoes());
                stmtTipoProduto.setBoolean(3, tipoProduto.isHabilitado());
                stmtTipoProduto.setString(4, tipoProduto.getInstrucoes());
                stmtTipoProduto.executeUpdate();
                
                ResultSet rs = stmtTipoProduto.getGeneratedKeys();
                int idTipoProduto = 0;
                if (rs.next()) {
                    idTipoProduto = rs.getInt(1);
                }

                String sqlTipoInsumoProduto = "INSERT INTO tipo_insumo_produto (id_insumo, id_produto) VALUES (?, ?)";
                PreparedStatement stmt = conexao.prepareStatement(sqlTipoInsumoProduto);
                
                for (TipoInsumo tipoInsumo : tipoProduto.getTipo_insumos()) {
                    stmt.setInt(1, tipoInsumo.getId());
                    stmt.setInt(2, idTipoProduto); 
                    stmt.addBatch();
                }
                stmt.executeBatch();
                System.out.println("Tipo de produto inserido com sucesso!");
            } else {
                System.out.println("Erro: conexão com o banco de dados não foi estabelecida.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RemoteException("Erro ao inserir tipo de produto: " + e.getMessage());
        } finally {
            Conexao.desconectar();
        }
    }

    @Override
    public void atualizarTipoProduto(TipoProduto tipoProduto) throws RemoteException {
        try {
            Conexao.conectar();
            Connection conexao = Conexao.con;

            if (conexao != null) {
                String sql = "UPDATE tipo_produto SET nome = ?, instrucoes = ?, habilitado = ?, updated_at = CURRENT_TIMESTAMP WHERE id = ?";
                PreparedStatement stmt = conexao.prepareStatement(sql);

                stmt.setString(1, tipoProduto.getNome());
                stmt.setString(2, tipoProduto.getInstrucoes());
                stmt.setBoolean(3, tipoProduto.isHabilitado());
                stmt.setInt(4, tipoProduto.getId());

                int linhasAfetadas = stmt.executeUpdate();

                if (linhasAfetadas > 0) {
                    System.out.println("Tipo de produto atualizado com sucesso!");
                } else {
                    System.out.println("Nenhum tipo de produto encontrado com o ID fornecido.");
                }
            } else {
                System.out.println("Erro: conexão com o banco de dados não foi estabelecida.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RemoteException("Erro ao atualizar tipo de insumo: " + e.getMessage());
        } finally {
            Conexao.desconectar();
        }
    }

    @Override
    public void desativarTipoProduto(int id) throws RemoteException {
        try {
            Conexao.conectar();
            Connection conexao = Conexao.con;

            if (conexao != null) {
                String sql = "DELETE FROM tipo_produto WHERE id = ?";
                PreparedStatement stmt = conexao.prepareStatement(sql);
                stmt.setInt(1, id);

                int linhasAfetadas = stmt.executeUpdate();

                if (linhasAfetadas > 0) {
                    System.out.println("Tipo de produto removido com sucesso!");
                } else {
                    System.out.println("Nenhum tipo de produto encontrado com o ID fornecido.");
                }
            } else {
                System.out.println("Erro: conexão com o banco de dados não foi estabelecida.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RemoteException("Erro ao remover tipo de produto: " + e.getMessage());
        } finally {
            Conexao.desconectar();
        }
    }

    @Override
    public TipoProduto obterTipoProduto(int id) throws RemoteException {
        TipoProduto tipoProduto = null;
        try {
            Conexao.conectar();
            Connection conexao = Conexao.con;

            if (conexao != null) {
                String sql = "SELECT * FROM tipo_produto WHERE id = ?";
                PreparedStatement sentenca = conexao.prepareStatement(sql);
                sentenca.setInt(1, id);
                ResultSet resultado = sentenca.executeQuery();
                List<TipoInsumo> tipoInsumos = new ArrayList<>();
                //[to-do]: fazer query para retornar lista de id dos tipos de insumo
                List<Integer> tipoInsumosIds = new ArrayList<>();
                //[to-do]: fazer um for para recuperar e adicionar em tipos_insumo os tipo_insumos a partir do id
                ControllerTipoInsumo controllerTipoInsumo = new ControllerTipoInsumo();
                tipoInsumos = controllerTipoInsumo.obterTipoInsumo(tipoInsumosIds);
                
                if (resultado.next()) {
                    tipoProduto.setId(resultado.getInt("id"));
                    tipoProduto.setNome(resultado.getString("nome"));
                    tipoProduto.setInstrucoes(resultado.getString("instrucoes"));
                    tipoProduto.setHabilitado(resultado.getBoolean("habilitado"));
                    tipoProduto.setTipo_insumos(tipoInsumos);
                    return tipoProduto;
                } else {
                    System.out.println("Tipo de produto com ID " + id + " não encontrado.");
                }
            } else {
                System.out.println("Erro: conexão com o banco de dados não foi estabelecida.");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao obter o tipo de produto: " + e.getMessage());
        } finally {
            Conexao.desconectar();
        }
        return tipoProduto;
    }

    @Override
    public List<TipoProduto> buscarTipoProdutoPorNome(String nome) throws RemoteException {
        List<TipoProduto> tipoProdutos = new ArrayList<>();
        try {
            Conexao.conectar();
            Connection conexao = Conexao.con;

            if (conexao != null) {
                String sql = "SELECT id, nome, instrucoes, habilitado, created_at, updated_at FROM tipo_produto WHERE nome LIKE ?";
                PreparedStatement stmt = conexao.prepareStatement(sql);
                stmt.setString(1, "%" + nome + "%");

                ResultSet rs = stmt.executeQuery();
                ControllerTipoInsumo controllerTipoInsumo = new ControllerTipoInsumo();
                
                while (rs.next()) {
                    TipoProduto tipoProduto = new TipoProduto();
                    List<TipoInsumo> tipo_insumos = new ArrayList<>();
                    

                    tipoProduto.setId(rs.getInt("id"));
                    tipoProduto.setNome(rs.getString("nome"));
                    tipoProduto.setInstrucoes(rs.getString("instrucoes"));
                    tipoProduto.setHabilitado(rs.getBoolean("habilitado"));
                    tipoProduto.setCreated_at(rs.getTimestamp("created_at"));
                    tipoProduto.setUpdated_at(rs.getTimestamp("updated_at"));
                    
                    //[to-do]: query recuperar tipos de insumo do tipo produto e fazer um for com a linha abaixo
                    tipo_insumos.add(controllerTipoInsumo.obterTipoInsumo(rs.getInt("tipo_insumo_id")));
                    tipoProduto.setTipo_insumos(tipo_insumos);
                    
                    tipoProdutos.add(tipoProduto);
                }
            } else {
                System.out.println("Erro: conexão com o banco de dados não foi estabelecida.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RemoteException("Erro ao buscar tipo de produto por nome: " + e.getMessage());
        } finally {
            Conexao.desconectar();
        }
        return tipoProdutos;
    }

    @Override
    public List<TipoProduto> obterTipoProduto(List<Integer> ids) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
