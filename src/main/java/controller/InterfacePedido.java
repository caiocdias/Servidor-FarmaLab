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
    void inserirPedido(Pedido pedido) throws RemoteException;
    Pedido obterPedido(int id) throws RemoteException;
    void atualizarPedido(Pedido pedido) throws RemoteException;
    void desativarPedido(int id) throws RemoteException;
    List<Pedido> buscarPedidosPorCliente(int clienteId) throws RemoteException;
    float calcularDescontoInsumo(Pedido pedido) throws RemoteException;
    float calcularDescontoMedico(Pedido pedido) throws RemoteException;
    float calcularValorFinal(Pedido pedido) throws RemoteException;
}
