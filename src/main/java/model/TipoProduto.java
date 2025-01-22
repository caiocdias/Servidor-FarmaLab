/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Isabely
 */
public class TipoProduto implements Serializable {
    private int id;
    private String nome;
    private String instrucoes;
    private boolean habilitado;
    private Timestamp created_at;
    private Timestamp updated_at;
    private List<TipoInsumo> tipo_insumos = new ArrayList<>();
    
    public TipoProduto(){
        
    }
    
    public TipoProduto(int id, String nome, boolean habilitado, Timestamp created_at, Timestamp updated_at, List<TipoInsumo> tipo_insumos){
        setId(id);
        setNome(nome);
        setInstrucoes(instrucoes);
        setHabilitado(habilitado);
        setCreated_at(created_at);
        setUpdated_at(updated_at);
        setTipo_insumos(tipo_insumos);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
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

    public String getInstrucoes() {
        return instrucoes;
    }

    public void setInstrucoes(String instrucoes) {
        this.instrucoes = instrucoes;
    }

    public List<TipoInsumo> getTipo_insumos() {
        return tipo_insumos;
    }

    public void setTipo_insumos(List<TipoInsumo> tipo_insumos) {
        this.tipo_insumos = tipo_insumos;
    }
}
