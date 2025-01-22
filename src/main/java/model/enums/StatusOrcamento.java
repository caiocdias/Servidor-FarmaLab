/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package model.enums;

/**
 *
 * @author Rafae
 */
public enum StatusOrcamento {
    EM_ANALISE("Em análise"),
    APROVADO("Aprovado. Produto cadastrado!"),
    APROVADO_PARCIALMENTE("Aprovado parcialmente. Leia as observações!"),
    REJEITADO("Rejeitado.");
    
    private final String descricao;
    
    StatusOrcamento(String descricao) {
        this.descricao = descricao;
    }
    
    public String getDescricao() {
        return descricao;
    }
}
