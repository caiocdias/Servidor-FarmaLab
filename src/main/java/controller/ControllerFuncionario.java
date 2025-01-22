package controller;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
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
}