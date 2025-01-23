package controller;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import model.TipoInsumo;

/**
 *
 * @author Thaíssa
 */

public interface InterfaceTipoInsumo extends Remote {
    void inserirTipoInsumo(TipoInsumo tipoInsumo) throws RemoteException;

    void atualizarTipoInsumo(TipoInsumo tipoInsumo) throws RemoteException;

    void desativarTipoInsumo(int id) throws RemoteException;

    TipoInsumo obterTipoInsumo(Integer id) throws RemoteException;

    List<TipoInsumo> buscarTipoInsumoPorNome(String nome) throws RemoteException;
}
