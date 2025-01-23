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
import model.NotaFiscal;
import util.Conexao;

/**
 *
 * @author Isabely
 */
public class ControllerNotaFiscal extends UnicastRemoteObject implements InterfaceNotaFiscal{

    public ControllerNotaFiscal() throws RemoteException {
        super();
    }
    
    @Override
    public void cadastrarNotaFiscal(NotaFiscal nf) throws RemoteException {
        try{
            Conexao.conectar();
            Connection conexao = Conexao.con;
            
            if (conexao != null) {
                String sqlNotaFiscal = "INSERT INTO nota_fiscal (num_nota, data_emissao, valor_total, habilitado) VALUES (?, ?, ?, ?)";
                PreparedStatement sentencaNotaFiscal = Conexao.con.prepareStatement(sqlNotaFiscal);
                sentencaNotaFiscal.setInt(1, nf.getNum_nota());
                sentencaNotaFiscal.setTimestamp(2, nf.getData_emissao());
                sentencaNotaFiscal.setFloat(3, nf.getValor_total());
                sentencaNotaFiscal.setBoolean(4, nf.isHabilitado());
                
                System.out.println("Nota fiscal cadastrada com sucesso!");
            } else {
                System.out.println("Erro: conexão com o banco de dados não foi estabelecida.");
            }
           }catch(SQLException e){
               System.out.println(e.getMessage());
           } finally {
            Conexao.desconectar();
            }
    }

    @Override
    public NotaFiscal obterNotaFiscal(int id) throws RemoteException {
        NotaFiscal nf = null;
        try {
            String sql = "SELECT * FROM nota_fiscal WHERE id = ?";
            Conexao.conectar();
            Connection conexao = Conexao.con;
            
            if (conexao != null) {
                PreparedStatement sentenca = Conexao.con.prepareStatement(sql);
                sentenca.setInt(1, id);
                ResultSet resultado = sentenca.executeQuery();

                if (resultado.next()) {
                    nf.setId(resultado.getInt("id"));
                    nf.setNum_nota(resultado.getInt("num_nota"));
                    nf.setData_emissao(resultado.getTimestamp("data_emissao"));
                    nf.setValor_total(resultado.getFloat("valor_total"));
                    nf.setHabilitado(resultado.getBoolean("habilitado"));

                    return nf;
                } else {
                    System.out.println("Nota Fiscal com ID " + id + " não encontrado.");
                }
            } else {
                System.out.println("Erro: conexão com o banco de dados não foi estabelecida.");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao obter o Nota Fiscal: " + e.getMessage());
        } finally {
            Conexao.desconectar();
        }
        return nf;
    }

    @Override
    public void atualizarNotaFiscal(NotaFiscal nf) throws RemoteException {
        try {
            Conexao.conectar();
            Connection conexao = Conexao.con;
            
            if (conexao != null) {
                String sql = "UPDATE nota_fiscal SET num_nota = ?, data_emissao = ?, valor_total = ?, habilitado = ?, updated_at = CURRENT_TIMESTAMP, WHERE id = ?";
                PreparedStatement sentenca = Conexao.con.prepareStatement(sql);
                sentenca.setInt(1, nf.getNum_nota());
                sentenca.setTimestamp(2, nf.getData_emissao());
                sentenca.setFloat(3, nf.getValor_total());
                sentenca.setBoolean(4, nf.isHabilitado());
                sentenca.executeUpdate();

                System.out.println("Nota Fiscal atualizado com sucesso!");
            } else {
                System.out.println("Erro: conexão com o banco de dados não foi estabelecida.");
            }
        } catch (SQLException e) {
            throw new RemoteException("Erro ao atualizar Nota Fiscal: " + e.getMessage());
        } finally {
            Conexao.desconectar();
        }
    }

    @Override
    public void desativarNotaFiscal(int id) throws RemoteException {
        try{
            Conexao.conectar();
            Connection conexao = Conexao.con;
            
            if (conexao != null) {
                String sql = "UPDATE nota_fiscal SET habilitado = false, updated_at = CURRENT_TIMESTAMP WHERE id = ?";
                PreparedStatement sentenca = Conexao.con.prepareStatement(sql);
                sentenca.setInt(1, id);
                int linhasAfetadas = sentenca.executeUpdate();
                
                if (linhasAfetadas > 0) {
                    System.out.println("Nota Fiscal desativada com sucesso.");
                } else {
                    System.out.println("Nenhuma Nota Fiscal foi encontrado para o ID informado.");
                }
            } else {
                System.out.println("Erro: conexão com o banco de dados não foi estabelecida.");
            }
        }catch(SQLException e){
            throw new RemoteException("Erro ao desativar Nota Fiscal: " + e.getMessage());
        }
    }
    
}
