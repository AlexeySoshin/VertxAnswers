package clientServer;

import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.handler.BodyHandler;

/**
 * Example of how ordered=false works with executeBlocking
 * https://stackoverflow.com/questions/49599662/vertx-http-server-use-only-one-instance-of-worker-thread
 */
public class ClientWithExecuteBlocking {

    public static void main(final String[] args) {
        final Vertx vertx = Vertx.vertx();

        Router router = Router.router(vertx);
        router.route().handler(BodyHandler.create());
        router.post("/").handler(c -> {
                    JsonObject json = c.getBodyAsJson();

                    vertx.executeBlocking(b -> {
                                System.out.println("Blocking on " + Thread.currentThread().getName());
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                b.complete();
                            }, false, h -> {
                                System.out.println("Released " + Thread.currentThread().getName());
                                c.response().end(json.toString());
                            }
                    );
                }
        );
        vertx.createHttpServer().requestHandler(router::accept).listen(8443);


        System.out.println("Server started");
        WebClient client = WebClient.create(vertx);

        for (int i = 0; i < 100; i++) {
            client.request(HttpMethod.POST, 8443, "localhost", "/").
                    sendBuffer(Buffer.buffer(String.format("{\"val\":%d}", i)), h -> {
                        System.out.println(h.result().bodyAsString());
                    });
        }
    }
}
