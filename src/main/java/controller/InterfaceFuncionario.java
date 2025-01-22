package controller;

import java.rmi.Remote;
import java.rmi.RemoteException;
import model.Funcionario;

public interface InterfaceFuncionario extends Remote {

    void inserirFuncionario(Funcionario funcionario) throws RemoteException;
}
