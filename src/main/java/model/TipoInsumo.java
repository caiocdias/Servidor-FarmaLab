/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 *
 * @author Isabely
 */
public class TipoInsumo implements Serializable{
    private int id;
    private String nome;
    private float quant;
    private boolean habilitado;
    private Timestamp created_at;
    private Timestamp updated_at;
    
    public TipoInsumo(){
        
    }
    
    public TipoInsumo(int id, String nome, float quant, boolean habilitado, Timestamp created_at, Timestamp updated_at){
        setId(id);
        setNome(nome);
        setHabilitado(habilitado);
        setQuant(quant);
        setCreated_at(created_at);
        setUpdated_at(updated_at);
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

    public float getQuant() {
        return quant;
    }

    public void setQuant(float quant) {
        this.quant = quant;
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
