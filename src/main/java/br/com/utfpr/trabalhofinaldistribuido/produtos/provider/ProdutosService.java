package br.com.utfpr.trabalhofinaldistribuido.produtos.provider;

import br.com.utfpr.trabalhofinaldistribuido.mensagens.CompraConcluidaMessage;
import br.com.utfpr.trabalhofinaldistribuido.mensagens.EstoqueMessage;
import br.com.utfpr.trabalhofinaldistribuido.mensagens.ItemPedido;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProdutosService {

    private final List<Produto> produtos = new ArrayList<>();
    private final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @PostConstruct
    public void carregarProdutos() {
        try (InputStream is = getClass().getResourceAsStream("/produtos.json")) {
            produtos.addAll(mapper.readValue(is, new TypeReference<List<Produto>>() {}));
        } catch (Exception e) {
            System.out.println("ERRO ao carregar produtos.json: " + e.getMessage());
        }
    }

    public List<Produto> listarTodos() {
        return produtos;
    }

    public Optional<Produto> buscarPorId(String id) {
        return produtos.stream().filter(p -> p.getId().equals(id)).findFirst();
    }

    @RabbitListener(queues = "fila.estoque")
    public void baixarEstoque(EstoqueMessage msg) {
        for (ItemPedido item : msg.getItens()) {
            System.out.println("[← FILA] fila.estoque | baixa de " + item.getQuantidade() + "x " + item.getProdutoId());
            buscarPorId(item.getProdutoId()).ifPresent(p -> {
                int novo = p.getQuantidadeEmEstoque() - item.getQuantidade();
                p.setQuantidadeEmEstoque(Math.max(0, novo));
                System.out.println("ESTOQUE ATUALIZADO: " + p.getNome() + " | novo estoque: " + p.getQuantidadeEmEstoque());
            });
        }

        System.out.println("[→ FILA] fila.compra.concluida | pedido #" + msg.getPedidoId());
        rabbitTemplate.convertAndSend("fila.compra.concluida", new CompraConcluidaMessage(msg.getPedidoId()));
    }
}
