package clientServer;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.HttpMethod;

/**
 * Example of plain VertX client and server with POST request setting body length
 */
public class Client {

    public static void main(final String[] args) {
        final Vertx vertx = Vertx.vertx();

        vertx.createHttpServer().requestHandler((c) -> {

            c.bodyHandler(b -> {
                System.out.println(b.toString());
            });
            c.response().end("ok");
        }).listen(8443);


        System.out.println("Server started");
        final HttpClient client = vertx.createHttpClient(
                new HttpClientOptions()
                        .setDefaultHost("localhost")
                        .setDefaultPort(8443));


        client.request(HttpMethod.POST, "/", (r) -> {
            System.out.println("Got response");
        }).putHeader("Content-Length", Integer.toString("Hello".length())).write("Hello").end();
    }
}
