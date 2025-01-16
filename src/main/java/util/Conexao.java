/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Caio Cezar Dias
 */
public class Conexao {
    public static Connection con;
    public static void conectar(){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
        }catch(ClassNotFoundException e){
            System.out.println("Driver nÃ£o encontrado: "+ e.getMessage());
        }
        String url = "jdbc:mysql://localhost:/vendas";
        String usuario = "root";
        String senha = "root";
        try{
            con = DriverManager.getConnection(url, usuario, senha);
        }catch(SQLException e){
            System.out.println("Erro ao conectar com o BD: "+ e.getMessage());
        }
    }
    public static void desconectar(){
        try{
            con.close();
        }catch(SQLException e){
            System.out.println("Erro ao encerrar conexÃ£o: "+ e.getMessage());
        }
    }
}
