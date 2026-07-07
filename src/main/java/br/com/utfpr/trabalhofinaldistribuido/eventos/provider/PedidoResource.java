package br.com.utfpr.trabalhofinaldistribuido.eventos.provider;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Path("/pedidos")
@Produces(MediaType.APPLICATION_JSON)
public class PedidoResource {

    @Autowired
    private MongoTemplate mongoTemplate;

    @GET
    @Path("/{pedidoId}")
    public Response consultar(@PathParam("pedidoId") Long pedidoId) {
        Pedido pedido = mongoTemplate.findOne(
            Query.query(Criteria.where("pedidoId").is(pedidoId)),
            Pedido.class
        );
        if (pedido == null) {
            return Response.status(Response.Status.NOT_FOUND)
                .entity(Map.of("erro", "Pedido não encontrado"))
                .build();
        }
        return Response.ok(pedido).build();
    }
}
