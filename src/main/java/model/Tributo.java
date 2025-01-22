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
public class Tributo implements Serializable{
    private int id;
    private String estado;
    private String nome_imposto;
    private float porcentagem;
    private boolean habilitado;
    private Timestamp created_at;
    private Timestamp updated_at;
    
    public Tributo(){
        
    };
    
    public Tributo(int id, String estado, String nome, String nome_imposto, float porcentagem, boolean habilitado){
        setId(id);
        setEstado(estado);
        setNome_imposto(nome_imposto);
        setPorcentagem(porcentagem);
        setHabilitado(habilitado);
        setCreated_at(created_at);
        setUpdated_at(updated_at);
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getNome_imposto() {
        return nome_imposto;
    }

    public void setNome_imposto(String nome_imposto) {
        this.nome_imposto = nome_imposto;
    }

    public float getPorcentagem() {
        return porcentagem;
    }

    public void setPorcentagem(float porcentagem) {
        this.porcentagem = porcentagem;
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
