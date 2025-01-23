package controller;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import model.TipoProduto;

/**
 *
 * @author Thaíssa
 */

public interface InterfaceTipoProduto extends Remote {
    void inserirTipoProduto(TipoProduto tipoProduto) throws RemoteException;

    void atualizarTipoProduto(TipoProduto tipoProduto) throws RemoteException;

    void desativarTipoProduto(int id) throws RemoteException;

    TipoProduto obterTipoProduto(Integer id) throws RemoteException;

    List<TipoProduto> buscarTipoProdutoPorNome(String nome) throws RemoteException;
}
