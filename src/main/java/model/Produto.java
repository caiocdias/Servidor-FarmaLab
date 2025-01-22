/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 *
 * @author Rafae
 */
public class Produto implements Serializable{
    private int id;
    private Pedido pedido;
    private TipoProduto tipo_produto;
    private boolean pronta_entrega;
    private boolean coletado;
    private Estoque estoque;
    private Timestamp data_validade;
    private boolean habilitado;
    private Timestamp created_at;
    private Timestamp updated_at;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public TipoProduto getTipo_produto() {
        return tipo_produto;
    }

    public void setTipo_produto(TipoProduto tipo_produto) {
        this.tipo_produto = tipo_produto;
    }

    public Estoque getEstoque() {
        return estoque;
    }

    public void setEstoque(Estoque estoque) {
        this.estoque = estoque;
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

    public boolean isPronta_entrega() {
        return pronta_entrega;
    }

    public void setPronta_entrega(boolean pronta_entrega) {
        this.pronta_entrega = pronta_entrega;
    }

    public boolean isColetado() {
        return coletado;
    }

    public void setColetado(boolean coletado) {
        this.coletado = coletado;
    }
}
