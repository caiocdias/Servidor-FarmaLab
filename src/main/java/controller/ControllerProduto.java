package controller;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Produto;
import util.Conexao;;

/**
 *
 * @author Thaíssa
 */

public class ControllerProduto extends UnicastRemoteObject implements InterfaceProduto {

    public ControllerProduto() throws RemoteException {
        super();
    }

    @Override
    public void inserirProduto(Produto produto) throws RemoteException {
        try {
            Conexao.conectar();
            Connection conexao = Conexao.con;

            if (conexao != null) {
                String sql = "INSERT INTO tipo de produto (id_pedido, id_tipo_produto, id_estoque, data_validade, pronta_entrega, habilitado) VALUES (?, ?, ?, ?, ?, ?, ?)";
                
                PreparedStatement stmt = conexao.prepareStatement(sql);
                
                stmt.setInt(1, produto.getPedido().getId());
                stmt.setInt(2, produto.getTipo_produto().getId());
                stmt.setInt(3, produto.getEstoque().getId());
                stmt.setTimestamp(4, produto.getData_validade());
                stmt.setBoolean(5, produto.isPronta_entrega());
                stmt.setBoolean(6, produto.isColetado());
                stmt.setBoolean(7, produto.isHabilitado());
                
                
                stmt.executeUpdate();
                System.out.println("Produto inseridao com sucesso!");
            } else {        
                System.out.println("Erro: conexão com o banco de dados não foi estabelecida.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RemoteException("Erro ao inserir Produto: " + e.getMessage());
        } finally {
            Conexao.desconectar();
        }
    }

    @Override
    public void atualizarProduto(Produto produto) throws RemoteException {
        try {
            Conexao.conectar();
            Connection conexao = Conexao.con;

            if (conexao != null) {
                String sql = "UPDATE tipo de produto SET id_pedido = ?, id_tipo_produto = ?, id_estoque = ?, data_validade = ?, pronta_entrega = ?, habilitado = ?, updated_at = CURRENT_TIMESTAMP WHERE id = ?";
                PreparedStatement stmt = conexao.prepareStatement(sql);

                stmt.setInt(1, produto.getPedido().getId());
                stmt.setInt(2, produto.getTipo_produto().getId());
                stmt.setInt(3, produto.getEstoque().getId());
                stmt.setTimestamp(4, produto.getData_validade());
                stmt.setBoolean(5, produto.isPronta_entrega());
                stmt.setBoolean(6, produto.isColetado());
                stmt.setBoolean(7, produto.isHabilitado());
                stmt.setInt(8, produto.getId());

                int linhasAfetadas = stmt.executeUpdate();

                if (linhasAfetadas > 0) {
                    System.out.println("Produto atualizado com sucesso!");
                } else {
                    System.out.println("Nenhum produto encontrado com o ID fornecido.");
                }
            } else {
                System.out.println("Erro: conexão com o banco de dados não foi estabelecida.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RemoteException("Erro ao atualizar produto: " + e.getMessage());
        } finally {
            Conexao.desconectar();
        }
    }

    @Override
    public void desativarProduto(int id) throws RemoteException {
        try {
            Conexao.conectar();
            Connection conexao = Conexao.con;

            if (conexao != null) {
                String sql = "DELETE FROM produto WHERE id = ?";
                PreparedStatement stmt = conexao.prepareStatement(sql);
                stmt.setInt(1, id);

                int linhasAfetadas = stmt.executeUpdate();

                if (linhasAfetadas > 0) {
                    System.out.println("Insumo produto com sucesso!");
                } else {
                    System.out.println("Nenhum produto encontrado com o ID fornecido.");
                }
            } else {
                System.out.println("Erro: conexão com o banco de dados não foi estabelecida.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RemoteException("Erro ao remover produto: " + e.getMessage());
        } finally {
            Conexao.desconectar();
        }
    }

    @Override
    public Produto obterProduto(Integer id) throws RemoteException {
        Produto produto = null;
        try {
            Conexao.conectar();
            Connection conexao = Conexao.con;

            if (conexao != null) {
                String sql = "SELECT * FROM produto WHERE id = ?";
                PreparedStatement sentenca = conexao.prepareStatement(sql);
                sentenca.setInt(1, id);
                ResultSet resultado = sentenca.executeQuery();

                if (resultado.next()) {
                    ControllerPedido controllerPedido = new ControllerPedido();
                    ControllerTipoProduto controllerTipoProduto = new ControllerTipoProduto();
                    ControllerEstoque controllerEstoque = new ControllerEstoque();
                    produto.setData_validade(resultado.getTimestamp("data_validade"));
                    produto.setPronta_entrega(resultado.getBoolean("pronta_entrega"));
                    produto.setColetado(resultado.getBoolean("coletado"));
                    produto.setHabilitado(resultado.getBoolean("habilitado"));
                    
                    produto.setPedido(controllerPedido.obterPedido(resultado.getInt("pedido_id")));
                    produto.setTipo_produto(controllerTipoProduto.obterTipoProduto(resultado.getInt("pedido_id")));
                    produto.setEstoque(controllerEstoque.obterEstoque(resultado.getInt("pedido_id")));
                    
                    return produto;
                } else {
                    System.out.println("Produto com ID " + id + " não encontrado.");
                }
            } else {
                System.out.println("Erro: conexão com o banco de dados não foi estabelecida.");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao obter o produto: " + e.getMessage());
        } finally {
            Conexao.desconectar();
        }
        return produto;
    }

    @Override
    public List<Produto> buscarProdutoPorNome(String nome) throws RemoteException {
        List<Produto> produtos = new ArrayList<>();
        try {
            Conexao.conectar();
            Connection conexao = Conexao.con;

            if (conexao != null) {
                String sql = "SELECT * FROM produto WHERE nome LIKE ?";
                PreparedStatement stmt = conexao.prepareStatement(sql);
                stmt.setString(1, "%" + nome + "%");

                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    ControllerPedido controllerPedido = new ControllerPedido();
                    ControllerEstoque controllerEstoque = new ControllerEstoque();
                    ControllerTipoProduto controllerTipoProduto = new ControllerTipoProduto();
                    Produto produto = new Produto();

                    produto.setId(rs.getInt("id"));
                    produto.setColetado(rs.getBoolean("coletado"));
                    produto.setPronta_entrega(rs.getBoolean("pronta_entrega"));
                    produto.setHabilitado(rs.getBoolean("habilitado"));
                    produto.setData_validade(rs.getTimestamp("data_validade"));
                    produto.setCreated_at(rs.getTimestamp("created_at"));
                    produto.setUpdated_at(rs.getTimestamp("updated_at"));

                    produto.setPedido(controllerPedido.obterPedido(rs.getInt("pedido_id")));
                    produto.setTipo_produto(controllerTipoProduto.obterTipoProduto(rs.getInt("tipo_produto_id")));     
                    produto.setEstoque(controllerEstoque.obterEstoque(rs.getInt("estoque_id")));   
                    
                    produtos.add(produto);
                }
            } else {
                System.out.println("Erro: conexão com o banco de dados não foi estabelecida.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RemoteException("Erro ao buscar produto por nome: " + e.getMessage());
        } finally {
            Conexao.desconectar();
        }
        return produtos;
    }

}
