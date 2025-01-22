/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.io.Serializable;
import java.sql.Timestamp;
import model.enums.StatusOrcamento;

/**
 *
 * @author Rafae
 */
public class Orcamento implements Serializable{
    private int id;
    private Unidade unidade;
    private Cliente cliente;
    private Funcionario funcionario;
    private StatusOrcamento status;
    private String descricao;
    private String observacoes;
    private boolean habilitado;
    private Timestamp created_at;
    private Timestamp updated_at;

    public Orcamento(){}
    
    public Orcamento(int id, Unidade unidade, Cliente cliente, Funcionario funcionario, StatusOrcamento status, String descricao, String observacoes, boolean habilitado, Timestamp created_at, Timestamp updated_at){
        setId(id);
        setUnidade(unidade);
        setCliente(cliente);
        setFuncionario(funcionario);
        setStatus(status);
        setDescricao(descricao);
        setObservacoes(observacoes);
        setHabilitado(habilitado);
        setCreated_at(created_at);
        setUpdated_at(updated_at);
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Unidade getUnidade() {
        return unidade;
    }

    public void setUnidade(Unidade unidade) {
        this.unidade = unidade;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Funcionario getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
    }

    public StatusOrcamento getStatus() {
        return status;
    }

    public void setStatus(StatusOrcamento status) {
        this.status = status;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
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
}
