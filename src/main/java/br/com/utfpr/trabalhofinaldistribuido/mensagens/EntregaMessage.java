package br.com.utfpr.trabalhofinaldistribuido.mensagens;

public class EntregaMessage {
    private Long pedidoId;
    private String endereco;
    private String email;

    public EntregaMessage() {}

    public EntregaMessage(Long pedidoId, String endereco, String email) {
        this.pedidoId = pedidoId;
        this.endereco = endereco;
        this.email = email;
    }

    public Long getPedidoId() { return pedidoId; }
    public void setPedidoId(Long pedidoId) { this.pedidoId = pedidoId; }
    public String getEndereco() { return endereco; }
    public void setEndereco(String endereco) { this.endereco = endereco; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}
