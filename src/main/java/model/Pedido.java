/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.io.Serializable;
import java.sql.Timestamp;
import model.enums.StatusPedido;

/**
 *
 * @author Caio Cezar Dias
 */
public class Pedido implements Serializable {
    private int id;
    private StatusPedido status;
    private boolean habilitado;
    private boolean pronta_entrega;
    private Timestamp created_at;
    private Timestamp updated_at;
    private Cliente cliente;
    private Funcionario funcionario;
    private Prescricao prescricao;
    private float descontoTotal;
    private float valorTotalBase;
    private float valorFinal;
    
    public Pedido() {
        
    }
    
    public Pedido(int id, StatusPedido status_pedido, boolean habilitado, boolean pronta_entrega, float descontoTotal,float valorTotalBase,float valorFinal, Timestamp created_at, Timestamp updated_at, Cliente cliente, Funcionario funcionario, Prescricao prescricao) {
        setId(id);
        setStatus(status);
        setHabilitado(habilitado);
        setPronta_entrega(pronta_entrega);
        setCreated_at(created_at);
        setUpdated_at(updated_at);
        setCliente(cliente);
        setFuncionario(funcionario);
        setPrescricao(prescricao);
        setDescontoTotal(descontoTotal);
        setValorTotalBase(valorTotalBase);
        setValorFinal(valorFinal);
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public StatusPedido getStatus() {
        return status;
    }

    public void setStatus(StatusPedido status) {
        this.status = status;
    }

    public boolean isPronta_entrega() {
        return pronta_entrega;
    }

    public void setPronta_entrega(boolean pronta_entrega) {
        this.pronta_entrega = pronta_entrega;
    }

    public Prescricao getPrescricao() {
        return prescricao;
    }

    public void setPrescricao(Prescricao prescricao) {
        this.prescricao = prescricao;
    }

    public float getDescontoTotal() {
        return descontoTotal;
    }

    public void setDescontoTotal(float descontoTotal) {
        this.descontoTotal = descontoTotal;
    }

    public float getValorTotalBase() {
        return valorTotalBase;
    }

    public void setValorTotalBase(float valorTotalBase) {
        this.valorTotalBase = valorTotalBase;
    }

    public float getValorFinal() {
        return valorFinal;
    }

    public void setValorFinal(float valorFinal) {
        this.valorFinal = valorFinal;
    }
    
}
