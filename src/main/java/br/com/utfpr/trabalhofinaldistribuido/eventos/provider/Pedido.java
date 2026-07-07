package br.com.utfpr.trabalhofinaldistribuido.eventos.provider;

import br.com.utfpr.trabalhofinaldistribuido.mensagens.ItemPedido;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "pedidos")
public class Pedido {

    @Id
    private String id;
    private Long pedidoId;
    private List<ItemPedido> itens;
    private Double valor;
    private String email;
    private String endereco;
    private Boolean simularErro;
    private String status;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;

    public Pedido() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
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
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getCriadoEm() { return criadoEm; }
    public void setCriadoEm(LocalDateTime criadoEm) { this.criadoEm = criadoEm; }
    public LocalDateTime getAtualizadoEm() { return atualizadoEm; }
    public void setAtualizadoEm(LocalDateTime atualizadoEm) { this.atualizadoEm = atualizadoEm; }
}
