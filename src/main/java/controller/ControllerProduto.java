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
                String sql = "INSERT INTO tipo de produto (nome, data_validade, created_at, updated_at) VALUES (?, ?. ?, ?)";
                PreparedStatement stmt = conexao.prepareStatement(sql);

                stmt.setString(1, produto.getNome());
                stmt.setTimestamp(2, produto.getData_validade());
                stmt.setTimestamp(3, produto.getCreated_at());
                stmt.setTimestamp(4, produto.getUpdated_at());
                
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
                String sql = "UPDATE tipo de produto SET nome = ?, data_validade = ?, updated_at = CURRENT_TIMESTAMP WHERE id = ?";
                PreparedStatement stmt = conexao.prepareStatement(sql);

                stmt.setString(1, produto.getNome());
                stmt.setInt(2, produto.getId());
                stmt.setTimestamp(3, produto.getData_validade());

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
                    produto.setId(resultado.getInt("id"));
                    produto.setNome(resultado.getString("nome"));

                    return produto;
                } else {
                    System.out.println("Produto com ID " + id + " não encontrado.");
                }
            } else {
                System.out.println("Erro: conexão com o banco de dados não foi estabelecida.");
            }
        } catch (SQLException /* | NotBoundException | MalformedURLException */ e) {
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
                    produto.setNome(rs.getString("nome"));
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
