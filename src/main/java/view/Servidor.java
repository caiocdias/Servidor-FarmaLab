/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;
import controller.PessoaController;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 *
 * @author Caio Cezar Dias
 */
public class Servidor {
    
    public static void main(String[] args) throws RemoteException {
        try{
            Registry reg = LocateRegistry.createRegistry(1022);
            reg.rebind("server",new PessoaController());
            System.out.println("Server is running");
        }catch(Exception e){
            System.out.println(e);
        }
    }
}
