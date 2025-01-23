/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package controller;

import java.rmi.Remote;
import java.rmi.RemoteException;
import model.NotaFiscal;

/**
 *
 * @author Isabely
 */

public interface InterfaceNotaFiscal extends Remote{
    void inserirNotaFiscal(NotaFiscal nf) throws RemoteException;
    NotaFiscal obterNotaFiscal(int id) throws RemoteException;
    void atualizarNotaFiscal(NotaFiscal nf) throws RemoteException;
    void desativarNotaFiscal(int id) throws RemoteException;
}
