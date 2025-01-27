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
import java.util.List;
import model.Venda;
import model.enums.StatusPedido;
import util.Conexao;

/**
 *
 * @author Rafae
 */
public class ControllerVenda extends UnicastRemoteObject implements InterfaceVenda{

    public ControllerVenda() throws RemoteException {
        super();
    }
    
    @Override
    public void inserirVenda(Venda venda) throws RemoteException {
        try{
                Conexao.conectar();
                Connection conexao = Conexao.con;

                if (conexao != null) {
                    String sqlVenda = "INSERT INTO venda (unidade_id, pedido_id, nota_fiscal_id) VALUES (?, ?, ?)";
                    PreparedStatement stmtVenda = conexao.prepareStatement(sqlVenda, Statement.RETURN_GENERATED_KEYS);
                    stmtVenda.setInt(1, venda.getUnidade().getId());
                    stmtVenda.setInt(2, venda.getPedido().getId());
                    stmtVenda.setInt(3, venda.getNota_fiscal().getId());
                    stmtVenda.executeUpdate();
                    venda.getPedido().setStatus(StatusPedido.PRONTO_PARA_PRODUCAO);
                    ControllerPedido controllerPedido = new ControllerPedido();
                    controllerPedido.atualizarPedido(venda.getPedido());
                    System.out.println("Venda inserida com sucesso!");
                } else {
                    System.out.println("Erro: conexão com o banco de dados não foi estabelecida.");
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new RemoteException("Erro ao inserir a venda: " + e.getMessage());
            } finally {
                Conexao.desconectar();
            }
    }

    @Override
    public void atualizarVenda(Venda venda) throws RemoteException {
        try {
            Conexao.conectar();
            Connection conexao = Conexao.con;

            if (conexao != null) {
                String sqlVenda = "UPDATE venda SET unidade_id = ?, pedido_id = ?, nota_fiscal_id = ?, updated_at = CURRENT_TIMESTAMP WHERE id = ?";
                PreparedStatement stmtVenda = conexao.prepareStatement(sqlVenda, Statement.RETURN_GENERATED_KEYS);
                    stmtVenda.setInt(1, venda.getUnidade().getId());
                    stmtVenda.setInt(2, venda.getPedido().getId());
                    stmtVenda.setInt(3, venda.getNota_fiscal().getId());
                    stmtVenda.setInt(4, venda.getId());
                    stmtVenda.executeUpdate();

                System.out.println("Venda atualizada com sucesso!");
            } else {
                System.out.println("Erro: conexão com o banco de dados não foi estabelecida.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RemoteException("Erro ao atualizar a venda: " + e.getMessage());
        } finally {
            Conexao.desconectar();
        }
    }

    @Override
    public void desativarVenda(int id) throws RemoteException {
        try {
            Conexao.conectar();
            Connection conexao = Conexao.con;

            if (conexao != null) {
                String sqlVenda = "UPDATE venda SET habilitado = false, updated_at = CURRENT_TIMESTAMP WHERE id = ?";
                PreparedStatement stmt = conexao.prepareStatement(sqlVenda);
                stmt.setInt(1, id);
                int linhasAfetadas = stmt.executeUpdate();

                if (linhasAfetadas > 0) {
                    System.out.println("Venda desativada com sucesso!");
                } else {
                    System.out.println("Nenhuma venda encontrada com o ID fornecido.");
                }
            } else {
                System.out.println("Erro: conexão com o banco de dados não foi estabelecida.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RemoteException("Erro ao desativar a venda: " + e.getMessage());
        } finally {
            Conexao.desconectar();
        }
    }

    @Override
    public Venda obterVenda(int id) throws RemoteException {
        Venda venda = null;
        try {
            Conexao.conectar();
            Connection conexao = Conexao.con;
            ControllerUnidade controllerUnidade = new ControllerUnidade();
            ControllerNotaFiscal controllerNotaFiscal = new ControllerNotaFiscal();
            ControllerPedido controllerPedido = new ControllerPedido(); 

            if (conexao != null) {
                String sql = "SELECT id, unidade_id, nota_fiscal_id, pedido_id, habilitado, created_at, updated_at FROM venda WHERE id = ?";
                
                PreparedStatement stmt = conexao.prepareStatement(sql);
                stmt.setInt(1, id);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    venda = new Venda(
                            rs.getInt("id"),
                            controllerUnidade.obterUnidade(rs.getInt("unidade_id")),
                            controllerNotaFiscal.obterNotaFiscal(rs.getInt("nota_fiscal_id")),
                            controllerPedido.obterPedido(rs.getInt("pedido_id")),
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
            throw new RemoteException("Erro ao pesquisar a venda: " + e.getMessage());
        } finally {
            Conexao.desconectar();
        }
        return venda;
    }

    @Override
    public List<Venda> buscarVendaPorClienteOuFuncionario(Integer cliente_id, Integer funcionario_id) throws RemoteException {
        List<Venda> vendas = null;
        try {
            Conexao.conectar();
            Connection conexao = Conexao.con;
            ControllerUnidade controllerUnidade = new ControllerUnidade();
            ControllerNotaFiscal controllerNotaFiscal = new ControllerNotaFiscal();
            ControllerPedido controllerPedido = new ControllerPedido(); 

            if (conexao != null) {
                String sql = "SELECT v.*"
                        + "FROM venda v INNER JOIN pedido p ON p.id = v.pedido_id WHERE 1=1";
                if (cliente_id != null) {
                    sql += " AND p.cliente_id = ?";
                }
                if (funcionario_id != null) {
                    sql += " AND p.funcionario_id = ?";
                }

                PreparedStatement stmt = conexao.prepareStatement(sql);

                int paramIndex = 1;
                if (cliente_id != null) {
                    stmt.setInt(paramIndex++, cliente_id);
                }
                if (funcionario_id != null) {
                    stmt.setInt(paramIndex++, funcionario_id);
                }

                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    Venda venda = new Venda(
                            rs.getInt("id"),
                            controllerUnidade.obterUnidade(rs.getInt("unidade_id")),
                            controllerNotaFiscal.obterNotaFiscal(rs.getInt("nota_fiscal_id")),
                            controllerPedido.obterPedido(rs.getInt("pedido_id")),
                            rs.getBoolean("habilitado"),
                            rs.getTimestamp("created_at"),
                            rs.getTimestamp("updated_at")
                    );
                    vendas.add(venda);
                }
            } else {
                System.out.println("Erro: conexão com o banco de dados não foi estabelecida.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RemoteException("Erro ao buscar venda(s): " + e.getMessage());
        } finally {
            Conexao.desconectar();
        }
        return vendas;
    }
    
}
