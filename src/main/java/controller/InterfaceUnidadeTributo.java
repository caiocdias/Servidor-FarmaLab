/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package controller;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author Isabely
 */
public interface InterfaceUnidadeTributo extends Remote{
    void inserirRelacionamento(int idUnidade, int idTributo) throws RemoteException;
    boolean verificarRelacionamento(int idUnidade, int idTributo) throws RemoteException;
}
