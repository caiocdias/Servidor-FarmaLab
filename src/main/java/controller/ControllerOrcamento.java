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
import model.Orcamento;
import model.enums.StatusOrcamento;
import util.Conexao;


/**
 *
 * @author Isabely
 */
public class ControllerOrcamento extends UnicastRemoteObject implements InterfaceOrcamento{
    
    public ControllerOrcamento() throws RemoteException{
        super();
    }

    @Override
    public void inserirOrcamento(Orcamento orcamento) throws RemoteException {
    
        try {
            Conexao.conectar();
            Connection conexao = Conexao.con;

            if (conexao != null) {
                String sql = "INSERT INTO orcamento (id_unidade, id_cliente, id_funcionario, status, descricao, observacoes, habilitado) VALUES (?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement sentenca = conexao.prepareStatement(sql);
                sentenca.setInt(1, orcamento.getUnidade().getId());
                sentenca.setInt(2, orcamento.getCliente().getId());
                sentenca.setInt(3, orcamento.getFuncionario().getId());
                sentenca.setString(4, orcamento.getStatus().getDescricao());
                sentenca.setString(5, orcamento.getDescricao());
                sentenca.setString(6, orcamento.getObservacoes());
                sentenca.setBoolean(7, orcamento.isHabilitado());
                sentenca.execute();
            } else {
                System.out.println("Erro: conexão com o banco de dados não foi estabelecida.");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao cadastrar o Orçamento: " + e.getMessage());
        } finally {
            Conexao.desconectar();
        }
    }

    @Override
    public Orcamento obterOrcamento(int id) throws RemoteException {
        Orcamento orcamento = null;
        try {
            Conexao.conectar();
            Connection conexao = Conexao.con;

            if (conexao != null) {
                String sql = "SELECT * FROM orcamento WHERE id = ? AND habilitado = 1";
                PreparedStatement sentenca = conexao.prepareStatement(sql);
                sentenca.setInt(1, id);
                ResultSet resultado = sentenca.executeQuery();

                if (resultado.next()) {
                    ControllerCliente controllerCliente = new ControllerCliente();
                    ControllerFuncionario controllerFuncionario = new ControllerFuncionario();
                    ControllerUnidade controllerUnidade = new ControllerUnidade();
                    
                    String descricaoSelecionada = resultado.getString("status");
                    StatusOrcamento statusAtualizado = null;
                    for (StatusOrcamento status : StatusOrcamento.values()) {
                        if (status.getDescricao().equals(descricaoSelecionada)) {
                            statusAtualizado = status;
                        }
                    }
                    
                    orcamento = new Orcamento(
                            resultado.getInt("id"),
                            controllerUnidade.obterUnidade(resultado.getInt("id_unidade")),
                            controllerCliente.obterCliente(resultado.getInt("id_cliente"), null),
                            controllerFuncionario.obterFuncionario(resultado.getInt("id_funcionario"), null),
                            statusAtualizado,
                            resultado.getString("descricao"),
                            resultado.getString("observacoes"),
                            resultado.getBoolean("habilitado"),
                            resultado.getTimestamp("created_at"),
                            resultado.getTimestamp("updated_at")
                    );
                     return orcamento;
                } else {
                    System.out.println("Orçamento com ID " + id + " não encontrado.");
                }
            } else {
                System.out.println("Erro: conexão com o banco de dados não foi estabelecida.");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao obter o Orçamento: " + e.getMessage());
        } finally {
            Conexao.desconectar();
        }
        return orcamento;
    }

    @Override
    public void atualizarOrcamento(Orcamento orcamento) throws RemoteException {
        try {
            Conexao.conectar();
            Connection conexao = Conexao.con;
            if (conexao != null) {
                String sql = "UPDATE orcamento SET id_unidade = ?, id_cliente = ?, id_funcionario = ?, status = ?, descricao = ?, observacoes = ?, habilitado = ?, updated_at = CURRENT_TIMESTAMP WHERE id = ?";
                PreparedStatement sentenca = conexao.prepareStatement(sql);
                sentenca.setInt(1, orcamento.getUnidade().getId());
                sentenca.setInt(2, orcamento.getCliente().getId());
                sentenca.setInt(3, orcamento.getFuncionario().getId());
                sentenca.setString(4, orcamento.getStatus().getDescricao());
                sentenca.setString(5, orcamento.getDescricao());
                sentenca.setString(6, orcamento.getObservacoes());
                sentenca.setBoolean(7, orcamento.isHabilitado());
                sentenca.setInt(8, orcamento.getId());
                sentenca.executeUpdate();

                System.out.println("Orçamento atualizado com sucesso!");
            } else {
                System.out.println("Erro: conexão com o banco de dados não foi estabelecida.");
            }
        } catch (SQLException e) {
            throw new RemoteException("Erro ao atualizar orçamento: " + e.getMessage());
        } finally {
            Conexao.desconectar();
        }
    }

    @Override
    public void desativarOrcamento(int id) throws RemoteException {
        try{
            Conexao.conectar();
            Connection conexao = Conexao.con;

            if (conexao != null) {
                String sql = "UPDATE orcamento SET habilitado = false, updated_at = CURRENT_TIMESTAMP WHERE id = ?";
                PreparedStatement sentenca = conexao.prepareStatement(sql);
                sentenca.setInt(1, id);
                int linhasAfetadas = sentenca.executeUpdate();
                if (linhasAfetadas > 0) {
                    System.out.println("Orçamento desabilitada com sucesso.");
                } else {
                    System.out.println("Nenhum orçamento foi encontrado para o ID informado.");
                }
            } else {
                System.out.println("Erro: conexão com o banco de dados não foi estabelecida.");
            }
        }catch(SQLException e){
            throw new RemoteException("Erro ao remover orçamento: " + e.getMessage());
        }
    }
}
