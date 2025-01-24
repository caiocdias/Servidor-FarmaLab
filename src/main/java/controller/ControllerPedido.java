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
import java.util.ArrayList;
import java.util.List;
import model.Pedido;
import model.enums.StatusPedido;
import util.Conexao;

/**
 *
 * @author Isabely
 */
public class ControllerPedido extends UnicastRemoteObject implements InterfacePedido{

    public ControllerPedido() throws RemoteException {
        super();
    }
    
    @Override
    public void inserirPedido(Pedido pedido) throws RemoteException {
        try {
            Conexao.conectar();
            Connection conexao = Conexao.con;

            if (conexao != null) {
                String sql = "INSERT INTO pedido (id_cliente, id_funcionario, status, habilitado) VALUES (?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement sentenca = conexao.prepareStatement(sql);
                sentenca.setInt(1, pedido.getCliente().getId());
                sentenca.setInt(2, pedido.getFuncionario().getId());
                sentenca.setString(3, pedido.getStatus().name());
                sentenca.setBoolean(4, pedido.isHabilitado());
                sentenca.setBoolean(5, pedido.isPronta_entrega());
                sentenca.execute();
            } else {
                System.out.println("Erro: conexão com o banco de dados não foi estabelecida.");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao cadastrar o Pedido: " + e.getMessage());
        } finally {
            Conexao.desconectar();
        }
    }

    @Override
    public Pedido obterPedido(int id) throws RemoteException {
        Pedido pedido = null;
        try {
            Conexao.conectar();
            Connection conexao = Conexao.con;

            if (conexao != null) {
                String sql = "SELECT * FROM pedido WHERE id = ?";
                PreparedStatement sentenca = conexao.prepareStatement(sql);
                sentenca.setInt(1, id);
                ResultSet resultado = sentenca.executeQuery();

                if (resultado.next()) {
                    ControllerCliente controllerCliente = new ControllerCliente();
                    ControllerFuncionario controllerFuncionario = new ControllerFuncionario();
                    
                    pedido.setId(resultado.getInt("id"));
                    pedido.setPronta_entrega(resultado.getBoolean("pronta_entrega"));
                    pedido.setHabilitado(resultado.getBoolean("habilitado"));

                    String statusString = resultado.getString("status");
                    pedido.setStatus(StatusPedido.valueOf(statusString));

                    pedido.setCliente(controllerCliente.obterCliente(resultado.getInt("cliente_id"), null));
                    pedido.setFuncionario(controllerFuncionario.obterFuncionario(resultado.getInt("funcionario_id"), null));

                     return pedido;
                } else {
                    System.out.println("Pedido com ID " + id + " não encontrado.");
                }
            } else {
                System.out.println("Erro: conexão com o banco de dados não foi estabelecida.");
            }
        } catch (SQLException /*| NotBoundException | MalformedURLException*/ e) {
            System.out.println("Erro ao obter o Pedido: " + e.getMessage());
        } finally {
            Conexao.desconectar();
        }
        return pedido;
    }

    @Override
    public void atualizarPedido(Pedido pedido) throws RemoteException {
        try {
            Conexao.conectar();
            Connection conexao = Conexao.con;
            if (conexao != null) {
                String sql = "UPDATE pedido SET id_cliente = ?, id_funcionario = ?, status = ?, habilitado = ?, pronta_entrega = ?, updated_at = CURRENT_TIMESTAMP, WHERE id = ?";
                PreparedStatement sentenca = conexao.prepareStatement(sql);
                sentenca.setInt(1, pedido.getCliente().getId());
                sentenca.setInt(2, pedido.getFuncionario().getId());
                sentenca.setString(3, pedido.getStatus().name());
                sentenca.setBoolean(4, pedido.isHabilitado());
                sentenca.setBoolean(5, pedido.isPronta_entrega());
                sentenca.setInt(6, pedido.getId());
                sentenca.executeUpdate();

                System.out.println("Pedido atualizado com sucesso!");
            } else {
                System.out.println("Erro: conexão com o banco de dados não foi estabelecida.");
            }
        } catch (SQLException e) {
            throw new RemoteException("Erro ao atualizar pedido: " + e.getMessage());
        } finally {
            Conexao.desconectar();
        }
    }

    @Override
    public void desativarPedido(int id) throws RemoteException {
        try{
            Conexao.conectar();
            Connection conexao = Conexao.con;

            if (conexao != null) {
                String sql = "UPDATE pedido SET habilitado = false, updated_at = CURRENT_TIMESTAMP WHERE id = ?";
                PreparedStatement sentenca = conexao.prepareStatement(sql);
                sentenca.setInt(1, id);
                int linhasAfetadas = sentenca.executeUpdate();
                if (linhasAfetadas > 0) {
                    System.out.println("Pedido desabilitada com sucesso.");
                } else {
                    System.out.println("Nenhum pedido foi encontrado para o ID informado.");
                }
            } else {
                System.out.println("Erro: conexão com o banco de dados não foi estabelecida.");
            }
        }catch(SQLException e){
            throw new RemoteException("Erro ao remover pedido: " + e.getMessage());
        }
    }

    @Override
    public List<Pedido> buscarPedidosPorCliente(int clienteId) throws RemoteException {
        List<Pedido> pedidos = new ArrayList<>();
        try {
            Conexao.conectar();
            Connection conexao = Conexao.con;

            if (conexao != null) {
                String sql = "SELECT * FROM pedido WHERE id_cliente = ?";
                PreparedStatement sentenca = conexao.prepareStatement(sql);
                sentenca.setInt(1, clienteId);
                ResultSet resultado = sentenca.executeQuery();

                while (resultado.next()) {
                    Pedido pedido = new Pedido();
                    ControllerCliente controllerCliente = new ControllerCliente();
                    ControllerFuncionario controllerFuncionario = new ControllerFuncionario();

                    pedido.setId(resultado.getInt("id"));
                    pedido.setPronta_entrega(resultado.getBoolean("pronta_entrega"));
                    pedido.setHabilitado(resultado.getBoolean("habilitado"));
                    pedido.setStatus(StatusPedido.valueOf(resultado.getString("status")));

                    pedido.setCliente(controllerCliente.obterCliente(resultado.getInt("id_cliente"), null));
                    pedido.setFuncionario(controllerFuncionario.obterFuncionario(resultado.getInt("id_funcionario"), null));

                    pedidos.add(pedido);
                }
            } else {
                System.out.println("Erro: conexão com o banco de dados não foi estabelecida.");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao buscar pedidos por cliente: " + e.getMessage());
        } finally {
            Conexao.desconectar();
        }

    return pedidos;
    }

    @Override
    public double calcularDescontoInsumo(Pedido pedido) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public double calcularDescontoMedico(Pedido pedido) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
