/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;
import java.rmi.RemoteException;
import model.Pessoa;
import util.Conexao;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.rmi.server.UnicastRemoteObject;

/**
 *
 * @author Caio Cezar Dias
 */
public class PessoaController extends UnicastRemoteObject implements InterfacePessoa {
    
    public PessoaController() throws RemoteException{}
    
    @Override
    public void inserirPessoa(Pessoa p) throws RemoteException {
        
        String sql = "insert into Pessoa (nome,endereco,cpf) values (?,?,?)";
        Conexao.conectar();
        try{
            PreparedStatement sentenca = Conexao.con.prepareStatement(sql);
            sentenca.setString(1, p.getNome());
            sentenca.setString(2, p.getEndereco());
            sentenca.setString(3, p.getCpf());
            /* if(!sentenca.execute())
                retorno = true; */
        }catch(SQLException e){
            System.out.println(e.getMessage());
        } 
        Conexao.desconectar();
        
    }

    @Override
    public Pessoa getPessoa(Pessoa p) throws RemoteException {
        p.setNome("NomeMudado");
        System.out.println("nomeNoServer: " + p.getNome());
        return p;
    }
    
}
