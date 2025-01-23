/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package controller;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import model.Tributo;

/**
 *
 * @author Rafae
 */
public interface InterfaceTributo extends Remote{
    void inserirTributo(Tributo tributo) throws RemoteException;

    void atualizarTributo(Tributo tributo) throws RemoteException;

    void desativarTributo(int id) throws RemoteException;

    Tributo obterTributo(int id) throws RemoteException;

    List<Tributo> buscarTributoPorNome(String nome) throws RemoteException;
}
