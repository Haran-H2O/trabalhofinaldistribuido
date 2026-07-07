package br.com.utfpr.trabalhofinaldistribuido.mensagens;

public class CompraConcluidaMessage {
    private Long pedidoId;

    public CompraConcluidaMessage() {}

    public CompraConcluidaMessage(Long pedidoId) {
        this.pedidoId = pedidoId;
    }

    public Long getPedidoId() { return pedidoId; }
    public void setPedidoId(Long pedidoId) { this.pedidoId = pedidoId; }
}
