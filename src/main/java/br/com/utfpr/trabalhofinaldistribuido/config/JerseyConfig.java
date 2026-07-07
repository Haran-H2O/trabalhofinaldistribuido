package br.com.utfpr.trabalhofinaldistribuido.config;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JerseyConfig extends ResourceConfig {
    public JerseyConfig() {
        packages(
            "br.com.utfpr.trabalhofinaldistribuido.produtos.provider",
            "br.com.utfpr.trabalhofinaldistribuido.cep.provider",
            "br.com.utfpr.trabalhofinaldistribuido.loja.provider",
            "br.com.utfpr.trabalhofinaldistribuido.eventos.provider"
        );
        register(JacksonFeature.class);
    }
}
