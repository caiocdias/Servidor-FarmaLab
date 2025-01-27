package controller;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import model.Insumo;
import model.TipoInsumo;

/**
 *
 * @author Thaíssa
 */

 public interface InterfaceInsumo extends Remote {
    void inserirInsumo(Insumo insumo) throws RemoteException;

    void atualizarInsumo(Insumo insumo) throws RemoteException;

    public void desativarInsumo(int id) throws RemoteException;
    
    Insumo obterInsumo(int id) throws RemoteException;
    
    public int promocaoInsumo(List<Integer> idsTipoInsumos) throws RemoteException;
}
