package br.com.utfpr.trabalhofinaldistribuido.loja.provider;

import java.util.List;

public class CompraRequest {
    private List<ItemCompraRequest> itens;
    private String cep;
    private String email;
    private String cartao;
    private Boolean simularErro;

    public CompraRequest() {}

    public List<ItemCompraRequest> getItens() { return itens; }
    public void setItens(List<ItemCompraRequest> itens) { this.itens = itens; }
    public String getCep() { return cep; }
    public void setCep(String cep) { this.cep = cep; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getCartao() { return cartao; }
    public void setCartao(String cartao) { this.cartao = cartao; }
    public Boolean getSimularErro() { return simularErro; }
    public void setSimularErro(Boolean simularErro) { this.simularErro = simularErro; }
}
