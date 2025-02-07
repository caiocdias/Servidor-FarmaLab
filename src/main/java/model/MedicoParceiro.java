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
public class MedicoParceiro extends Pessoa implements Serializable {
    private String crm;
    private String estado;
    private boolean habilitado;
    private Timestamp created_at;
    private Timestamp updated_at;

    public MedicoParceiro() {
        
    }
    public MedicoParceiro(int id, String nome, String cpf, String endereco, String telefone, String crm, String estado, boolean habilitado, Timestamp created_at, Timestamp updated_at) {
        super(id, nome, cpf, endereco, telefone);
        setCrm(crm);
        setEstado(estado);
        setHabilitado(habilitado);
        setCreated_at(created_at);
        setUpdated_at(updated_at);
    }
    public String getCrm() {
        return crm;
    }

    public void setCrm(String crm) {
        this.crm = crm;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
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

    public Timestamp getUpdated_at() {
        return updated_at;
    }

    public void setCreated_at(Timestamp created_at) {
        this.created_at = created_at;
    }

    public void setUpdated_at(Timestamp updated_at) {
        this.updated_at = updated_at;
    }
}
