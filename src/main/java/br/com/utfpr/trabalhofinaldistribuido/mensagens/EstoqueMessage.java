package br.com.utfpr.trabalhofinaldistribuido.mensagens;

import java.util.List;

public class EstoqueMessage {
    private Long pedidoId;
    private List<ItemPedido> itens;

    public EstoqueMessage() {}

    public EstoqueMessage(Long pedidoId, List<ItemPedido> itens) {
        this.pedidoId = pedidoId;
        this.itens = itens;
    }

    public Long getPedidoId() { return pedidoId; }
    public void setPedidoId(Long pedidoId) { this.pedidoId = pedidoId; }
    public List<ItemPedido> getItens() { return itens; }
    public void setItens(List<ItemPedido> itens) { this.itens = itens; }
}
