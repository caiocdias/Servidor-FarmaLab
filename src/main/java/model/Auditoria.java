/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;
import java.io.Serializable;
import java.sql.Timestamp;
/**
 *
 * @author Caio Cezar Dias
 */
public class Auditoria implements Serializable {
    private int id;
    private String movimento;
    private Timestamp data;
    private Funcionario funcionario;

    public Auditoria() {
        
    };
    
    public Auditoria(int id, String movimento, Timestamp data, Funcionario funcionario) {
        setId(id);
        setMovimento(movimento);
        setData(data);
        setFuncionario(funcionario);
    };
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    };
    
    public String getMovimento() {
        return movimento;
    }

    public void setMovimento(String movimento) {
        this.movimento = movimento;
    }

    public Timestamp getData() {
        return data;
    }

    public void setData(Timestamp data) {
        this.data = data;
    }

    public Funcionario getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(Funcionario funcionario) {
        if (funcionario != null) {
            this.funcionario = funcionario; 
        }
    }
}