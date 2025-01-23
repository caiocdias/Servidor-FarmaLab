/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import model.Pedido;
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
    public void inserirPedido(Pedido p) throws RemoteException {
        try {
            Conexao.conectar();
            Connection conexao = Conexao.con;

            if (conexao != null) {
                String sql = "INSERT INTO pedido (id_cliente, id_funcionario, status, habilitado) VALUES (?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement sentenca = conexao.prepareStatement(sql);
                sentenca.setInt(1, p.getCliente().getId());
                sentenca.setInt(2, p.getFuncionario().getId());
                sentenca.setString(3, p.getStatus_pedido().name());
                sentenca.setBoolean(4, p.isHabilitado());
                sentenca.setBoolean(5, p.isPronta_entrega());
                sentenca.execute();
            } else {
                System.out.println("Erro: conexão com o banco de dados não foi estabelecida.");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao cadastrar o Orçamento: " + e.getMessage());
        } finally {
            Conexao.desconectar();
        }
    }

    @Override
    public Pedido obterPedido(int id) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void atualizarPedido(Pedido p) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void desativarPedido(int id) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Pedido> buscarPedidoPorNome(int clienteId) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public double calcularDescontoInsumo(Pedido p) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public double calcularDescontoMedico(Pedido p) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
