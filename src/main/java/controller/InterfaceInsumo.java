package controller;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import model.Insumo;

/**
 *
 * @author Thaíssa
 */

 public interface InterfaceInsumo extends Remote {
    void inserirInsumo(Insumo insumo) throws RemoteException;

    void atualizarInsumo(Insumo insumo) throws RemoteException;

    public void desativarInsumo(int id) throws RemoteException;
    
    Insumo obterInsumo(Integer id) throws RemoteException;
    
    List<Insumo> buscarInsumosPorNome(String nome) throws RemoteException;
    
}
