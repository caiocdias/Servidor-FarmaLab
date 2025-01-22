package controller;

import java.rmi.Remote;
import java.rmi.RemoteException;
import model.Cliente;

public interface InterfaceCliente extends Remote {
    void inserirCliente(String nome, String endereco, String cpf, String telefone, boolean habilitado) throws RemoteException;
    Cliente obterCliente(int id) throws RemoteException;
}

