package controller;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
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
                String sql = "INSERT INTO insumo (id_tipo_insumo, id_estoque, quant, data_validade, habilitado) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement stmt = conexao.prepareStatement(sql);
                stmt.setInt(1, insumo.getTipo_insumo().getId());
                stmt.setInt(2, insumo.getEstoque().getId());
                stmt.setFloat(3, insumo.getQuant());
                stmt.setTimestamp(4, insumo.getData_validade());
                stmt.setBoolean(5, insumo.isHabilitado());
                stmt.executeUpdate();
                System.out.println("Insumo inserido com sucesso!");
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
                String sql = "UPDATE tipo de insumo SET id_tipo_insumo = ?, id_estoque = ?, quant = ?, data_validade = ?, habilitado = ?, updated_at = CURRENT_TIMESTAMP WHERE id = ?";
                PreparedStatement stmt = conexao.prepareStatement(sql);
                stmt.setInt(1, insumo.getTipo_insumo().getId());
                stmt.setInt(2, insumo.getEstoque().getId());
                stmt.setFloat(3, insumo.getQuant());
                stmt.setTimestamp(4, insumo.getData_validade());
                stmt.setBoolean(5, insumo.isHabilitado());
                stmt.setInt(6, insumo.getId());

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
                    ControllerEstoque controllerEstoque = new ControllerEstoque();
                    ControllerTipoInsumo controllerTipoInsumo = new ControllerTipoInsumo();
                    
                    insumo = new Insumo();
                    insumo.setId(rs.getInt("id"));
                    insumo.setQuant(rs.getFloat("quant"));
                    insumo.setData_validade(rs.getTimestamp("data_validade"));
                    insumo.setHabilitado(rs.getBoolean("habilitado"));
                    insumo.setEstoque(controllerEstoque.obterEstoque(rs.getInt("id_estoque")));
                    insumo.setTipo_insumo(controllerTipoInsumo.obterTipoInsumo(rs.getInt("id_tipo_insumo")));
                    
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
    
    if (idsTipoInsumos.isEmpty()) {
        return insumosPromocao;
    }

    try {
        Conexao.conectar();
        Connection conexao = Conexao.con;

        if (conexao != null) {
            // Construção dinâmica do IN (?) com placeholders
            String placeholders = String.join(",", Collections.nCopies(idsTipoInsumos.size(), "?"));
            String sql = "SELECT id_tipo_insumo FROM insumo " +
                         "WHERE quant > 25 AND DATEDIFF(data_validade, CURRENT_DATE()) < 30 " +
                         "AND id_tipo_insumo IN (" + placeholders + ") " +
                         "GROUP BY id_tipo_insumo;";

            PreparedStatement sentenca = conexao.prepareStatement(sql);

            // Preenchendo os parâmetros do PreparedStatement
            for (int i = 0; i < idsTipoInsumos.size(); i++) {
                sentenca.setInt(i + 1, idsTipoInsumos.get(i));
            }

            ResultSet rs = sentenca.executeQuery();

            // Contando quantos insumos atendem aos critérios
            while (rs.next()) {
                insumosPromocao++;
            }
        } else {
            System.out.println("Erro: conexão com o banco de dados não foi estabelecida.");
        }
    } catch (Exception e) {
        e.printStackTrace();
    } finally {
        Conexao.desconectar();
    }

    return insumosPromocao;
}
}
