package controller;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.PreparedStatement;
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
                // Inserir dados na tabela 'prescricao'
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
}
