package cors;

import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.CorsHandler;

import java.math.BigDecimal;

/**
 * CORS example
 */
public class CorsExample {

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();

        Router router = Router.router(vertx);

        router.route().handler(CorsHandler.create("*"));
        router.route().handler((req) -> {
            req.response().end("Ok");
        });

        vertx.createHttpServer().requestHandler(router::accept).listen(8888);
    }
}
