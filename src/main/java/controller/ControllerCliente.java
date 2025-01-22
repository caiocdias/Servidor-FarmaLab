package controller;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import model.Cliente;
import util.Conexao;

public class ControllerCliente extends UnicastRemoteObject implements InterfaceCliente {

    public ControllerCliente() throws RemoteException {
        super();
    }

    @Override
    public void inserirCliente(String nome, String endereco, String cpf, String telefone, boolean habilitado) throws RemoteException {
        try {
            Conexao.conectar();
            Connection conexao = Conexao.con;

            if (conexao != null) {
                String sqlPessoa = "INSERT INTO pessoa (nome, endereco, cpf, telefone) VALUES (?, ?, ?, ?)";
                PreparedStatement stmtPessoa = conexao.prepareStatement(sqlPessoa, Statement.RETURN_GENERATED_KEYS);
                stmtPessoa.setString(1, nome);
                stmtPessoa.setString(2, endereco);
                stmtPessoa.setString(3, cpf);
                stmtPessoa.setString(4, telefone);
                stmtPessoa.executeUpdate();

                ResultSet rs = stmtPessoa.getGeneratedKeys();
                int idPessoa = 0;
                if (rs.next()) {
                    idPessoa = rs.getInt(1);
                }

                String sqlCliente = "INSERT INTO cliente (id_pessoa, habilitado) VALUES (?, ?)";
                PreparedStatement stmtCliente = conexao.prepareStatement(sqlCliente);
                stmtCliente.setInt(1, idPessoa);
                stmtCliente.setBoolean(2, habilitado);
                stmtCliente.executeUpdate();

                System.out.println("Cliente inserido com sucesso!");
            } else {
                System.out.println("Erro: conexão com o banco de dados não foi estabelecida.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RemoteException("Erro ao inserir cliente: " + e.getMessage());
        } finally {
            Conexao.desconectar();
        }
    }
    
    @Override
    public Cliente obterCliente(int id) throws RemoteException {
        Cliente cliente = null;
        try {
            Conexao.conectar();
            Connection conexao = Conexao.con;

            if (conexao != null) {
                String sql = "SELECT p.id, p.nome, p.cpf, p.endereco, p.telefone, c.habilitado, c.created_at, c.updated_at " +
                             "FROM pessoa p INNER JOIN cliente c ON p.id = c.id_pessoa WHERE p.id = ?";
                PreparedStatement stmt = conexao.prepareStatement(sql);
                stmt.setInt(1, id);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    cliente = new Cliente(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("cpf"),
                        rs.getString("endereco"),
                        rs.getString("telefone"),
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
            throw new RemoteException("Erro ao obter cliente: " + e.getMessage());
        } finally {
            Conexao.desconectar();
        }
        return cliente;
    }
    
    @Override
    public void atualizarCliente(Cliente cliente) throws RemoteException {
        try {
            Conexao.conectar();
            Connection conexao = Conexao.con;

            if (conexao != null) {
                String sqlPessoa = "UPDATE pessoa SET nome = ?, endereco = ?, cpf = ?, telefone = ? WHERE id = ?";
                PreparedStatement stmtPessoa = conexao.prepareStatement(sqlPessoa);
                stmtPessoa.setString(1, cliente.getNome());
                stmtPessoa.setString(2, cliente.getEndereco());
                stmtPessoa.setString(3, cliente.getCpf());
                stmtPessoa.setString(4, cliente.getTelefone());
                stmtPessoa.setInt(5, cliente.getId());
                stmtPessoa.executeUpdate();

                String sqlCliente = "UPDATE cliente SET habilitado = ?, updated_at = CURRENT_TIMESTAMP WHERE id_pessoa = ?";
                PreparedStatement stmtCliente = conexao.prepareStatement(sqlCliente);
                stmtCliente.setBoolean(1, cliente.isHabilitado());
                stmtCliente.setInt(2, cliente.getId());
                stmtCliente.executeUpdate();

                System.out.println("Cliente atualizado com sucesso!");
            } else {
                System.out.println("Erro: conexão com o banco de dados não foi estabelecida.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RemoteException("Erro ao atualizar cliente: " + e.getMessage());
        } finally {
            Conexao.desconectar();
        }
    }
}
