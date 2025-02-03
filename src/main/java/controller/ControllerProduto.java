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
                String sql = "INSERT INTO produto (id_pedido_venda, id_pedido_producao, id_tipo_produto, id_estoque, data_validade, pronta_entrega, coletado, habilitado) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                
                PreparedStatement stmt = conexao.prepareStatement(sql);
                
                stmt.setInt(1, produto.getPedido_venda().getId());
                stmt.setInt(2, produto.getPedido_producao().getId());
                stmt.setInt(3, produto.getTipo_produto().getId());
                stmt.setInt(4, produto.getEstoque().getId());
                stmt.setTimestamp(5, produto.getData_validade());
                stmt.setBoolean(6, produto.isPronta_entrega());
                stmt.setBoolean(7, produto.isColetado());
                stmt.setBoolean(8, produto.isHabilitado());
                
                
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
                String sql = "UPDATE tipo de produto SET id_pedido_venda = ?, id_pedido_producao = ?, id_tipo_produto = ?, id_estoque = ?, data_validade = ?, pronta_entrega = ?, habilitado = ?, updated_at = CURRENT_TIMESTAMP WHERE id = ?";
                PreparedStatement stmt = conexao.prepareStatement(sql);

                stmt.setInt(1, produto.getPedido_venda().getId());
                stmt.setInt(2, produto.getPedido_producao().getId());
                stmt.setInt(3, produto.getTipo_produto().getId());
                stmt.setInt(4, produto.getEstoque().getId());
                stmt.setTimestamp(5, produto.getData_validade());
                stmt.setBoolean(6, produto.isPronta_entrega());
                stmt.setBoolean(7, produto.isColetado());
                stmt.setBoolean(8, produto.isHabilitado());
                stmt.setInt(9, produto.getId());

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
                    
                    produto.setPedido_venda(controllerPedido.obterPedido(resultado.getInt("id_pedido_venda")));
                    produto.setPedido_producao(controllerPedido.obterPedido(resultado.getInt("id_pedido_producao")));
                    produto.setTipo_produto(controllerTipoProduto.obterTipoProduto(resultado.getInt("id_tipo_produto")));
                    produto.setEstoque(controllerEstoque.obterEstoque(resultado.getInt("id_estoque")));
                    
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
    public Produto produtoDisponivel(int idTipoProduto) throws RemoteException {
        Produto produto = null;
        try{
            Conexao.conectar();
            Connection conexao = Conexao.con;
            
            if (conexao != null) {
                String sql = "SELECT * FROM produto WHERE pronta_entrega = 1 AND habilitado = 1 AND id_tipo_produto = ? AND id_pedido_venda is null LIMIT 1";
                PreparedStatement stmt = conexao.prepareStatement(sql);
                stmt.setInt(1, idTipoProduto);
                ResultSet rs = stmt.executeQuery();
                
                ControllerPedido controllerPedido = new ControllerPedido();
                ControllerTipoProduto controllerTipoProduto = new ControllerTipoProduto();
                ControllerEstoque controllerEstoque = new ControllerEstoque();
                if (rs.next()) {
                    produto.setData_validade(rs.getTimestamp("data_validade"));
                    produto.setPronta_entrega(rs.getBoolean("pronta_entrega"));
                    produto.setColetado(rs.getBoolean("coletado"));
                    produto.setHabilitado(rs.getBoolean("habilitado"));
                    
                    produto.setPedido_venda(controllerPedido.obterPedido(rs.getInt("id_pedido_venda")));
                    produto.setPedido_producao(controllerPedido.obterPedido(rs.getInt("id_pedido_producao")));
                    produto.setTipo_produto(controllerTipoProduto.obterTipoProduto(rs.getInt("id_tipo_produto")));
                    produto.setEstoque(controllerEstoque.obterEstoque(rs.getInt("id_estoque")));
                } else {
                    System.out.println("Produto não disponivel em estoque.");
                }
            }else{
                System.out.println("Erro: conexão com o banco de dados não foi estabelecida.");
            }
            
        } catch (Exception e){
            e.printStackTrace();
            throw new RemoteException("Erro ao buscar produto por nome: " + e.getMessage());
        }
        
        return produto;
    }

}
