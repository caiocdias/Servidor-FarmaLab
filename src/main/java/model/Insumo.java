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
public class Insumo implements Serializable {
    private int id;
    private float quant;
    private Timestamp data_validade;
    private boolean habilitado;
    private Estoque estoque;
    private TipoInsumo tipo_insumo;
    private Timestamp created_at;
    private Timestamp updated_at;
    
    public Insumo() {
        
    }
    
    public Insumo(int id, float quant, Timestamp data_validade, boolean habilitado, Estoque estoque, TipoInsumo tipo_insumo, Timestamp created_at, Timestamp updated_at){
        setId(id);
        setQuant(quant);
        setData_validade(data_validade);
        setHabilitado(habilitado);
        setEstoque(estoque);
        setTipo_insumo(tipo_insumo);
        setCreated_at(created_at);
        setUpdated_at(updated_at);
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getQuant() {
        return quant;
    }

    public void setQuant(float quant) {
        this.quant = quant;
    }

    public Timestamp getData_validade() {
        return data_validade;
    }

    public void setData_validade(Timestamp data_validade) {
        this.data_validade = data_validade;
    }

    public boolean isHabilitado() {
        return habilitado;
    }

    public void setHabilitado(boolean habilitado) {
        this.habilitado = habilitado;
    }

    public Estoque getEstoque() {
        return estoque;
    }

    public void setEstoque(Estoque estoque) {
        this.estoque = estoque;
    }

    public TipoInsumo getTipo_insumo() {
        return tipo_insumo;
    }

    public void setTipo_insumo(TipoInsumo tipo_insumo) {
        this.tipo_insumo = tipo_insumo;
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
