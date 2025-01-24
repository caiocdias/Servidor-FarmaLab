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
public class Venda implements Serializable {
    private int id;
    private Unidade unidade;
    private Pedido pedido;
    private NotaFiscal nota_fiscal;
    private boolean habilitado;
    private Timestamp created_at;
    private Timestamp updated_at;

    public Venda() {
        
    }
    
    public Venda(int id, Unidade unidade, NotaFiscal nota_fiscal, Pedido pedido,Boolean habilitado, Timestamp created_at, Timestamp updated_at) {
        setId(id);
        setUnidade(unidade);
        setNota_fiscal(nota_fiscal);
        setPedido(pedido);
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

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public NotaFiscal getNota_fiscal() {
        return nota_fiscal;
    }

    public void setNota_fiscal(NotaFiscal nota_fiscal) {
        this.nota_fiscal = nota_fiscal;
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

    public boolean isHabilitado() {
        return habilitado;
    }

    public void setHabilitado(boolean habilitado) {
        this.habilitado = habilitado;
    }
}
