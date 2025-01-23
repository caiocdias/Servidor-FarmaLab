package controller;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import model.Produto;

/**
 *
 * @author Thaíssa
 */

 public interface InterfaceProduto extends Remote {
    void inserirProduto(Produto produto) throws RemoteException;

    void atualizarProduto(Produto produto) throws RemoteException;

    public void desativarProduto(int id) throws RemoteException;
    
    Produto obterProduto(Integer id) throws RemoteException;
    
    List<Produto> buscarProdutoPorNome(String nome) throws RemoteException;
    
}
