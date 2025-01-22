package controller;

import java.rmi.Remote;
import java.rmi.RemoteException;
import model.Prescricao;

public interface InterfacePrescricao extends Remote {

    void inserirPrescricao(Prescricao prescricao) throws RemoteException;
}
