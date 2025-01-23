/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package controller;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import model.Estoque;

/**
 *
 * @author Rafae
 */
public interface InterfaceEstoque extends Remote{
    void inserirEstoque(Estoque estoque) throws RemoteException;

    void atualizarEstoque(Estoque unidade) throws RemoteException;

    void desativarEstoque(int id) throws RemoteException;

    Estoque obterEstoque(int id) throws RemoteException;

    List<Estoque> buscarEstoquePorNome(String nome) throws RemoteException;
}
