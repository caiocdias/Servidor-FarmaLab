/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package controller;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import model.Pedido;
import model.enums.StatusPedido;

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
    Pedido calcularValorFinal(Pedido pedido) throws RemoteException;
    float calcularTributo(Pedido pedido) throws RemoteException;
    public List<Pedido> obterPedido(List<Integer> ids) throws RemoteException;
    List<Pedido> buscarPedidosStatus(StatusPedido status) throws RemoteException;
    void retirarPedido(Pedido pedido) throws RemoteException;
}
