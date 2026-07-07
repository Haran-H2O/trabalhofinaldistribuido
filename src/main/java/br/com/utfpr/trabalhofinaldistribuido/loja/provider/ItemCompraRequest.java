package br.com.utfpr.trabalhofinaldistribuido.loja.provider;

public class ItemCompraRequest {
    private String produtoId;
    private Integer quantidade;

    public ItemCompraRequest() {}

    public String getProdutoId() { return produtoId; }
    public void setProdutoId(String produtoId) { this.produtoId = produtoId; }
    public Integer getQuantidade() { return quantidade; }
    public void setQuantidade(Integer quantidade) { this.quantidade = quantidade; }
}
