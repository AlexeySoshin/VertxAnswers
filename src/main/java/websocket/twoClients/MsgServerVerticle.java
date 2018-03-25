package websocket.twoClients;

import io.netty.util.internal.ConcurrentSet;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.ServerWebSocket;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

import java.util.Set;

/**
 * https://stackoverflow.com/questions/47683263/vertx-websockets-starts-encoding-deconding-in-loop-after-connecting-second-clien/47690798#47690798
 */
public class MsgServerVerticle extends AbstractVerticle {
    private final Logger L;

    private final String path;
    private final int port;
    private final String eBusTag;
    private final String backwardTag;

    private HttpServer server;

    private EventBus eBus;

    private final Set<ServerWebSocket> conns;

    public MsgServerVerticle(final int port, final String eBusTag, final String backwardTag) {
        this.port = port;
        this.eBusTag = eBusTag;
        this.backwardTag = backwardTag;

        this.conns = new ConcurrentSet<>();
        this.path = "/" + eBusTag;

        this.L = LoggerFactory.getLogger(eBusTag);

    }

    @Override
    public void start(final Future<Void> startFuture) throws Exception {
        this.eBus = this.vertx.eventBus();
        this.L.info("Initializing server instance at port " + this.port);

        this.server = this.vertx.createHttpServer();

        this.server.websocketHandler(webSock -> {

            if (!webSock.path().equals(this.path)) {

                webSock.reject();

            } else {

                this.conns.add(webSock);

                this.conns.forEach(sock -> {
                    if (sock != webSock) {
                        sock.writeBinaryMessage(Utils.jsonToBuf(Utils.msg("SERVER: new client " + webSock.remoteAddress().toString())));
                    }
                });

                this.eBus.publish(this.backwardTag, Utils.msg("SERVER: new client " + webSock.remoteAddress().toString()));

                webSock.binaryMessageHandler(buf -> {
                    final JsonObject msg = Utils.bufToJson(buf);
                    this.conns.forEach(sock -> {
                        if (sock != webSock) {
                            sock.writeBinaryMessage(buf);
                        }
                    });
                    this.eBus.publish(this.backwardTag, msg);
                });

            }

        });

        this.server.listen(this.port);

        startFuture.complete();
    }

    @Override
    public void stop(final Future<Void> stopFuture) throws Exception {
        this.conns.forEach(sock -> {
            sock.writeFinalTextFrame("Server is shutting down...");
        });
        this.server.close();
        stopFuture.complete();
    }
}
