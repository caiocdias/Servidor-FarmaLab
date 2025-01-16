/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import controller.InterfacePessoa;
import controller.PessoaController;
import java.rmi.RemoteException;

/**
 *
 * @author Caio Cezar Dias
 */
public class Servidor {
    public static void main(String[] args) throws RemoteException {
        InterfacePessoa pessoa = new PessoaController();
        conexao.bind(pessoa, "pessoa");
    }
}
