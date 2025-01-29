package controller;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Insumo;
import model.TipoInsumo;
import util.Conexao;

/**
 *
 * @author Thaíssa
 */

public class ControllerInsumo extends UnicastRemoteObject implements InterfaceInsumo {

    public ControllerInsumo() throws RemoteException {
        super();
    }

    @Override
    public void inserirInsumo(Insumo insumo) throws RemoteException {
        try {
            Conexao.conectar();
            Connection conexao = Conexao.con;

            if (conexao != null) {
                String sql = "INSERT INTO insumo (quant, data_validade,) VALUES (?, ?)";
                PreparedStatement stmt = conexao.prepareStatement(sql);
                stmt.setFloat(1, insumo.getQuant());
                stmt.setTimestamp(2, insumo.getData_validade());
                
                stmt.executeUpdate();
                System.out.println("Insumo inseridao com sucesso!");
            } else {        
                System.out.println("Erro: conexão com o banco de dados não foi estabelecida.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RemoteException("Erro ao inserir insumo: " + e.getMessage());
        } finally {
            Conexao.desconectar();
        }
    }

    @Override
    public void atualizarInsumo(Insumo insumo) throws RemoteException {
        try {
            Conexao.conectar();
            Connection conexao = Conexao.con;

            if (conexao != null) {
                String sql = "UPDATE tipo de insumo SET quant = ?, data_validade = ?, updated_at = CURRENT_TIMESTAMP WHERE id = ?";
                PreparedStatement stmt = conexao.prepareStatement(sql);
                stmt.setFloat(1, insumo.getQuant());
                stmt.setTimestamp(2, insumo.getData_validade());
                
                stmt.setInt(3, insumo.getId());

                int linhasAfetadas = stmt.executeUpdate();

                if (linhasAfetadas > 0) {
                    System.out.println("Insumo atualizado com sucesso!");
                } else {
                    System.out.println("Nenhum insumo encontrado com o ID fornecido.");
                }
            } else {
                System.out.println("Erro: conexão com o banco de dados não foi estabelecida.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RemoteException("Erro ao atualizar insumo: " + e.getMessage());
        } finally {
            Conexao.desconectar();
        }
    }

    @Override
    public void desativarInsumo(int id) throws RemoteException {
        try {
            Conexao.conectar();
            Connection conexao = Conexao.con;

            if (conexao != null) {
                String sql = "DELETE FROM insumo WHERE id = ?";
                PreparedStatement stmt = conexao.prepareStatement(sql);
                stmt.setInt(1, id);

                int linhasAfetadas = stmt.executeUpdate();

                if (linhasAfetadas > 0) {
                    System.out.println("Insumo removido com sucesso!");
                } else {
                    System.out.println("Nenhum insumo encontrado com o ID fornecido.");
                }
            } else {
                System.out.println("Erro: conexão com o banco de dados não foi estabelecida.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RemoteException("Erro ao remover insumo: " + e.getMessage());
        } finally {
            Conexao.desconectar();
        }
    }

    @Override
    public Insumo obterInsumo(int id) throws RemoteException {
        Insumo insumo = null;
        try {
            Conexao.conectar();
            Connection conexao = Conexao.con;

            if (conexao != null) {
                String sql = "SELECT * FROM insumo WHERE id = ?";
                PreparedStatement sentenca = conexao.prepareStatement(sql);
                sentenca.setInt(1, id);
                ResultSet rs = sentenca.executeQuery();
                

                if (rs.next()) {
                    insumo.setId(rs.getInt("id"));
                    insumo.setQuant(rs.getFloat("quant"));
                    insumo.setData_validade(rs.getTimestamp("data_validade"));
                    
                    return insumo;
                } else {
                    System.out.println("Insumo com ID " + id + " não encontrado.");
                }
            } else {
                System.out.println("Erro: conexão com o banco de dados não foi estabelecida.");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao obter o insumo: " + e.getMessage());
        } finally {
            Conexao.desconectar();
        }
        return insumo;
    }

    @Override
    public int promocaoInsumo(List<Integer> idsTipoInsumos) throws RemoteException {
        int insumosPromocao = 0;
        try{
            Conexao.conectar();
            Connection conexao = Conexao.con;

            if (conexao != null) {
                
                String sql = "SELECT id_tipo_insumo, SUM(quant) AS total_quant FROM insumo WHERE quant > 25 AND DATEDIFF(data_validade, CURRENT_DATE()) < 30 GROUP BY id_tipo_insumo;";
                PreparedStatement sentenca = conexao.prepareStatement(sql);
                String insumosIds = new String();
                
                for (int i = 0; i < idsTipoInsumos.size(); i++) {
                    String tributoId = idsTipoInsumos.get(i).toString();
                    insumosIds += tributoId;
                    if (i != idsTipoInsumos.size() - 1) {
                        insumosIds += ",";
                    } 
                }
                sentenca.setString(1, insumosIds);
                ResultSet rs = sentenca.executeQuery();
               
                if (rs.next()) {
                     insumosPromocao++;
                } else {
                    System.out.println("Insumo com estes IDs não encontrados.");
                }
            }else{
                System.out.println("Erro: conexão com o banco de dados não foi estabelecida.");
            }
        }catch (Exception e){
            
        } finally {
            Conexao.desconectar();
        }
        return insumosPromocao;
    }
}
