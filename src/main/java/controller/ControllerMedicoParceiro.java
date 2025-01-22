package controller;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
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
}
