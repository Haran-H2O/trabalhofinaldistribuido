package br.com.utfpr.trabalhofinaldistribuido.eventos.provider;

import br.com.utfpr.trabalhofinaldistribuido.mensagens.CompraConcluidaMessage;
import br.com.utfpr.trabalhofinaldistribuido.mensagens.EmailMessage;
import br.com.utfpr.trabalhofinaldistribuido.mensagens.ItemPedido;
import br.com.utfpr.trabalhofinaldistribuido.mensagens.PedidoMessage;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.changestream.ChangeStreamDocument;
import jakarta.annotation.PostConstruct;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class GerenciadorEventosService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @PostConstruct
    public void iniciar() {
        Thread watcher = new Thread(this::escutarPedidos, "mongo-change-stream-pedidos");
        watcher.setDaemon(true);
        watcher.start();
    }

    private void escutarPedidos() {
        while (true) {
            try {
                MongoCollection<Document> pedidos = mongoTemplate.getCollection("pedidos");
                List<Bson> pipeline = List.of(Aggregates.match(Filters.eq("operationType", "insert")));
                System.out.println("GERENCIADOR DE EVENTOS: escutando change stream da collection 'pedidos'...");
                try (MongoCursor<ChangeStreamDocument<Document>> cursor = pedidos.watch(pipeline).iterator()) {
                    while (cursor.hasNext()) {
                        Document fullDoc = cursor.next().getFullDocument();
                        if (fullDoc == null) continue;
                        processarNovoPedido(fullDoc);
                    }
                }
            } catch (Exception e) {
                System.out.println("ERRO no change stream do MongoDB, tentando novamente em 5s: " + e.getMessage());
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
        }
    }

    private void processarNovoPedido(Document doc) {
        Long pedidoId = doc.getLong("pedidoId");
        List<ItemPedido> itens = new ArrayList<>();
        for (Document itemDoc : doc.getList("itens", Document.class)) {
            itens.add(new ItemPedido(
                itemDoc.getString("produtoId"),
                itemDoc.getInteger("quantidade"),
                itemDoc.getDouble("precoUnitario")
            ));
        }
        Double valor = doc.getDouble("valor");
        String email = doc.getString("email");
        String endereco = doc.getString("endereco");
        Boolean simularErro = doc.getBoolean("simularErro");

        System.out.println("[MONGO EVENT] novo pedido detectado via change stream: #" + pedidoId);

        System.out.println("[→ FILA] fila.email | confirmação de compra para " + email);
        rabbitTemplate.convertAndSend("fila.email", new EmailMessage(
            "CONFIRMACAO_COMPRA",
            email,
            "Pedido #" + pedidoId + " confirmado"
        ));

        atualizarStatus(pedidoId, "PROCESSANDO_PAGAMENTO");

        System.out.println("[→ FILA] fila.pagamento | pedido #" + pedidoId);
        rabbitTemplate.convertAndSend("fila.pagamento", new PedidoMessage(
            pedidoId, itens, valor, email, endereco, simularErro
        ));
    }

    @RabbitListener(queues = "fila.pagamento.dlq")
    public void processarDlq(PedidoMessage pedido) {
        System.out.println("[← FILA] fila.pagamento.dlq | pedido #" + pedido.getPedidoId() + " rejeitado");
        atualizarStatus(pedido.getPedidoId(), "PAGAMENTO_RECUSADO");
    }

    @RabbitListener(queues = "fila.compra.concluida")
    public void processarCompraConcluida(CompraConcluidaMessage msg) {
        System.out.println("[← FILA] fila.compra.concluida | pedido #" + msg.getPedidoId());
        atualizarStatus(msg.getPedidoId(), "CONCLUIDA");
    }

    private void atualizarStatus(Long pedidoId, String status) {
        Query query = Query.query(Criteria.where("pedidoId").is(pedidoId));
        Update update = Update.update("status", status).set("atualizadoEm", LocalDateTime.now());
        mongoTemplate.updateFirst(query, update, Pedido.class);
        System.out.println("STATUS ATUALIZADO: pedido #" + pedidoId + " -> " + status);
    }
}
