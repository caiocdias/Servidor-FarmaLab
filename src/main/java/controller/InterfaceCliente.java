package controller;

import java.rmi.Remote;
import java.rmi.RemoteException;
import model.Cliente;

public interface InterfaceCliente extends Remote {
    void inserirCliente(Cliente cliente) throws RemoteException;
    Cliente obterCliente(int id) throws RemoteException;
    void atualizarCliente(Cliente cliente) throws RemoteException;
    void desativarCliente(int id) throws RemoteException;
    Cliente pesquisarCliente(Integer id, String cpf) throws RemoteException;
}

