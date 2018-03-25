package websocket.twoClients;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;

/**
 * https://stackoverflow.com/questions/47683263/vertx-websockets-starts-encoding-deconding-in-loop-after-connecting-second-clien/47690798#47690798
 */
public class Main {

    public static void main(final String[] args) {

        final Vertx vertx = Vertx.vertx();

        vertx.deployVerticle(new MsgServerVerticle(8080,
                "ebus",
                "back"), new DeploymentOptions().setWorker(true), (r) -> {
            vertx.deployVerticle(new MsgClientVerticle("ebus",
                    "127.0.0.1",
                    8080,
                    "/ebus",
                    "a",
                    "back"), new DeploymentOptions().setWorker(true), (r2) -> {
                vertx.deployVerticle(new MsgClientVerticle("ebus",
                        "127.0.0.1",
                        8080,
                        "/ebus",
                        "b",
                        "back"), new DeploymentOptions().setWorker(true), (r3) -> {
                    System.out.println("Done");
                });
            });
        });
    }
}
