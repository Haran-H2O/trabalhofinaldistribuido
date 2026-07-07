package br.com.utfpr.trabalhofinaldistribuido.loja.provider;

public class CompraResponse {
    private Long pedidoId;
    private String status;
    private String produto;
    private Double valorTotal;
    private String endereco;
    private String mensagem;

    public CompraResponse() {}

    public Long getPedidoId() { return pedidoId; }
    public void setPedidoId(Long pedidoId) { this.pedidoId = pedidoId; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getProduto() { return produto; }
    public void setProduto(String produto) { this.produto = produto; }
    public Double getValorTotal() { return valorTotal; }
    public void setValorTotal(Double valorTotal) { this.valorTotal = valorTotal; }
    public String getEndereco() { return endereco; }
    public void setEndereco(String endereco) { this.endereco = endereco; }
    public String getMensagem() { return mensagem; }
    public void setMensagem(String mensagem) { this.mensagem = mensagem; }
}
