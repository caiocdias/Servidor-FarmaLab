package controller;

import java.rmi.Remote;
import java.rmi.RemoteException;
import model.Prescricao;

public interface InterfacePrescricao extends Remote {

    void inserirPrescricao(Prescricao prescricao) throws RemoteException;

    void atualizarPrescricao(Prescricao prescricao) throws RemoteException;

    void desativarPrescricao(int id) throws RemoteException;

    Prescricao obterPrescricao(Integer id, String crm) throws RemoteException;
}
