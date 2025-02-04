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
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import model.MedicoParceiro;
import model.Pedido;
import model.Prescricao;
import model.Produto;
import model.TipoInsumo;
import model.Tributo;
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
            
            Float valorTotalBase = 0.0f;
            for(Produto produto : pedido.getProdutos()){
                valorTotalBase += produto.getTipo_produto().getValor_base();
            }
            pedido.setValorTotalBase(valorTotalBase);
            
            Conexao.conectar();
            Connection conexao = Conexao.con;
            
            if (conexao != null) {
                String sql = "INSERT INTO pedido (id_cliente, id_funcionario, id_prescricao,id_unidade, status, habilitado, pronta_entrega, valor_total_base, valor_final, desconto_total, tributo_total) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement sentenca = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                pedido = calcularValorFinal(pedido);
                sentenca.setInt(1, pedido.getCliente().getId());
                sentenca.setInt(2, pedido.getFuncionario().getId());
                sentenca.setInt(3, pedido.getPrescricao().getId());
                sentenca.setInt(4, pedido.getUnidade().getId());
                sentenca.setString(5, pedido.getStatus().getDescricao());
                sentenca.setBoolean(6, pedido.isHabilitado());
                sentenca.setBoolean(7, pedido.isPronta_entrega());
                sentenca.setFloat(8, pedido.getValorTotalBase());
                sentenca.setFloat(9, pedido.getValorFinal());
                sentenca.setFloat(10, pedido.getDescontoTotal());
                sentenca.setFloat(11, pedido.getTributoTotal());
                
                sentenca.execute();
                ResultSet rs = sentenca.getGeneratedKeys();
                if (rs.next()) {
                    pedido.setId(rs.getInt(1));
                }
                ControllerProduto controllerProduto = new ControllerProduto();
                for(Produto produto : pedido.getProdutos()){
                    if(!pedido.isPronta_entrega()){
                        produto.setPedido_venda(pedido);
                        if(produto.getId() == 0){
                            produto.setPedido_producao(pedido);
                            controllerProduto.inserirProduto(produto);
                        }else{
                            controllerProduto.atualizarProduto(produto);
                        }
                    }else{
                        produto.setPronta_entrega(true);
                        controllerProduto.inserirProduto(produto);
                    }
                }
                
            } else {
                System.out.println("Erro: conexão com o banco de dados não foi estabelecida.");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao cadastrar o Pedido: " + e.getMessage());
            e.printStackTrace();
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
                    
                    pedido = new Pedido();
                    pedido.setId(resultado.getInt("id"));
                    pedido.setPronta_entrega(resultado.getBoolean("pronta_entrega"));
                    pedido.setHabilitado(resultado.getBoolean("habilitado"));
                    pedido.setDescontoTotal(resultado.getFloat("desconto_total"));
                    pedido.setValorTotalBase(resultado.getFloat("valor_total_base"));
                    pedido.setValorFinal(resultado.getFloat("valor_final"));
                    pedido.setTributoTotal(resultado.getFloat("tributo_total"));
                    
                    String statusString = resultado.getString("status");
                    pedido.setStatus(StatusPedido.valueOf(statusString));

                    pedido.setCliente(controllerCliente.obterCliente(resultado.getInt("id_cliente"), null));
                    pedido.setFuncionario(controllerFuncionario.obterFuncionario(resultado.getInt("id_funcionario"), null));
                    pedido.setPrescricao(controllerPrescricao.obterPrescricao(resultado.getInt("id_prescricao"), null));
                    pedido.setUnidade(controllerUnidade.obterUnidade(resultado.getInt("id_unidade")));

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
                String sql = "UPDATE pedido SET id_cliente = ?, id_funcionario = ?, id_prescricao = ?,id_unidade = ?, status = ?, habilitado = ?, pronta_entrega = ?, valor_total_base = ?, valor_final = ?, tributo_total = ?, updated_at = CURRENT_TIMESTAMP, WHERE id = ?";
                PreparedStatement sentenca = conexao.prepareStatement(sql);
                sentenca.setInt(1, pedido.getCliente().getId());
                sentenca.setInt(2, pedido.getFuncionario().getId());
                sentenca.setInt(3, pedido.getPrescricao().getId());
                sentenca.setInt(4, pedido.getPrescricao().getId());
                sentenca.setString(5, pedido.getStatus().getDescricao());
                sentenca.setBoolean(6, pedido.isHabilitado());
                sentenca.setBoolean(7, pedido.isPronta_entrega());
                sentenca.setFloat(8, pedido.getValorTotalBase());
                sentenca.setFloat(9, pedido.getValorFinal());
                sentenca.setFloat(10, pedido.getTributoTotal());
                sentenca.setInt(11, pedido.getId());
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
                String sql = "SELECT * FROM pedido WHERE id_cliente = ? AND habilitado = 1";
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
                    pedido.setTributoTotal(resultado.getFloat("tributo_total"));

                    pedido.setCliente(controllerCliente.obterCliente(resultado.getInt("id_cliente"), null));
                    pedido.setFuncionario(controllerFuncionario.obterFuncionario(resultado.getInt("id_funcionario"), null));
                    pedido.setPrescricao(controllerPrescricao.obterPrescricao(resultado.getInt("id_prescricao"), null));
                    pedido.setUnidade(controllerUnidade.obterUnidade(resultado.getInt("id_unidade")));
                    
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
    
    
    
    @Override
    public float calcularDescontoInsumo(Pedido pedido) throws RemoteException {
        float descontoInsumo = 0.0f;
        try {
            List<Produto> produtos = pedido.getProdutos();
            List<Integer> idsTipoInsumos = new ArrayList();
            for(Produto produto : produtos){
                for(TipoInsumo tipoInsumo : produto.getTipo_produto().getTipo_insumos()){
                    idsTipoInsumos.add(tipoInsumo.getId());
                }
            }
            ControllerInsumo controllerInsumo = new ControllerInsumo();
            int quantInsumoPromocao = controllerInsumo.promocaoInsumo(idsTipoInsumos); 
            descontoInsumo = quantInsumoPromocao * 0.03f;
            
        } catch (Exception e) {
            System.out.println("Erro ao calcular desconto para insumos: " + e.getMessage());
        }
        
    return descontoInsumo;
    }


    @Override
    public float calcularDescontoMedico(Pedido pedido) throws RemoteException {
    float descontoMedico = 0.0f;    
    try {
        Prescricao prescricao = pedido.getPrescricao();
        
        ControllerMedicoParceiro controllerMedicoParceiro = new ControllerMedicoParceiro();
        MedicoParceiro medico = controllerMedicoParceiro.obterMedicoParceiro(null, prescricao.getCrm());
        if(medico != null){
            descontoMedico = 0.05f;
        } 
    } catch (Exception e) {
        System.out.println("Erro ao calcular desconto do médico parceiro: " + e.getMessage());
    } 

    return descontoMedico;
    }

    @Override
    public Pedido calcularValorFinal(Pedido pedido) throws RemoteException {
        float valorFinal = 0.0f;
        try {
            pedido.setDescontoTotal(calcularDescontoInsumo(pedido) + calcularDescontoMedico(pedido));
            pedido.setTributoTotal(calcularTributo(pedido));
            valorFinal = pedido.getValorTotalBase() * (1 -  pedido.getDescontoTotal() + pedido.getTributoTotal());
            pedido.setValorFinal(valorFinal);
        } catch (Exception e) {
            System.out.println("Erro ao calcular valor total: " + e.getMessage());
        } 
        
        return pedido;
    }

    @Override
    public float calcularTributo(Pedido pedido) throws RemoteException {
        float tributoTotal = 0.0f;
        try{
            for(Tributo tributo : pedido.getUnidade().getTributos()){
                tributoTotal += tributo.getPorcentagem();
            }
        } catch(Exception e){
            
        }
        return tributoTotal;
    }

    @Override
    public List<Pedido> buscarPedidosStatus(StatusPedido status) throws RemoteException {
        List<Pedido> pedidos = new ArrayList<>();
        try {
            Conexao.conectar();
            Connection conexao = Conexao.con;

            if (conexao != null) {
                String sql = "SELECT id FROM pedido WHERE status = ? AND habilitado = 1";
                PreparedStatement stmt = conexao.prepareStatement(sql);
                stmt.setString(1, status.getDescricao());
                ResultSet rs = stmt.executeQuery();
                List<Integer> pedidosIds = new ArrayList(); 
                while (rs.next()){
                    pedidosIds.add(rs.getInt("id"));
                }
                pedidos = obterPedido(pedidosIds);
            } 
        } catch (Exception e) {
            e.printStackTrace();
            throw new RemoteException("Erro ao buscar pedidos por status: " + e.getMessage());
        } finally {
            Conexao.desconectar();
        }
        return pedidos;
    }
    
    public List<Pedido> obterPedido(List<Integer> ids) throws RemoteException {
        List<Pedido> pedidos = new ArrayList();
        try {
            Conexao.conectar();
            Connection conexao = Conexao.con;

            if (conexao != null) {
                String sql = "SELECT * FROM pedido WHERE id = ?";
                PreparedStatement sentenca = conexao.prepareStatement(sql);
                String pedidoIds = new String();
                for (int i = 0; i < ids.size(); i++) {
                    String pedidoId = ids.get(i).toString();
                    pedidoIds += pedidoId;
                    if (i != ids.size() - 1) {
                        pedidoIds += ",";
                    } 
                }
                sentenca.setString(1, pedidoIds);
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
                    pedido.setTributoTotal(resultado.getFloat("tributo_total"));

                    pedido.setCliente(controllerCliente.obterCliente(resultado.getInt("id_cliente"), null));
                    pedido.setFuncionario(controllerFuncionario.obterFuncionario(resultado.getInt("id_funcionario"), null));
                    pedido.setPrescricao(controllerPrescricao.obterPrescricao(resultado.getInt("id_prescricao"), null));
                    pedido.setUnidade(controllerUnidade.obterUnidade(resultado.getInt("id_unidade")));
                    
                    pedidos.add(pedido);
                }
            } else {
                System.out.println("Erro: conexão com o banco de dados não foi estabelecida.");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao obter o tributo: " + e.getMessage());
        } finally {
            Conexao.desconectar();
        }
        return pedidos;
    }

    @Override
    public void retirarPedido(Pedido pedido) throws RemoteException {
        pedido.setStatus(StatusPedido.CONCLUIDO);
        atualizarPedido(pedido);
        ControllerProduto controllerProduto = new ControllerProduto();
        for(Produto produto : pedido.getProdutos()){
            produto.setColetado(true);
            controllerProduto.atualizarProduto(produto);
        }
    }
    
}
