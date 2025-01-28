package view;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import controller.ControllerCliente;
import controller.ControllerFuncionario;
import controller.ControllerMedicoParceiro;
import controller.ControllerNotaFiscal;
import controller.ControllerOrcamento;
import controller.ControllerPrescricao;
import controller.InterfaceCliente;
import controller.InterfaceFuncionario;
import controller.InterfaceMedicoParceiro;
import controller.InterfaceNotaFiscal;
import controller.InterfaceOrcamento;
import controller.InterfacePrescricao;

public class Server {

    public static void main(String[] args) {
        try {
            LocateRegistry.createRegistry(1099);

            InterfaceCliente clienteStub = new ControllerCliente();
            Naming.rebind("//localhost/Cliente", clienteStub);
            System.out.println("Serviço de Cliente registrado com sucesso!");

            InterfaceFuncionario funcionarioStub = new ControllerFuncionario();
            Naming.rebind("//localhost/Funcionario", funcionarioStub);
            System.out.println("Serviço de Funcionario registrado com sucesso!");

            InterfaceMedicoParceiro medicoParceiroStub = new ControllerMedicoParceiro();
            Naming.rebind("//localhost/MedicoParceiro", medicoParceiroStub);
            System.out.println("Serviço de MedicoParceiro registrado com sucesso!");

            InterfacePrescricao prescricaoStub = new ControllerPrescricao();
            Naming.rebind("//localhost/Prescricao", prescricaoStub);
            System.out.println("Serviço de Prescricao registrado com sucesso!");
            
            InterfaceNotaFiscal notaFiscalStub = new ControllerNotaFiscal();
            Naming.rebind("//localhost/NotaFiscal", notaFiscalStub);
            System.out.println("Serviço de Nota Fiscal registrado com sucesso!");
            
            InterfaceOrcamento OrcamentoStub = new ControllerOrcamento();
            Naming.rebind("//localhost/Orcamento", OrcamentoStub);
            System.out.println("Serviço de Orcamento registrado com sucesso!");

            System.out.println("Servidor RMI pronto e aguardando conexões...");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro ao iniciar o servidor RMI: " + e.getMessage());
        }
    }
}

