package clientServer;

import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonArray;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.handler.BodyHandler;

/**
 * https://stackoverflow.com/questions/49588402/vert-x-routingcontext-cant-receive-json-array
 */
public class ClientJsonArray {

    public static void main(final String[] args) {
        final Vertx vertx = Vertx.vertx();

        Router router = Router.router(vertx);
        router.route().handler(BodyHandler.create());
        router.post("/").handler(c -> {
                //JsonObject json = c.getBodyAsJson();

                JsonArray jsonArray = c.getBodyAsJsonArray();

                c.response().end(jsonArray.toString());
            }
        );
        vertx.createHttpServer().requestHandler(router::accept).listen(8443);


        System.out.println("Server started");
        WebClient client = WebClient.create(vertx);

        // This will succeed if you use getBodyAsJson
        client.request(HttpMethod.POST, 8443, "localhost", "/").
                // Don't use sendJson() with strings, it will double wrap them
                sendBuffer(Buffer.buffer("{}"), h -> {
                    System.out.println(h.result().bodyAsString());
                });

        // This will succeed if you use getBodyAsJsonArray
        client.request(HttpMethod.POST, 8443, "localhost", "/").
                sendBuffer(Buffer.buffer("[]"), h -> {
                    System.out.println(h.result().bodyAsString());
        });
    }
}
