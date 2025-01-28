/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package controller;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import model.Venda;

/**
 *
 * @author Rafae
 */
public interface InterfaceVenda extends Remote{
    void inserirVenda(Venda venda) throws RemoteException;

    void atualizarVenda(Venda venda) throws RemoteException;

    void desativarVenda(int id) throws RemoteException;

    Venda obterVenda(int id) throws RemoteException;

    List<Venda> buscarVendaPorClienteOuFuncionario(Integer cliente_id, Integer funcionario_id) throws RemoteException;
    
    String imprimirNotaFiscal(Venda venda) throws RemoteException;
}
