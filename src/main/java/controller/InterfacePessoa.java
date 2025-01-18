/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import java.rmi.Remote;
import java.rmi.RemoteException;
import model.Pessoa;
/**
 *
 * @author Caio Cezar Dias
 */
public interface InterfacePessoa extends Remote {
    public void inserirPessoa(Pessoa p) throws RemoteException;
    public Pessoa getPessoa(Pessoa p) throws RemoteException;
}
