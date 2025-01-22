package controller;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import model.Cliente;
import model.Prescricao;
import util.Conexao;

public class ControllerPrescricao extends UnicastRemoteObject implements InterfacePrescricao {

    public ControllerPrescricao() throws RemoteException {
        super();
    }

    @Override
    public void inserirPrescricao(Prescricao prescricao) throws RemoteException {
        try {
            Conexao.conectar();
            Connection conexao = Conexao.con;

            if (conexao != null) {
                String sql = "INSERT INTO prescricao (crm, created_at, updated_at, id_cliente) VALUES (?, ?, ?, ?)";
                PreparedStatement stmt = conexao.prepareStatement(sql);

                stmt.setString(1, prescricao.getCrm());
                stmt.setTimestamp(2, prescricao.getCreated_at());
                stmt.setTimestamp(3, prescricao.getUpdated_at());
                stmt.setInt(4, prescricao.getCliente().getId());

                stmt.executeUpdate();
                System.out.println("Prescrição inserida com sucesso!");
            } else {
                System.out.println("Erro: conexão com o banco de dados não foi estabelecida.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RemoteException("Erro ao inserir prescrição: " + e.getMessage());
        } finally {
            Conexao.desconectar();
        }
    }

    @Override
    public void atualizarPrescricao(Prescricao prescricao) throws RemoteException {
        try {
            Conexao.conectar();
            Connection conexao = Conexao.con;

            if (conexao != null) {
                String sql = "UPDATE prescricao SET crm = ?, updated_at = CURRENT_TIMESTAMP, id_cliente = ? WHERE id = ?";
                PreparedStatement stmt = conexao.prepareStatement(sql);

                stmt.setString(1, prescricao.getCrm());
                stmt.setInt(2, prescricao.getCliente().getId());
                stmt.setInt(3, prescricao.getId());

                int linhasAfetadas = stmt.executeUpdate();

                if (linhasAfetadas > 0) {
                    System.out.println("Prescrição atualizada com sucesso!");
                } else {
                    System.out.println("Nenhuma prescrição encontrada com o ID fornecido.");
                }
            } else {
                System.out.println("Erro: conexão com o banco de dados não foi estabelecida.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RemoteException("Erro ao atualizar prescrição: " + e.getMessage());
        } finally {
            Conexao.desconectar();
        }
    }

    @Override
    public void desativarPrescricao(int id) throws RemoteException {
        try {
            Conexao.conectar();
            Connection conexao = Conexao.con;

            if (conexao != null) {
                String sql = "DELETE FROM prescricao WHERE id = ?";
                PreparedStatement stmt = conexao.prepareStatement(sql);
                stmt.setInt(1, id);

                int linhasAfetadas = stmt.executeUpdate();

                if (linhasAfetadas > 0) {
                    System.out.println("Prescrição removida com sucesso!");
                } else {
                    System.out.println("Nenhuma prescrição encontrada com o ID fornecido.");
                }
            } else {
                System.out.println("Erro: conexão com o banco de dados não foi estabelecida.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RemoteException("Erro ao remover prescrição: " + e.getMessage());
        } finally {
            Conexao.desconectar();
        }
    }

    @Override
    public Prescricao obterPrescricao(Integer id, String crm) throws RemoteException {
        Prescricao prescricao = null;
        try {
            Conexao.conectar();
            Connection conexao = Conexao.con;

            if (conexao != null) {
                String sql = "SELECT p.id, p.crm, p.created_at, p.updated_at, p.id_cliente, "
                        + "c.nome, c.cpf, c.endereco, c.telefone "
                        + "FROM prescricao p INNER JOIN pessoa c ON p.id_cliente = c.id WHERE 1=1";
                if (id != null) {
                    sql += " AND p.id = ?";
                }
                if (crm != null && !crm.isEmpty()) {
                    sql += " AND p.crm = ?";
                }

                PreparedStatement stmt = conexao.prepareStatement(sql);

                int paramIndex = 1;
                if (id != null) {
                    stmt.setInt(paramIndex++, id);
                }
                if (crm != null && !crm.isEmpty()) {
                    stmt.setString(paramIndex++, crm);
                }

                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    prescricao = new Prescricao(
                            rs.getInt("id"),
                            rs.getString("crm"),
                            rs.getTimestamp("created_at"),
                            rs.getTimestamp("updated_at"),
                            new Cliente(
                                    rs.getInt("id_cliente"),
                                    rs.getString("nome"),
                                    rs.getString("cpf"),
                                    rs.getString("endereco"),
                                    rs.getString("telefone"),
                                    true, // Assuming habilitado is true
                                    rs.getTimestamp("created_at"),
                                    rs.getTimestamp("updated_at")
                            )
                    );
                }
            } else {
                System.out.println("Erro: conexão com o banco de dados não foi estabelecida.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RemoteException("Erro ao pesquisar prescrição: " + e.getMessage());
        } finally {
            Conexao.desconectar();
        }
        return prescricao;
    }

}
