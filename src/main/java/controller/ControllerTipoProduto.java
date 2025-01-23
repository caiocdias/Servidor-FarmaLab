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
                String sql = "INSERT INTO tipo_produto (nome, instrucoes, habilitado, created_at, updated_at) VALUES (?, ?. ?, ?, ?)";
                PreparedStatement stmt = conexao.prepareStatement(sql);

                stmt.setString(1, tipoProduto.getNome());
                stmt.setString(2, tipoProduto.getInstrucoes());
                stmt.setBoolean(3, tipoProduto.isHabilitado());
                stmt.setTimestamp(4, tipoProduto.getUpdated_at());
                stmt.setTimestamp(5, tipoProduto.getCreated_at());

                stmt.executeUpdate();
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
                String sql = "UPDATE tipo_produto SET nome = ?, instrucoes = ?, updated_at = CURRENT_TIMESTAMP WHERE id = ?";
                PreparedStatement stmt = conexao.prepareStatement(sql);

                stmt.setString(1, tipoProduto.getNome());
                stmt.setString(2, tipoProduto.getInstrucoes());
                stmt.setInt(3, tipoProduto.getId());

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
    public TipoProduto obterTipoProduto(Integer id) throws RemoteException {
        TipoProduto tipoProduto = null;
        try {
            Conexao.conectar();
            Connection conexao = Conexao.con;

            if (conexao != null) {
                String sql = "SELECT * FROM tipo_produto WHERE id = ?";
                PreparedStatement sentenca = conexao.prepareStatement(sql);
                sentenca.setInt(1, id);
                ResultSet resultado = sentenca.executeQuery();

                if (resultado.next()) {

                    tipoProduto.setId(resultado.getInt("id"));
                    tipoProduto.setNome(resultado.getString("nome"));
                    tipoProduto.setInstrucoes(resultado.getString("instrucoes"));
                    tipoProduto.setHabilitado(resultado.getBoolean("habilitado"));

                    return tipoProduto;
                } else {
                    System.out.println("Tipo de produto com ID " + id + " não encontrado.");
                }
            } else {
                System.out.println("Erro: conexão com o banco de dados não foi estabelecida.");
            }
        } catch (SQLException /* | NotBoundException | MalformedURLException */ e) {
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

                while (rs.next()) {
                    TipoProduto tipoProduto = new TipoProduto();
                    ControllerTipoInsumo controllerTipoInsumo = new ControllerTipoInsumo();
                    List<TipoInsumo> tipo_produtos = new ArrayList<>();

                    tipo_produtos.add(controllerTipoInsumo.obterTipoInsumo(rs.getInt("tipo_insumo_id")));

                    tipoProduto.setId(rs.getInt("id"));
                    tipoProduto.setNome(rs.getString("nome"));
                    tipoProduto.setInstrucoes(rs.getString("instrucoes"));
                    tipoProduto.setHabilitado(rs.getBoolean("habilitado"));
                    tipoProduto.setCreated_at(rs.getTimestamp("created_at"));
                    tipoProduto.setUpdated_at(rs.getTimestamp("updated_at"));
                    tipoProduto.setTipo_insumos(tipo_produtos);
                    
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
}
