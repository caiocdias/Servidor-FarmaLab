/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package controller;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import model.Pedido;

/**
 *
 * @author Isabely
 */

public interface InterfacePedido extends Remote{
    void inserirPedido(Pedido p) throws RemoteException;
    Pedido obterPedido(int id) throws RemoteException;
    void atualizarPedido(Pedido p) throws RemoteException;
    void desativarPedido(int id) throws RemoteException;
    List<Pedido> buscarPedidoPorNome(int clienteId) throws RemoteException;
    double calcularDescontoInsumo(Pedido p) throws RemoteException;
    double calcularDescontoMedico(Pedido p) throws RemoteException;
}
