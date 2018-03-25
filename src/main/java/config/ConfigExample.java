package config;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;

/**
 * Accessing configuration from verticle
 */
public class ConfigExample {
    public static void main(final String[] args) {
        final Vertx vertx = Vertx.vertx();

        vertx.deployVerticle(new AbstractVerticle() {
            @Override
            public void start() {
            final Integer port = config().getInteger("http.port");
            System.out.println(port);

            this.vertx.createHttpServer().listen(config().getInteger("http.port"), result -> {
                if (result.succeeded()) {
                    System.out.println("Ok");
                } else {
                    System.out.println("Not ok");
                }
            });
            }
        });
    }
}
