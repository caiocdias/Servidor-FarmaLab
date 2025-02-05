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
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import model.Produto;
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
    public Venda inserirVenda(Venda venda) throws RemoteException {
        int id = 0;
        try{
            Conexao.conectar();
            Connection conexao = Conexao.con;

            if (conexao != null) {
                String sqlVenda = "INSERT INTO venda (id_unidade, id_pedido) VALUES (?, ?)";
                venda.getPedido().setStatus(StatusPedido.PRONTO_PARA_PRODUCAO);
                PreparedStatement stmtVenda = conexao.prepareStatement(sqlVenda, Statement.RETURN_GENERATED_KEYS);
                stmtVenda.setInt(1, venda.getUnidade().getId());
                stmtVenda.setInt(2, venda.getPedido().getId());
                stmtVenda.executeUpdate();
                
                ControllerPedido controllerPedido = new ControllerPedido();
                controllerPedido.atualizarPedido(venda.getPedido());
                
                ResultSet rs = stmtVenda.getGeneratedKeys();
                if (rs.next()) {
                    venda.setId(rs.getInt(1));
                }
                Timestamp timestamp = Timestamp.from(Instant.now());
                venda.setCreated_at(timestamp);
                
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
        return venda;
    }

    @Override
    public void atualizarVenda(Venda venda) throws RemoteException {
        try {
            Conexao.conectar();
            Connection conexao = Conexao.con;

            if (conexao != null) {
                String sqlVenda = "UPDATE venda SET id_unidade = ?, id_pedido = ?, updated_at = CURRENT_TIMESTAMP WHERE id = ?";
                PreparedStatement stmtVenda = conexao.prepareStatement(sqlVenda, Statement.RETURN_GENERATED_KEYS);
                    stmtVenda.setInt(1, venda.getUnidade().getId());
                    stmtVenda.setInt(2, venda.getPedido().getId());
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
            ControllerPedido controllerPedido = new ControllerPedido(); 

            if (conexao != null) {
                String sql = "SELECT id, id_unidade, id_pedido, habilitado, created_at, updated_at FROM venda WHERE id = ?";
                
                PreparedStatement stmt = conexao.prepareStatement(sql);
                stmt.setInt(1, id);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    venda = new Venda(
                            rs.getInt("id"),
                            controllerUnidade.obterUnidade(rs.getInt("id_unidade")),
                            controllerPedido.obterPedido(rs.getInt("id_pedido")),
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
            ControllerPedido controllerPedido = new ControllerPedido(); 

            if (conexao != null) {
                String sql = "SELECT v.*"
                        + "FROM venda v INNER JOIN pedido p ON p.id = v.id_pedido WHERE 1=1 AND v.habilitado = 1";
                if (cliente_id != null) {
                    sql += " AND p.id_cliente = ?";
                }
                if (funcionario_id != null) {
                    sql += " AND p.id_funcionario = ?";
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
                            controllerUnidade.obterUnidade(rs.getInt("id_unidade")),
                            controllerPedido.obterPedido(rs.getInt("id_pedido")),
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

    @Override
    public String imprimirNotaFiscal(Venda venda) throws RemoteException {
        String produtos = new String();
        for(Produto produto : venda.getPedido().getProdutos()){
            produtos += produto.getTipo_produto().getNome() + " .......... " + produto.getTipo_produto().getValor_base() + "\n";
            
        }
        
        String nf = "NOTA FISCAL\n\n"
                + "Nº: "+venda.getId()+"\n"
                + "Data de Emissão: "+venda.getCreated_at()+ "\n\n"
                + "DADOS DO CLIENTE:\n"
                + "Nome: "+venda.getPedido().getCliente().getNome()+"\n"
                + "CPF:"+venda.getPedido().getCliente().getCpf()+"\n\n"
                + "PRODUTOS:\n"
                + produtos
                + "__________________\n\n"
                + "   Valor Total: "+venda.getPedido().getValorTotalBase()+"\n"
                + " + Tributos: "+venda.getPedido().getTributoTotal()+"\n"
                + " - Descontos Totais: "+venda.getPedido().getDescontoTotal()+"\n"
                + "__________________\n\n"
                + "   Valor Final: "+venda.getPedido().getValorFinal();
        
        return nf;
    }
    
}
