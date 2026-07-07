package br.com.utfpr.trabalhofinaldistribuido.loja.provider;

import br.com.utfpr.trabalhofinaldistribuido.cep.provider.Endereco;
import br.com.utfpr.trabalhofinaldistribuido.eventos.provider.Pedido;
import br.com.utfpr.trabalhofinaldistribuido.mensagens.ItemPedido;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class LojaService {

    private static final String BASE = "http://localhost:8080/api";
    private final AtomicLong pedidoCounter = new AtomicLong(1000);
    private final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private MongoTemplate mongoTemplate;

    public Endereco consultarCep(String cep) {
        System.out.println("[HTTP] Loja Web consultando CEP " + cep + " no CEP Service");
        HttpResponse<String> resp = Unirest.get(BASE + "/cep/{cep}").routeParam("cep", cep).asString();
        if (resp.getStatus() == 404) {
            return null;
        }
        try {
            return mapper.readValue(resp.getBody(), Endereco.class);
        } catch (Exception e) {
            return null;
        }
    }

    public CompraResponse realizarCompra(CompraRequest request) {
        Long pedidoId = pedidoCounter.incrementAndGet();
        try {
            if (request.getItens() == null || request.getItens().isEmpty()) {
                throw new RuntimeException("Nenhum item informado para a compra");
            }

            List<ItemPedido> itensPedido = new ArrayList<>();
            List<String> resumoProdutos = new ArrayList<>();
            double valorTotal = 0;

            for (ItemCompraRequest item : request.getItens()) {
                String produtoBody = Unirest.get(BASE + "/produtos/{id}")
                    .routeParam("id", item.getProdutoId())
                    .asString().getBody();
                JsonNode produtoJson = mapper.readTree(produtoBody);

                if (produtoJson.has("erro")) {
                    throw new RuntimeException("Produto não encontrado: " + item.getProdutoId());
                }

                String nomeProduto = produtoJson.get("nome").asText();
                double preco = produtoJson.get("preco").asDouble();
                valorTotal += preco * item.getQuantidade();

                itensPedido.add(new ItemPedido(item.getProdutoId(), item.getQuantidade(), preco));
                resumoProdutos.add(nomeProduto + " x" + item.getQuantidade());
            }

            Endereco endereco = consultarCep(request.getCep());
            String enderecoStr = endereco != null
                ? endereco.getLogradouro() + ", " + endereco.getBairro() + " - " + endereco.getLocalidade() + "/" + endereco.getUf()
                : "CEP " + request.getCep() + " (não localizado)";

            double valorPagamento = Boolean.TRUE.equals(request.getSimularErro()) ? -valorTotal : valorTotal;

            Pedido pedido = new Pedido();
            pedido.setPedidoId(pedidoId);
            pedido.setItens(itensPedido);
            pedido.setValor(valorPagamento);
            pedido.setEmail(request.getEmail());
            pedido.setEndereco(enderecoStr);
            pedido.setSimularErro(request.getSimularErro());
            pedido.setStatus("RECEBIDO");
            pedido.setCriadoEm(LocalDateTime.now());
            pedido.setAtualizadoEm(LocalDateTime.now());
            mongoTemplate.insert(pedido);

            String resumo = String.join(", ", resumoProdutos);
            System.out.println("COMPRA REGISTRADA NO MONGODB: pedido #" + pedidoId + " | " + resumo + " | R$ " + String.format("%.2f", valorTotal));

            CompraResponse response = new CompraResponse();
            response.setPedidoId(pedidoId);
            response.setStatus("PROCESSANDO");
            response.setProduto(resumo);
            response.setValorTotal(valorTotal);
            response.setEndereco(enderecoStr);
            response.setMensagem("Pedido recebido. O restante do fluxo é processado de forma assíncrona a partir do evento gerado no MongoDB.");
            return response;

        } catch (Exception e) {
            System.out.println("ERRO no fluxo de compra pedido #" + pedidoId + ": " + e.getMessage());
            CompraResponse response = new CompraResponse();
            response.setPedidoId(pedidoId);
            response.setStatus("ERRO");
            response.setMensagem(e.getMessage());
            return response;
        }
    }
}
