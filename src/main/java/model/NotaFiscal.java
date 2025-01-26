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
public class NotaFiscal implements Serializable{
    private int id;
    private int num_nota;
    private Timestamp data_emissao;
    private boolean habilitado;
    private Timestamp created_at;
    private Timestamp updated_at;
    

    public NotaFiscal(){
    
    }
    
    public NotaFiscal(int id, int num_nota, Timestamp data_emissao, boolean habilitado, Timestamp created_at, Timestamp updated_at){
        setId(id);
        setNum_nota(num_nota);
        setData_emissao(data_emissao);
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

    public int getNum_nota() {
        return num_nota;
    }

    public void setNum_nota(int num_nota) {
        this.num_nota = num_nota;
    }

    public Timestamp getData_emissao() {
        return data_emissao;
    }

    public void setData_emissao(Timestamp data_emissao) {
        this.data_emissao = data_emissao;
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
