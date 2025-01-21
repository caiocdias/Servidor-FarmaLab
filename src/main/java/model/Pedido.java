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
public class Pedido implements Serializable {
    private int id;
    private String status;
    private boolean habilitado;
    private Timestamp created_at;
    private Timestamp updated_at;
    private Cliente cliente;
    private Funcionario funcionario;
    public Pedido() {
        
    };
    
    public Pedido(int id, String status, boolean habilitado, Timestamp created_at, Timestamp updated_at, Cliente cliente, Funcionario funcionario) {
        setId(id);
        setStatus(status);
        setHabilitado(habilitado);
        setCreated_at(created_at);
        setUpdated_at(updated_at);
        setCliente(cliente);
        setFuncionario(funcionario);
    };
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isHabilitado() {
        return habilitado;
    }

    public void setHabilitado(boolean habilitado) {
        this.habilitado = habilitado;
    }

    public Timestamp getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Timestamp created_at) {
        this.created_at = created_at;
    }

    public Timestamp getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Timestamp updated_at) {
        this.updated_at = updated_at;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        if (cliente != null) {
            this.cliente = cliente; 
        }
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
