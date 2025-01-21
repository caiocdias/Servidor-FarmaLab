package model.enums;

/**
 *
 * @author Caio Cezar Dias
 */
public enum StatusPedido {
    AGUARDANDO_PAGAMENTO("Aguardando Pagamento"),
    PRONTO_PARA_PRODUCAO("Pronto para Produção"),
    EM_ANDAMENTO("Em andamento"),
    PARA_RETIRADA("Para retirada"),
    CONCLUIDO("Concluído");
    
    private final String descricao;
    
    StatusPedido(String descricao) {
        this.descricao = descricao;
    }
    
    public String getDescricao() {
        return descricao;
    }
}