/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import controller.ControllerCliente;

/**
 *
 * @author Caio Cezar Dias
 */
public class Servidor {
    public static void main(String[] args) {
        try {
            // Define o nome do serviço RMI
            String nomeServico = "ClienteService";

            // Cria uma instância do controlador (implementação da interface)
            ControllerCliente clienteService = new ControllerCliente();

            // Cria o registro RMI na porta padrão 1099
            Registry registro = LocateRegistry.createRegistry(1099);

            // Registra o serviço com o nome especificado
            registro.rebind(nomeServico, clienteService);

            System.out.println("Servidor RMI iniciado e serviço '" + nomeServico + "' registrado.");
        } catch (Exception e) {
            System.err.println("Erro ao iniciar o servidor RMI: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
