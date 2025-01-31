package controller;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import model.MedicoParceiro;
import util.Conexao;

public class ControllerMedicoParceiro extends UnicastRemoteObject implements InterfaceMedicoParceiro {

    public ControllerMedicoParceiro() throws RemoteException {
        super();
    }

    @Override
    public void inserirMedicoParceiro(MedicoParceiro medicoParceiro) throws RemoteException {
        try {
            Conexao.conectar();
            Connection conexao = Conexao.con;

            if (conexao != null) {
                String sqlPessoa = "INSERT INTO pessoa (nome, endereco, cpf, telefone) VALUES (?, ?, ?, ?)";
                PreparedStatement stmtPessoa = conexao.prepareStatement(sqlPessoa, Statement.RETURN_GENERATED_KEYS);
                stmtPessoa.setString(1, medicoParceiro.getNome());
                stmtPessoa.setString(2, medicoParceiro.getEndereco());
                stmtPessoa.setString(3, medicoParceiro.getCpf());
                stmtPessoa.setString(4, medicoParceiro.getTelefone());
                stmtPessoa.executeUpdate();

                ResultSet rs = stmtPessoa.getGeneratedKeys();
                int idPessoa = 0;
                if (rs.next()) {
                    idPessoa = rs.getInt(1);
                }

                String sqlMedicoParceiro = "INSERT INTO medico_parceiro (id_pessoa, crm, estado, habilitado, created_at, updated_at) "
                        + "VALUES (?, ?, ?, ?, ?, ?)";
                PreparedStatement stmtMedicoParceiro = conexao.prepareStatement(sqlMedicoParceiro);
                stmtMedicoParceiro.setInt(1, idPessoa);
                stmtMedicoParceiro.setString(2, medicoParceiro.getCrm());
                stmtMedicoParceiro.setString(3, medicoParceiro.getEstado());
                stmtMedicoParceiro.setBoolean(4, medicoParceiro.isHabilitado());
                stmtMedicoParceiro.setTimestamp(5, medicoParceiro.getCreated_at());
                stmtMedicoParceiro.setTimestamp(6, medicoParceiro.getUpdated_at());
                stmtMedicoParceiro.executeUpdate();

                System.out.println("Médico Parceiro inserido com sucesso!");
            } else {
                System.out.println("Erro: conexão com o banco de dados não foi estabelecida.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RemoteException("Erro ao inserir médico parceiro: " + e.getMessage());
        } finally {
            Conexao.desconectar();
        }
    }

    @Override
    public void atualizarMedicoParceiro(MedicoParceiro medicoParceiro) throws RemoteException {
        try {
            Conexao.conectar();
            Connection conexao = Conexao.con;

            if (conexao != null) {
                String sqlPessoa = "UPDATE pessoa SET nome = ?, endereco = ?, cpf = ?, telefone = ? WHERE id = ?";
                PreparedStatement stmtPessoa = conexao.prepareStatement(sqlPessoa);
                stmtPessoa.setString(1, medicoParceiro.getNome());
                stmtPessoa.setString(2, medicoParceiro.getEndereco());
                stmtPessoa.setString(3, medicoParceiro.getCpf());
                stmtPessoa.setString(4, medicoParceiro.getTelefone());
                stmtPessoa.setInt(5, medicoParceiro.getId());
                stmtPessoa.executeUpdate();

                String sqlMedicoParceiro = "UPDATE medico_parceiro SET crm = ?, estado = ?, habilitado = ?, updated_at = CURRENT_TIMESTAMP WHERE id_pessoa = ?";
                PreparedStatement stmtMedicoParceiro = conexao.prepareStatement(sqlMedicoParceiro);
                stmtMedicoParceiro.setString(1, medicoParceiro.getCrm());
                stmtMedicoParceiro.setString(2, medicoParceiro.getEstado());
                stmtMedicoParceiro.setBoolean(3, medicoParceiro.isHabilitado());
                stmtMedicoParceiro.setInt(4, medicoParceiro.getId());
                stmtMedicoParceiro.executeUpdate();

                System.out.println("Médico Parceiro atualizado com sucesso!");
            } else {
                System.out.println("Erro: conexão com o banco de dados não foi estabelecida.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RemoteException("Erro ao atualizar médico parceiro: " + e.getMessage());
        } finally {
            Conexao.desconectar();
        }
    }

    @Override
    public void desativarMedicoParceiro(int id) throws RemoteException {
        try {
            Conexao.conectar();
            Connection conexao = Conexao.con;

            if (conexao != null) {
                String sqlMedicoParceiro = "UPDATE medico_parceiro SET habilitado = false, updated_at = CURRENT_TIMESTAMP WHERE id_pessoa = ?";
                PreparedStatement stmt = conexao.prepareStatement(sqlMedicoParceiro);
                stmt.setInt(1, id);
                int linhasAfetadas = stmt.executeUpdate();

                if (linhasAfetadas > 0) {
                    System.out.println("Médico Parceiro desativado com sucesso!");
                } else {
                    System.out.println("Nenhum Médico Parceiro encontrado com o ID fornecido.");
                }
            } else {
                System.out.println("Erro: conexão com o banco de dados não foi estabelecida.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RemoteException("Erro ao desativar médico parceiro: " + e.getMessage());
        } finally {
            Conexao.desconectar();
        }
    }

    @Override
    public MedicoParceiro obterMedicoParceiro(Integer id, String crm) throws RemoteException {
        MedicoParceiro medicoParceiro = null;
        try {
            Conexao.conectar();
            Connection conexao = Conexao.con;

            if (conexao != null) {
                String sql = "SELECT p.id, p.nome, p.cpf, p.endereco, p.telefone, "
                        + "m.crm, m.estado, m.habilitado, m.created_at, m.updated_at "
                        + "FROM pessoa p INNER JOIN medico_parceiro m ON p.id = m.id_pessoa WHERE 1=1";
                if (id != null) {
                    sql += " AND p.id = ?";
                }
                if (crm != null && !crm.isEmpty()) {
                    sql += " AND m.crm = ?";
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
                    medicoParceiro = new MedicoParceiro(
                            rs.getInt("id"),
                            rs.getString("nome"),
                            rs.getString("cpf"),
                            rs.getString("endereco"),
                            rs.getString("telefone"),
                            rs.getString("crm"),
                            rs.getString("estado"),
                            rs.getBoolean("habilitado"),
                            rs.getTimestamp("created_at"),
                            rs.getTimestamp("updated_at")
                    );
                }
            } else {
                System.out.println("Erro: conexão com o banco de dados não foi estabelecida.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RemoteException("Erro ao pesquisar médico parceiro: " + e.getMessage());
        } finally {
            Conexao.desconectar();
        }
        return medicoParceiro;
    }

    @Override
    public List<MedicoParceiro> buscarMedicosParceirosPorNome(String nome) throws RemoteException {
        List<MedicoParceiro> medicosParceiros = new ArrayList<>();
        try {
            Conexao.conectar();
            Connection conexao = Conexao.con;

            if (conexao != null) {
                String sql = "SELECT p.id, p.nome, p.cpf, p.endereco, p.telefone, "
                        + "m.crm, m.estado, m.habilitado, m.created_at, m.updated_at "
                        + "FROM pessoa p INNER JOIN medico_parceiro m ON p.id = m.id_pessoa "
                        + "WHERE p.nome LIKE ? AND m.habilitado = 1";
                PreparedStatement stmt = conexao.prepareStatement(sql);
                stmt.setString(1, "%" + nome + "%");

                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    MedicoParceiro medicoParceiro = new MedicoParceiro(
                            rs.getInt("id"),
                            rs.getString("nome"),
                            rs.getString("cpf"),
                            rs.getString("endereco"),
                            rs.getString("telefone"),
                            rs.getString("crm"),
                            rs.getString("estado"),
                            rs.getBoolean("habilitado"),
                            rs.getTimestamp("created_at"),
                            rs.getTimestamp("updated_at")
                    );
                    medicosParceiros.add(medicoParceiro);
                }
            } else {
                System.out.println("Erro: conexão com o banco de dados não foi estabelecida.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RemoteException("Erro ao buscar médicos parceiros por nome: " + e.getMessage());
        } finally {
            Conexao.desconectar();
        }
        return medicosParceiros;
    }

}
