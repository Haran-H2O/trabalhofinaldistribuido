package br.com.utfpr.trabalhofinaldistribuido.mensagens;

import java.util.List;

public class PedidoMessage {
    private Long pedidoId;
    private List<ItemPedido> itens;
    private Double valor;
    private String email;
    private String endereco;
    private Boolean simularErro;

    public PedidoMessage() {}

    public PedidoMessage(Long pedidoId, List<ItemPedido> itens, Double valor, String email, String endereco, Boolean simularErro) {
        this.pedidoId = pedidoId;
        this.itens = itens;
        this.valor = valor;
        this.email = email;
        this.endereco = endereco;
        this.simularErro = simularErro;
    }

    public Long getPedidoId() { return pedidoId; }
    public void setPedidoId(Long pedidoId) { this.pedidoId = pedidoId; }
    public List<ItemPedido> getItens() { return itens; }
    public void setItens(List<ItemPedido> itens) { this.itens = itens; }
    public Double getValor() { return valor; }
    public void setValor(Double valor) { this.valor = valor; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getEndereco() { return endereco; }
    public void setEndereco(String endereco) { this.endereco = endereco; }
    public Boolean getSimularErro() { return simularErro; }
    public void setSimularErro(Boolean simularErro) { this.simularErro = simularErro; }
}
