package br.com.utfpr.trabalhofinaldistribuido.mensagens;

import java.util.List;

public class FiscalMessage {
    private Long pedidoId;
    private List<ItemPedido> itens;
    private Double valorTotal;
    private String email;

    public FiscalMessage() {}

    public FiscalMessage(Long pedidoId, List<ItemPedido> itens, Double valorTotal, String email) {
        this.pedidoId = pedidoId;
        this.itens = itens;
        this.valorTotal = valorTotal;
        this.email = email;
    }

    public Long getPedidoId() { return pedidoId; }
    public void setPedidoId(Long pedidoId) { this.pedidoId = pedidoId; }
    public List<ItemPedido> getItens() { return itens; }
    public void setItens(List<ItemPedido> itens) { this.itens = itens; }
    public Double getValorTotal() { return valorTotal; }
    public void setValorTotal(Double valorTotal) { this.valorTotal = valorTotal; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}
