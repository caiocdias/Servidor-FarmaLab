package controller;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import model.Funcionario;
import util.Conexao;

public class ControllerFuncionario extends UnicastRemoteObject implements InterfaceFuncionario {

    public ControllerFuncionario() throws RemoteException {
        super();
    }

    @Override
    public void inserirFuncionario(Funcionario funcionario) throws RemoteException {
        try {
            Conexao.conectar();
            Connection conexao = Conexao.con;

            if (conexao != null) {
                String sqlPessoa = "INSERT INTO pessoa (nome, endereco, cpf, telefone) VALUES (?, ?, ?, ?)";
                PreparedStatement stmtPessoa = conexao.prepareStatement(sqlPessoa, Statement.RETURN_GENERATED_KEYS);
                stmtPessoa.setString(1, funcionario.getNome());
                stmtPessoa.setString(2, funcionario.getEndereco());
                stmtPessoa.setString(3, funcionario.getCpf());
                stmtPessoa.setString(4, funcionario.getTelefone());
                stmtPessoa.executeUpdate();

                ResultSet rs = stmtPessoa.getGeneratedKeys();
                int idPessoa = 0;
                if (rs.next()) {
                    idPessoa = rs.getInt(1);
                }

                String sqlFuncionario = "INSERT INTO funcionario (id_pessoa, cargo, password, salario, habilitado, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement stmtFuncionario = conexao.prepareStatement(sqlFuncionario);
                stmtFuncionario.setInt(1, idPessoa);
                stmtFuncionario.setString(2, funcionario.getCargo());
                stmtFuncionario.setString(3, funcionario.getPassword());
                stmtFuncionario.setFloat(4, funcionario.getSalario());
                stmtFuncionario.setBoolean(5, funcionario.isHabilitado());
                stmtFuncionario.setTimestamp(6, funcionario.getCreated_at());
                stmtFuncionario.setTimestamp(7, funcionario.getUpdated_at());
                stmtFuncionario.executeUpdate();

                System.out.println("Funcionário inserido com sucesso!");
            } else {
                System.out.println("Erro: conexão com o banco de dados não foi estabelecida.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RemoteException("Erro ao inserir funcionário: " + e.getMessage());
        } finally {
            Conexao.desconectar();
        }
    }

    @Override
    public void atualizarFuncionario(Funcionario funcionario) throws RemoteException {
        try {
            Conexao.conectar();
            Connection conexao = Conexao.con;

            if (conexao != null) {
                String sqlPessoa = "UPDATE pessoa SET nome = ?, endereco = ?, cpf = ?, telefone = ? WHERE id = ?";
                PreparedStatement stmtPessoa = conexao.prepareStatement(sqlPessoa);
                stmtPessoa.setString(1, funcionario.getNome());
                stmtPessoa.setString(2, funcionario.getEndereco());
                stmtPessoa.setString(3, funcionario.getCpf());
                stmtPessoa.setString(4, funcionario.getTelefone());
                stmtPessoa.setInt(5, funcionario.getId());
                stmtPessoa.executeUpdate();

                String sqlFuncionario = "UPDATE funcionario SET cargo = ?, password = ?, salario = ?, habilitado = ?, updated_at = CURRENT_TIMESTAMP WHERE id_pessoa = ?";
                PreparedStatement stmtFuncionario = conexao.prepareStatement(sqlFuncionario);
                stmtFuncionario.setString(1, funcionario.getCargo());
                stmtFuncionario.setString(2, funcionario.getPassword());
                stmtFuncionario.setFloat(3, funcionario.getSalario());
                stmtFuncionario.setBoolean(4, funcionario.isHabilitado());
                stmtFuncionario.setInt(5, funcionario.getId());
                stmtFuncionario.executeUpdate();

                System.out.println("Funcionário atualizado com sucesso!");
            } else {
                System.out.println("Erro: conexão com o banco de dados não foi estabelecida.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RemoteException("Erro ao atualizar funcionário: " + e.getMessage());
        } finally {
            Conexao.desconectar();
        }
    }

    @Override
    public void desativarFuncionario(int id) throws RemoteException {
        try {
            Conexao.conectar();
            Connection conexao = Conexao.con;

            if (conexao != null) {
                String sqlFuncionario = "UPDATE funcionario SET habilitado = false, updated_at = CURRENT_TIMESTAMP WHERE id_pessoa = ?";
                PreparedStatement stmt = conexao.prepareStatement(sqlFuncionario);
                stmt.setInt(1, id);
                int linhasAfetadas = stmt.executeUpdate();

                if (linhasAfetadas > 0) {
                    System.out.println("Funcionário desativado com sucesso!");
                } else {
                    System.out.println("Nenhum funcionário encontrado com o ID fornecido.");
                }
            } else {
                System.out.println("Erro: conexão com o banco de dados não foi estabelecida.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RemoteException("Erro ao desativar funcionário: " + e.getMessage());
        } finally {
            Conexao.desconectar();
        }
    }

    @Override
    public Funcionario obterFuncionario(Integer id, String cpf) throws RemoteException {
        Funcionario funcionario = null;
        try {
            Conexao.conectar();
            Connection conexao = Conexao.con;

            if (conexao != null) {
                String sql = "SELECT p.id, p.nome, p.cpf, p.endereco, p.telefone, "
                        + "f.cargo, f.password, f.salario, f.habilitado, f.created_at, f.updated_at "
                        + "FROM pessoa p INNER JOIN funcionario f ON p.id = f.id_pessoa WHERE 1=1";
                if (id != null) {
                    sql += " AND p.id = ?";
                }
                if (cpf != null && !cpf.isEmpty()) {
                    sql += " AND p.cpf = ?";
                }

                PreparedStatement stmt = conexao.prepareStatement(sql);

                int paramIndex = 1;
                if (id != null) {
                    stmt.setInt(paramIndex++, id);
                }
                if (cpf != null && !cpf.isEmpty()) {
                    stmt.setString(paramIndex++, cpf);
                }

                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    funcionario = new Funcionario(
                            rs.getInt("id"),
                            rs.getString("nome"),
                            rs.getString("cpf"),
                            rs.getString("endereco"),
                            rs.getString("telefone"),
                            rs.getString("cargo"),
                            rs.getString("password"),
                            rs.getFloat("salario"),
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
            throw new RemoteException("Erro ao pesquisar funcionário: " + e.getMessage());
        } finally {
            Conexao.desconectar();
        }
        return funcionario;
    }

    @Override
    public List<Funcionario> buscarFuncionariosPorNome(String nome) throws RemoteException {
        List<Funcionario> funcionarios = new ArrayList<>();
        try {
            Conexao.conectar();
            Connection conexao = Conexao.con;

            if (conexao != null) {
                String sql = "SELECT p.id, p.nome, p.cpf, p.endereco, p.telefone, "
                        + "f.cargo, f.password, f.salario, f.habilitado, f.created_at, f.updated_at "
                        + "FROM pessoa p INNER JOIN funcionario f ON p.id = f.id_pessoa "
                        + "WHERE p.nome LIKE ? AND f.habilitado = 1";
                PreparedStatement stmt = conexao.prepareStatement(sql);
                stmt.setString(1, "%" + nome + "%");

                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    Funcionario funcionario = new Funcionario(
                            rs.getInt("id"),
                            rs.getString("nome"),
                            rs.getString("cpf"),
                            rs.getString("endereco"),
                            rs.getString("telefone"),
                            rs.getString("cargo"),
                            rs.getString("password"),
                            rs.getFloat("salario"),
                            rs.getBoolean("habilitado"),
                            rs.getTimestamp("created_at"),
                            rs.getTimestamp("updated_at")
                    );
                    funcionarios.add(funcionario);
                }
            } else {
                System.out.println("Erro: conexão com o banco de dados não foi estabelecida.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RemoteException("Erro ao buscar funcionários por nome: " + e.getMessage());
        } finally {
            Conexao.desconectar();
        }
        return funcionarios;
    }

    @Override
    public boolean autenticarFuncionario(String cpf, String senha) throws RemoteException {
        boolean autenticado = false;

        try {
            Conexao.conectar();
            Connection conexao = Conexao.con;

            if (conexao != null) {
                String sql = "SELECT * FROM funcionario f INNER JOIN pessoa p ON f.id_pessoa = p.id WHERE p.cpf = ? AND f.password = ?";
                PreparedStatement stmt = conexao.prepareStatement(sql);
                stmt.setString(1, cpf);
                stmt.setString(2, senha);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    autenticado = true;
                }
            } else {
                System.out.println("Erro: conexão com o banco de dados não foi estabelecida.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RemoteException("Erro ao autenticar funcionário: " + e.getMessage());
        } finally {
            Conexao.desconectar();
        }

        return autenticado;
    }

}
