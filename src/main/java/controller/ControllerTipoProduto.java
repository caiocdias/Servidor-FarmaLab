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
                String sql = "INSERT INTO tipo_produto (nome, instrucoes, valor_base, habilitado, instrucoes) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement stmtTipoProduto = conexao.prepareStatement(sql);

                stmtTipoProduto.setString(1, tipoProduto.getNome());
                stmtTipoProduto.setString(2, tipoProduto.getInstrucoes());
                stmtTipoProduto.setFloat(3, tipoProduto.getValor_base());
                stmtTipoProduto.setBoolean(4, tipoProduto.isHabilitado());
                stmtTipoProduto.setString(5, tipoProduto.getInstrucoes());
                stmtTipoProduto.executeUpdate();
                
                ResultSet rs = stmtTipoProduto.getGeneratedKeys();
                int idTipoProduto = 0;
                if (rs.next()) {
                    idTipoProduto = rs.getInt(1);
                }

                String sqlTipoInsumoProduto = "INSERT INTO tipo_insumo_produto (id_tipo_insumo, id_tipo_produto) VALUES (?, ?)";
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
                String sql = "UPDATE tipo_produto SET nome = ?, instrucoes = ?, valor_base = ?, habilitado = ?, updated_at = CURRENT_TIMESTAMP WHERE id = ?";
                PreparedStatement stmt = conexao.prepareStatement(sql);

                stmt.setString(1, tipoProduto.getNome());
                stmt.setString(2, tipoProduto.getInstrucoes());
                stmt.setFloat(3, tipoProduto.getValor_base());
                stmt.setBoolean(4, tipoProduto.isHabilitado());
                stmt.setInt(5, tipoProduto.getId());

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
                List<Integer> tipoInsumosIds = new ArrayList<>();
            
                String sql2 = "SELECT id_tipo_insumo FROM tipo_insumo_produto WHERE id_tipo_produto = ?";
                PreparedStatement sentenca2 = conexao.prepareStatement(sql2);
                sentenca2.setInt(1, id);
                ResultSet resultado2 = sentenca2.executeQuery();
                
                while(resultado2.next()){
                    tipoInsumosIds.add(resultado2.getInt("id_tipo_insumo"));
                }
                
                ControllerTipoInsumo controllerTipoInsumo = new ControllerTipoInsumo();
                tipoInsumos = controllerTipoInsumo.obterTipoInsumo(tipoInsumosIds);
                
                if (resultado.next()) {
                    tipoProduto.setId(resultado.getInt("id"));
                    tipoProduto.setNome(resultado.getString("nome"));
                    tipoProduto.setInstrucoes(resultado.getString("instrucoes"));
                    tipoProduto.setValor_base(resultado.getFloat("valor_base"));
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
                String sql = "SELECT id FROM tipo_produto WHERE nome LIKE ?";
                PreparedStatement stmt = conexao.prepareStatement(sql);
                stmt.setString(1, "%" + nome + "%");
                ResultSet rs = stmt.executeQuery();
                List<Integer> produtosIds = new ArrayList(); 
                while (rs.next()){
                    produtosIds.add(rs.getInt("id"));
                }
                tipoProdutos = obterTipoProduto(produtosIds);
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
        List<TipoProduto> tipoProdutos = new ArrayList();
        try {
            Conexao.conectar();
            Connection conexao = Conexao.con;

            if (conexao != null) {
                String sql = "SELECT * FROM tipo_produto WHERE id = ?";
                PreparedStatement sentenca = conexao.prepareStatement(sql);
                String insumosIds = new String();
                for (int i = 0; i < ids.size(); i++) {
                    String insumoId = ids.get(i).toString();
                    insumosIds += insumoId;
                    if (i != tipoProdutos.size() - 1) {
                        insumosIds += ",";
                    } 
                }
                sentenca.setString(1, insumosIds);
                ResultSet resultado = sentenca.executeQuery();
                
                while (resultado.next()) {
                    List<Integer> tipoInsumosIds = new ArrayList<>();
                    List<TipoInsumo> tipoInsumos = new ArrayList<>();
                    
                    String sql2 = "SELECT id_tipo_insumo FROM tipo_insumo_produto WHERE id_tipo_produto = ?";
                    PreparedStatement sentenca2 = conexao.prepareStatement(sql2);
                    sentenca2.setInt(1, resultado.getInt("id"));
                    ResultSet resultado2 = sentenca2.executeQuery();
                    
                    while(resultado2.next()){
                        tipoInsumosIds.add(resultado2.getInt("id_tipo_insumo"));
                    }

                    ControllerTipoInsumo controllerTipoInsumo = new ControllerTipoInsumo();
                    tipoInsumos = controllerTipoInsumo.obterTipoInsumo(tipoInsumosIds);
                    
                    TipoProduto tipoProduto = new TipoProduto(
                        resultado.getInt("id"),
                        resultado.getString("nome"),
                        resultado.getString("instrucoes"),
                        resultado.getFloat("valor_base"),
                        resultado.getBoolean("habilitado"),
                        resultado.getTimestamp("created_at"),
                        resultado.getTimestamp("updated_at"),
                        tipoInsumos
                    );
                    tipoProdutos.add(tipoProduto);
                }
            } else {
                System.out.println("Erro: conexão com o banco de dados não foi estabelecida.");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao obter o tipo de insumo: " + e.getMessage());
        } finally {
            Conexao.desconectar();
        }
        return tipoProdutos;
    }
}
