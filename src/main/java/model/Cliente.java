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
public class Cliente extends Pessoa implements Serializable {
    private boolean habilitado;
    private Timestamp created_at;
    private Timestamp updated_at;

    public Cliente(int id, String nome, String cpf, String endereco, String telefone, boolean habilitado) {
        super(id, nome, cpf, endereco, telefone);
        setHabilitado(habilitado);
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
}
