package controller;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import util.Conexao;
import model.TipoInsumo;

/**
 *
 * @author Thaíssa
 */

public class ControllerTipoInsumo extends UnicastRemoteObject implements InterfaceTipoInsumo {

    public ControllerTipoInsumo() throws RemoteException {
        super();
    }

    @Override
    public void inserirTipoInsumo(TipoInsumo tipoInsumo) throws RemoteException {
        try {
            Conexao.conectar();
            Connection conexao = Conexao.con;

            if (conexao != null) {
                String sql = "INSERT INTO tipo de insumo (nome, quant, habilitado) VALUES (?, ?, ?)";
                PreparedStatement stmt = conexao.prepareStatement(sql);

                stmt.setString(1, tipoInsumo.getNome());
                stmt.setFloat(2, tipoInsumo.getQuant());
                stmt.setBoolean(3, tipoInsumo.isHabilitado());

                stmt.executeUpdate();
                System.out.println("Tipo de insumo inserida com sucesso!");
            } else {
                System.out.println("Erro: conexão com o banco de dados não foi estabelecida.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RemoteException("Erro ao inserir tipo de insumo: " + e.getMessage());
        } finally {
            Conexao.desconectar();
        }
    }

    @Override
    public void atualizarTipoInsumo(TipoInsumo tipoInsumo) throws RemoteException {
        try {
            Conexao.conectar();
            Connection conexao = Conexao.con;

            if (conexao != null) {
                String sql = "UPDATE tipo_insumo SET nome = ?, quant = ?, habilitado = ?, updated_at = CURRENT_TIMESTAMP WHERE id = ?";
                PreparedStatement stmt = conexao.prepareStatement(sql);

                stmt.setString(1, tipoInsumo.getNome());
                stmt.setFloat(2, tipoInsumo.getQuant());
                stmt.setBoolean(3, tipoInsumo.isHabilitado());
                stmt.setInt(4, tipoInsumo.getId());

                int linhasAfetadas = stmt.executeUpdate();

                if (linhasAfetadas > 0) {
                    System.out.println("Tipo de insumo atualizado com sucesso!");
                } else {
                    System.out.println("Nenhum tipo de insumo encontrado com o ID fornecido.");
                }
            } else {
                System.out.println("Erro: conexão com o banco de dados não foi estabelecida.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RemoteException("Erro ao atualizar tipo de insumo: " + e.getMessage());
        } finally {
            Conexao.desconectar();
        }
    }

    @Override
    public void desativarTipoInsumo(int id) throws RemoteException {
        try {
            Conexao.conectar();
            Connection conexao = Conexao.con;

            if (conexao != null) {
                String sql = "DELETE FROM tipoInsumo WHERE id = ?";
                PreparedStatement stmt = conexao.prepareStatement(sql);
                stmt.setInt(1, id);

                int linhasAfetadas = stmt.executeUpdate();

                if (linhasAfetadas > 0) {
                    System.out.println("Tipo de insumo removido com sucesso!");
                } else {
                    System.out.println("Nenhum tipo de insumo encontrado com o ID fornecido.");
                }
            } else {
                System.out.println("Erro: conexão com o banco de dados não foi estabelecida.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RemoteException("Erro ao remover tipo de insumo: " + e.getMessage());
        } finally {
            Conexao.desconectar();
        }
    }

    @Override
    public TipoInsumo obterTipoInsumo(int id) throws RemoteException {
        TipoInsumo tipoInsumo = null;
        try {
            Conexao.conectar();
            Connection conexao = Conexao.con;

            if (conexao != null) {
                String sql = "SELECT * FROM tipo_insumo WHERE id = ?";
                PreparedStatement sentenca = conexao.prepareStatement(sql);
                sentenca.setInt(1, id);
                ResultSet resultado = sentenca.executeQuery();

                if (resultado.next()) {

                    tipoInsumo.setId(resultado.getInt("id"));
                    tipoInsumo.setNome(resultado.getString("nome"));
                    tipoInsumo.setQuant(resultado.getFloat("quantidade"));
                    tipoInsumo.setHabilitado(resultado.getBoolean("habilitado"));

                    return tipoInsumo;
                } else {
                    System.out.println("Tipo de insumo com ID " + id + " não encontrado.");
                }
            } else {
                System.out.println("Erro: conexão com o banco de dados não foi estabelecida.");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao obter o tipo de insumo: " + e.getMessage());
        } finally {
            Conexao.desconectar();
        }
        return tipoInsumo;
    }

    @Override
    public List<TipoInsumo> buscarTipoInsumoPorNome(String nome) throws RemoteException {
        List<TipoInsumo> tipoInsumos = new ArrayList<>();
        try {
            Conexao.conectar();
            Connection conexao = Conexao.con;

            if (conexao != null) {
                String sql = "SELECT id, nome, quant, habilitado, created_at, updated_at FROM tipo_insumo WHERE nome LIKE ?";
                PreparedStatement stmt = conexao.prepareStatement(sql);
                stmt.setString(1, "%" + nome + "%");

                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    TipoInsumo tipoInsumo = new TipoInsumo();

                    tipoInsumo.setId(rs.getInt("id"));
                    tipoInsumo.setNome(rs.getString("nome"));
                    tipoInsumo.setQuant(rs.getFloat("quant"));
                    tipoInsumo.setHabilitado(rs.getBoolean("habilitado"));
                    tipoInsumo.setCreated_at(rs.getTimestamp("created_at"));
                    tipoInsumo.setUpdated_at(rs.getTimestamp("updated_at"));
                    
                    tipoInsumos.add(tipoInsumo);
                }
            } else {
                System.out.println("Erro: conexão com o banco de dados não foi estabelecida.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RemoteException("Erro ao buscar tipo de insumo por nome: " + e.getMessage());
        } finally {
            Conexao.desconectar();
        }
        return tipoInsumos;
    }

    @Override
    public List<TipoInsumo> obterTipoInsumo(List<Integer> ids) throws RemoteException {
        List<TipoInsumo> tipoInsumos = new ArrayList();
        try {
            Conexao.conectar();
            Connection conexao = Conexao.con;

            if (conexao != null) {
                String sql = "SELECT * FROM tipo_insumo WHERE id = ?";
                PreparedStatement sentenca = conexao.prepareStatement(sql);
                String insumosIds = new String();
                for (int i = 0; i < ids.size(); i++) {
                    String insumoId = ids.get(i).toString();
                    insumosIds += insumoId;
                    if (i != tipoInsumos.size() - 1) {
                        insumosIds += ",";
                    } 
                }
                sentenca.setString(1, insumosIds);
                ResultSet resultado = sentenca.executeQuery();

                while (resultado.next()) {
                    TipoInsumo tipoInsumo = new TipoInsumo(
                        resultado.getInt("id"),
                        resultado.getString("nome"),
                        resultado.getFloat("quantidade"),
                        resultado.getBoolean("habilitado"),
                        resultado.getTimestamp("created_at"),
                        resultado.getTimestamp("updated_at")
                    );
                    tipoInsumos.add(tipoInsumo);
                }
            } else {
                System.out.println("Erro: conexão com o banco de dados não foi estabelecida.");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao obter o tipo de insumo: " + e.getMessage());
        } finally {
            Conexao.desconectar();
        }
        return tipoInsumos;
    }

}
