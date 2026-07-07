package br.com.utfpr.trabalhofinaldistribuido.fiscal.provider;

import br.com.utfpr.trabalhofinaldistribuido.mensagens.EmailMessage;
import br.com.utfpr.trabalhofinaldistribuido.mensagens.EstoqueMessage;
import br.com.utfpr.trabalhofinaldistribuido.mensagens.FiscalMessage;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class FiscalService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = "fila.fiscal")
    public void emitir(FiscalMessage msg) {
        System.out.println("[← FILA] fila.fiscal | pedido #" + msg.getPedidoId() + " | emitindo NF");

        String chaveNF = "NF-" + UUID.randomUUID().toString().substring(0, 10).toUpperCase();
        System.out.println("NOTA FISCAL EMITIDA: " + chaveNF + " | pedido #" + msg.getPedidoId() + " | R$ " + String.format("%.2f", msg.getValorTotal()));

        System.out.println("[→ FILA] fila.email | NF " + chaveNF + " disponível para " + msg.getEmail());
        rabbitTemplate.convertAndSend("fila.email", new EmailMessage(
            "NOTA_FISCAL",
            msg.getEmail(),
            "NF " + chaveNF + " disponível"
        ));

        System.out.println("[→ FILA] fila.estoque | baixa de " + msg.getItens().size() + " item(ns) do pedido #" + msg.getPedidoId());
        rabbitTemplate.convertAndSend("fila.estoque", new EstoqueMessage(
            msg.getPedidoId(),
            msg.getItens()
        ));
    }
}
