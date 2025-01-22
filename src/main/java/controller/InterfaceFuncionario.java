package controller;

import java.rmi.Remote;
import java.rmi.RemoteException;
import model.Funcionario;

public interface InterfaceFuncionario extends Remote {

    void inserirFuncionario(Funcionario funcionario) throws RemoteException;

    void atualizarFuncionario(Funcionario funcionario) throws RemoteException;

    public void desativarFuncionario(int id) throws RemoteException;
    
    Funcionario obterFuncionario(Integer id, String cpf) throws RemoteException;
}
