/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package controller;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import model.Orcamento;

/**
 *
 * @author Isabely
 */

public interface InterfaceOrcamento extends Remote{
    void inserirOrcamento(Orcamento orcamento) throws RemoteException;
    Orcamento obterOrcamento(int id) throws RemoteException;
    void atualizarOrcamento(Orcamento orcamento) throws RemoteException;
    void desativarOrcamento(int id) throws RemoteException;
}
