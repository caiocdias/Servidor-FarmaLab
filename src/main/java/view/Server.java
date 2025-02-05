package view;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import controller.ControllerCliente;
import controller.ControllerEstoque;
import controller.ControllerFuncionario;
import controller.ControllerInsumo;
import controller.ControllerMedicoParceiro;
import controller.ControllerOrcamento;
import controller.ControllerPedido;
import controller.ControllerPrescricao;
import controller.ControllerProduto;
import controller.ControllerTipoInsumo;
import controller.ControllerTipoProduto;
import controller.ControllerTributo;
import controller.ControllerUnidade;
import controller.ControllerUnidadeTributo;
import controller.ControllerVenda;
import controller.InterfaceCliente;
import controller.InterfaceEstoque;
import controller.InterfaceFuncionario;
import controller.InterfaceInsumo;
import controller.InterfaceMedicoParceiro;
import controller.InterfaceOrcamento;
import controller.InterfacePedido;
import controller.InterfacePrescricao;
import controller.InterfaceProduto;
import controller.InterfaceTipoInsumo;
import controller.InterfaceTipoProduto;
import controller.InterfaceTributo;
import controller.InterfaceUnidade;
import controller.InterfaceUnidadeTributo;
import controller.InterfaceVenda;

public class Server {

    public static void main(String[] args) {
        try {
            LocateRegistry.createRegistry(1099);

            InterfaceCliente clienteStub = new ControllerCliente();
            Naming.rebind("//localhost/Cliente", clienteStub);
            System.out.println("Serviço de Cliente registrado com sucesso!");
            
            InterfaceEstoque estoqueStub = new ControllerEstoque();
            Naming.rebind("//localhost/Estoque", estoqueStub);
            System.out.println("Serviço de Estoque registrado com sucesso!");

            InterfaceFuncionario funcionarioStub = new ControllerFuncionario();
            Naming.rebind("//localhost/Funcionario", funcionarioStub);
            System.out.println("Serviço de Funcionario registrado com sucesso!");
            
            InterfaceInsumo insumoStub = new ControllerInsumo();
            Naming.rebind("//localhost/Insumo", insumoStub);
            System.out.println("Serviço de Insumo registrado com sucesso!");

            InterfaceMedicoParceiro medicoParceiroStub = new ControllerMedicoParceiro();
            Naming.rebind("//localhost/MedicoParceiro", medicoParceiroStub);
            System.out.println("Serviço de MedicoParceiro registrado com sucesso!");
            
            InterfaceOrcamento orcamentoStub = new ControllerOrcamento();
            Naming.rebind("//localhost/Orcamento", orcamentoStub);
            System.out.println("Serviço de Orcamento registrado com sucesso!");
            
            InterfacePedido pedidoStub = new ControllerPedido();
            Naming.rebind("//localhost/Pedido", pedidoStub);
            System.out.println("Serviço de Pedido registrado com sucesso!");
            
            InterfacePrescricao prescricaoStub = new ControllerPrescricao();
            Naming.rebind("//localhost/Prescricao", prescricaoStub);
            System.out.println("Serviço de Prescricao registrado com sucesso!");
            
            InterfaceProduto produtoStub = new ControllerProduto();
            Naming.rebind("//localhost/Produto", produtoStub);
            System.out.println("Serviço de Produto registrado com sucesso!");
            
            InterfaceTipoInsumo tipoInsumoStub = new ControllerTipoInsumo();
            Naming.rebind("//localhost/TipoInsumo", tipoInsumoStub);
            System.out.println("Serviço de TipoInsumo registrado com sucesso!");
            
            InterfaceTipoProduto tipoProdutoStub = new ControllerTipoProduto();
            Naming.rebind("//localhost/TipoProduto", tipoProdutoStub);
            System.out.println("Serviço de TipoProduto registrado com sucesso!");
            
            InterfaceTributo TributoStub = new ControllerTributo();
            Naming.rebind("//localhost/Tributo", TributoStub);
            System.out.println("Serviço de Tributo registrado com sucesso!");
            
            InterfaceUnidade UnidadeStub = new ControllerUnidade();
            Naming.rebind("//localhost/Unidade", UnidadeStub);
            System.out.println("Serviço de Unidade registrado com sucesso!");
            
            InterfaceUnidadeTributo unidadeTributoStub = new ControllerUnidadeTributo();
            Naming.rebind("//localhost/UnidadeTributo", unidadeTributoStub);
            System.out.println("Serviço de UnidadeTributo registrado com sucesso!");
            
            InterfaceVenda VendaStub = new ControllerVenda();
            Naming.rebind("//localhost/Venda", VendaStub);
            System.out.println("Serviço de Venda registrado com sucesso!");

            System.out.println("Servidor RMI pronto e aguardando conexões...");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro ao iniciar o servidor RMI: " + e.getMessage());
        }
    }
}

