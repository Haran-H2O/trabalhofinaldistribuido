package br.com.utfpr.trabalhofinaldistribuido.mensagens;

public class EstoqueMessage {
    private Long pedidoId;
    private String produtoId;
    private Integer quantidade;

    public EstoqueMessage() {}

    public EstoqueMessage(Long pedidoId, String produtoId, Integer quantidade) {
        this.pedidoId = pedidoId;
        this.produtoId = produtoId;
        this.quantidade = quantidade;
    }

    public Long getPedidoId() { return pedidoId; }
    public void setPedidoId(Long pedidoId) { this.pedidoId = pedidoId; }
    public String getProdutoId() { return produtoId; }
    public void setProdutoId(String produtoId) { this.produtoId = produtoId; }
    public Integer getQuantidade() { return quantidade; }
    public void setQuantidade(Integer quantidade) { this.quantidade = quantidade; }
}
