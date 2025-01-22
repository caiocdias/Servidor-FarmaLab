package controller;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import model.MedicoParceiro;

public interface InterfaceMedicoParceiro extends Remote {

    void inserirMedicoParceiro(MedicoParceiro medicoParceiro) throws RemoteException;

    void atualizarMedicoParceiro(MedicoParceiro medicoParceiro) throws RemoteException;

    void desativarMedicoParceiro(int id) throws RemoteException;

    MedicoParceiro obterMedicoParceiro(Integer id, String cpf) throws RemoteException;

    List<MedicoParceiro> buscarMedicosParceirosPorNome(String nome) throws RemoteException;
}
