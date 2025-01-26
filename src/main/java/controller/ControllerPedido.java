/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import model.Insumo;
import model.MedicoParceiro;
import model.Pedido;
import model.Prescricao;
import model.enums.StatusPedido;
import util.Conexao;

/**
 *
 * @author Isabely
 */
public class ControllerPedido extends UnicastRemoteObject implements InterfacePedido{

    public ControllerPedido() throws RemoteException {
        super();
    }
    
    @Override
    public void inserirPedido(Pedido pedido) throws RemoteException {
        try {
            Conexao.conectar();
            Connection conexao = Conexao.con;

            if (conexao != null) {
                String sql = "INSERT INTO pedido (id_cliente, id_funcionario, id_prescricao,id_unidade, status, habilitado, pronta_entrega, valor_total_base) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement sentenca = conexao.prepareStatement(sql);
                sentenca.setInt(1, pedido.getCliente().getId());
                sentenca.setInt(2, pedido.getFuncionario().getId());
                sentenca.setInt(3, pedido.getPrescricao().getId());
                sentenca.setInt(4, pedido.getUnidade().getId());
                sentenca.setString(5, pedido.getStatus().name());
                sentenca.setBoolean(6, pedido.isHabilitado());
                sentenca.setBoolean(7, pedido.isPronta_entrega());
                sentenca.setFloat(8, pedido.getValorTotalBase());
                sentenca.execute();
            } else {
                System.out.println("Erro: conexão com o banco de dados não foi estabelecida.");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao cadastrar o Pedido: " + e.getMessage());
        } finally {
            Conexao.desconectar();
        }
    }

    @Override
    public Pedido obterPedido(int id) throws RemoteException {
        Pedido pedido = null;
        try {
            Conexao.conectar();
            Connection conexao = Conexao.con;

            if (conexao != null) {
                String sql = "SELECT * FROM pedido WHERE id = ?";
                PreparedStatement sentenca = conexao.prepareStatement(sql);
                sentenca.setInt(1, id);
                ResultSet resultado = sentenca.executeQuery();

                if (resultado.next()) {
                    ControllerCliente controllerCliente = new ControllerCliente();
                    ControllerFuncionario controllerFuncionario = new ControllerFuncionario();
                    ControllerPrescricao controllerPrescricao = new ControllerPrescricao();
                    ControllerUnidade controllerUnidade = new ControllerUnidade();
                    
                    pedido.setId(resultado.getInt("id"));
                    pedido.setPronta_entrega(resultado.getBoolean("pronta_entrega"));
                    pedido.setHabilitado(resultado.getBoolean("habilitado"));
                    pedido.setDescontoTotal(resultado.getFloat("desconto_total"));
                    pedido.setValorTotalBase(resultado.getFloat("valor_total_base"));
                    pedido.setValorFinal(resultado.getFloat("valor_final"));
                    
                    String statusString = resultado.getString("status");
                    pedido.setStatus(StatusPedido.valueOf(statusString));

                    pedido.setCliente(controllerCliente.obterCliente(resultado.getInt("cliente_id"), null));
                    pedido.setFuncionario(controllerFuncionario.obterFuncionario(resultado.getInt("funcionario_id"), null));
                    pedido.setPrescricao(controllerPrescricao.obterPrescricao(resultado.getInt("prescricao_id"), null));
                    pedido.setUnidade(controllerUnidade.obterUnidade(resultado.getInt("unidade_id")));

                     return pedido;
                } else {
                    System.out.println("Pedido com ID " + id + " não encontrado.");
                }
            } else {
                System.out.println("Erro: conexão com o banco de dados não foi estabelecida.");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao obter o Pedido: " + e.getMessage());
        } finally {
            Conexao.desconectar();
        }
        return pedido;
    }

    @Override
    public void atualizarPedido(Pedido pedido) throws RemoteException {
        try {
            Conexao.conectar();
            Connection conexao = Conexao.con;
            if (conexao != null) {
                String sql = "UPDATE pedido SET id_cliente = ?, id_funcionario = ?, id_prescricao = ?,id_unidade = ?, status = ?, habilitado = ?, pronta_entrega = ?, valor_total_base = ?, updated_at = CURRENT_TIMESTAMP, WHERE id = ?";
                PreparedStatement sentenca = conexao.prepareStatement(sql);
                sentenca.setInt(1, pedido.getCliente().getId());
                sentenca.setInt(2, pedido.getFuncionario().getId());
                sentenca.setInt(3, pedido.getPrescricao().getId());
                sentenca.setInt(4, pedido.getPrescricao().getId());
                sentenca.setString(5, pedido.getStatus().name());
                sentenca.setBoolean(6, pedido.isHabilitado());
                sentenca.setBoolean(7, pedido.isPronta_entrega());
                sentenca.setFloat(8, pedido.getValorTotalBase());
                sentenca.setInt(9, pedido.getId());
                sentenca.executeUpdate();

                System.out.println("Pedido atualizado com sucesso!");
            } else {
                System.out.println("Erro: conexão com o banco de dados não foi estabelecida.");
            }
        } catch (SQLException e) {
            throw new RemoteException("Erro ao atualizar pedido: " + e.getMessage());
        } finally {
            Conexao.desconectar();
        }
    }

    @Override
    public void desativarPedido(int id) throws RemoteException {
        try{
            Conexao.conectar();
            Connection conexao = Conexao.con;

            if (conexao != null) {
                String sql = "UPDATE pedido SET habilitado = false, updated_at = CURRENT_TIMESTAMP WHERE id = ?";
                PreparedStatement sentenca = conexao.prepareStatement(sql);
                sentenca.setInt(1, id);
                int linhasAfetadas = sentenca.executeUpdate();
                if (linhasAfetadas > 0) {
                    System.out.println("Pedido desabilitada com sucesso.");
                } else {
                    System.out.println("Nenhum pedido foi encontrado para o ID informado.");
                }
            } else {
                System.out.println("Erro: conexão com o banco de dados não foi estabelecida.");
            }
        }catch(SQLException e){
            throw new RemoteException("Erro ao remover pedido: " + e.getMessage());
        }
    }

    @Override
    public List<Pedido> buscarPedidosPorCliente(int clienteId) throws RemoteException {
        List<Pedido> pedidos = new ArrayList<>();
        try {
            Conexao.conectar();
            Connection conexao = Conexao.con;

            if (conexao != null) {
                String sql = "SELECT * FROM pedido WHERE id_cliente = ?";
                PreparedStatement sentenca = conexao.prepareStatement(sql);
                sentenca.setInt(1, clienteId);
                ResultSet resultado = sentenca.executeQuery();
                
                ControllerCliente controllerCliente = new ControllerCliente();
                ControllerFuncionario controllerFuncionario = new ControllerFuncionario();
                ControllerPrescricao controllerPrescricao = new ControllerPrescricao();
                ControllerUnidade controllerUnidade = new ControllerUnidade();
                while (resultado.next()) {
                    Pedido pedido = new Pedido();

                    pedido.setId(resultado.getInt("id"));
                    pedido.setPronta_entrega(resultado.getBoolean("pronta_entrega"));
                    pedido.setHabilitado(resultado.getBoolean("habilitado"));
                    pedido.setStatus(StatusPedido.valueOf(resultado.getString("status")));
                    pedido.setDescontoTotal(resultado.getFloat("desconto_total"));
                    pedido.setValorTotalBase(resultado.getFloat("valor_total_base"));
                    pedido.setValorFinal(resultado.getFloat("valor_final"));

                    pedido.setCliente(controllerCliente.obterCliente(resultado.getInt("id_cliente"), null));
                    pedido.setFuncionario(controllerFuncionario.obterFuncionario(resultado.getInt("id_funcionario"), null));
                    pedido.setPrescricao(controllerPrescricao.obterPrescricao(resultado.getInt("prescricao_id"), null));
                    pedido.setUnidade(controllerUnidade.obterUnidade(resultado.getInt("unidade_id")));
                    
                    pedidos.add(pedido);
                }
            } else {
                System.out.println("Erro: conexão com o banco de dados não foi estabelecida.");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao buscar pedidos por cliente: " + e.getMessage());
        } finally {
            Conexao.desconectar();
        }

    return pedidos;
    }
    
    
    //[to-do]: Metodos de calculo
    @Override
    public float calcularDescontoInsumo(Pedido pedido) throws RemoteException {
        float descontoInsumo = 0;
        /*
        try {
            ControllerInsumo controllerInsumo = new ControllerInsumo();
            Insumo insumo = controllerInsumo.obterInsumos(pedido); //terminar de desenvolver essa parte ainda, obter insumos de acordo com o pedido, talvez tem q setar um for (dependendo de quando insumos tem pra vencer)
            Timestamp dataValidade = insumo.getData_validade();
            long diasRestantes = (dataValidade.getTime() - System.currentTimeMillis()) / (1000 * 60 * 60 * 24);
            if (diasRestantes <= 7) {
                descontoInsumo = pedido.getValorTotalBase() * 0.10f; // 10% de desconto
            }

        } catch (Exception e) {
            System.out.println("Erro ao calcular desconto para insumos: " + e.getMessage());
        } 
        */
    return descontoInsumo;
    }


    @Override
    public float calcularDescontoMedico(Pedido pedido) throws RemoteException {
    float descontoMedico = 0;    
    try {
        Prescricao prescricao = pedido.getPrescricao();
        
        ControllerMedicoParceiro controllerMedicoParceiro = new ControllerMedicoParceiro();
        MedicoParceiro medico = controllerMedicoParceiro.obterMedicoParceiro(null, prescricao.getCrm());
        if(medico != null){
            descontoMedico = pedido.getValorTotalBase() * 0.05f; // 5% de desconto
        } 
    } catch (Exception e) {
        System.out.println("Erro ao calcular desconto médico: " + e.getMessage());
    } 

    return descontoMedico;
    }

    @Override
    public float calcularValorFinal(Pedido pedido) throws RemoteException {
        float valorFinal = 0;
        try {
            pedido.setDescontoTotal(calcularDescontoInsumo(pedido) + calcularDescontoMedico(pedido));
            ControllerTributo controllerTributo = new ControllerTributo();
            //float tributos = ControllerTributos.calcularTributos();
            valorFinal = pedido.getValorTotalBase() -  pedido.getDescontoTotal(); //+ tributos
            
            Conexao.conectar();
            Connection conexao = Conexao.con;

            if (conexao != null) {
                String sql = "INSERT INTO pedido (desconto_total, valor_final) VALUES (?, ?)";
                PreparedStatement sentenca = conexao.prepareStatement(sql);
                sentenca.setFloat(1, pedido.getDescontoTotal());
                sentenca.setFloat(2, pedido.getValorFinal());
                sentenca.execute();
            }else {
                System.out.println("Erro: conexão com o banco de dados não foi estabelecida.");
            }
        } catch (RemoteException | SQLException e) {
            System.out.println("Erro ao calcular valor total: " + e.getMessage());
        } 
        
        return valorFinal;
    }
    
}
