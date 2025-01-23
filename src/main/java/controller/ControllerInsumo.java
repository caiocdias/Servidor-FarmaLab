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
                String sql = "INSERT INTO tipo de insumo (nome, quant, data_validade, created_at, updated_at) VALUES (?, ?. ?, ?, ?)";
                PreparedStatement stmt = conexao.prepareStatement(sql);

                stmt.setString(1, insumo.getNome());
                stmt.setFloat(2, insumo.getQuant());
                stmt.setTimestamp(3, insumo.getData_validade());
                stmt.setTimestamp(4, insumo.getCreated_at());
                stmt.setTimestamp(5, insumo.getUpdated_at());
                
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
                String sql = "UPDATE tipo de insumo SET nome = ?, quant = ?, data_validade = ?, updated_at = CURRENT_TIMESTAMP WHERE id = ?";
                PreparedStatement stmt = conexao.prepareStatement(sql);

                stmt.setString(1, insumo.getNome());
                stmt.setFloat(2, insumo.getQuant());
                stmt.setInt(3, insumo.getId());
                stmt.setTimestamp(4, insumo.getData_validade());

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
    public Insumo obterInsumo(Integer id) throws RemoteException {
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
                    insumo.setNome(rs.getString("nome"));
                    
                    return insumo;
                } else {
                    System.out.println("Insumo com ID " + id + " não encontrado.");
                }
            } else {
                System.out.println("Erro: conexão com o banco de dados não foi estabelecida.");
            }
        } catch (SQLException /* | NotBoundException | MalformedURLException */ e) {
            System.out.println("Erro ao obter o insumo: " + e.getMessage());
        } finally {
            Conexao.desconectar();
        }
        return insumo;
    }

    @Override
    public List<Insumo> buscarInsumosPorNome(String nome) throws RemoteException {
        List<Insumo> insumos = new ArrayList<>();
        try {
            Conexao.conectar();
            Connection conexao = Conexao.con;

            if (conexao != null) {
                String sql = "SELECT * FROM insumo WHERE nome LIKE ?";
                PreparedStatement stmt = conexao.prepareStatement(sql);
                stmt.setString(1, "%" + nome + "%");

                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    ControllerTipoInsumo controllerTipoInsumo = new ControllerTipoInsumo();
                    ControllerEstoque controllerEstoque = new ControllerEstoque();
                    Insumo insumo = new Insumo();

                    insumo.setId(rs.getInt("id"));
                    insumo.setQuant(rs.getFloat("quant"));
                    insumo.setHabilitado(rs.getBoolean("habilitado"));
                    insumo.setNome(rs.getString("nome"));
                    insumo.setData_validade(rs.getTimestamp("data_validade"));
                    insumo.setCreated_at(rs.getTimestamp("created_at"));
                    insumo.setUpdated_at(rs.getTimestamp("updated_at"));

                    insumo.setTipo_insumo(controllerTipoInsumo.obterTipoInsumo(rs.getInt("tipo_insumo_id")));     
                    insumo.setEstoque(controllerEstoque.obterEstoque(rs.getInt("estoque_id")));   
                    
                    insumos.add(insumo);
                }
            } else {
                System.out.println("Erro: conexão com o banco de dados não foi estabelecida.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RemoteException("Erro ao buscar insumo por nome: " + e.getMessage());
        } finally {
            Conexao.desconectar();
        }
        return insumos;
    }

}
