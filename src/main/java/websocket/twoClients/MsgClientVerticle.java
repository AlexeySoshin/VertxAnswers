package websocket.twoClients;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.http.HttpClient;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.LoggerFactory;

/**
 * https://stackoverflow.com/questions/47683263/vertx-websockets-starts-encoding-deconding-in-loop-after-connecting-second-clien/47690798#47690798
 */
public class MsgClientVerticle extends AbstractVerticle {
    private final io.vertx.core.logging.Logger L;

    private final String eBusTag;
    private final String backwardTag;
    private final String targetHost;
    private final int port;
    private final String id;
    private final String path;

    private EventBus eBus;

    private HttpClient client;

    public MsgClientVerticle(final String eBusTag, final String targetHost, final int port, final String path, final String id, final String backwardTag) {
        this.eBusTag = eBusTag;
        this.targetHost = targetHost;
        this.path = path;
        this.port = port;
        this.id = id;
        this.backwardTag = backwardTag;

        this.L = LoggerFactory.getLogger(eBusTag);
    }

    @Override
    public void start(final Future<Void> startFuture) throws Exception {
        this.L.info("Initializing client connection to " + this.targetHost + ":" + this.port + this.path);
        this.eBus = this.vertx.eventBus();

        try {

            this.client = this.vertx.createHttpClient();

            this.client.websocket(this.port, this.targetHost, this.path, webSock -> {
                this.L.info("Connected to " + this.targetHost + ":" + this.port + "/" + this.path);
                this.eBus.publish(this.backwardTag, Utils.msg("Connected"));
                webSock.binaryMessageHandler(buf -> {
                    this.eBus.publish(this.backwardTag, Utils.bufToJson(buf));
                });
                this.eBus.consumer(this.eBusTag).handler(msg -> {
                    final JsonObject message = (JsonObject) msg.body();
                    webSock.writeBinaryMessage(Utils.jsonToBuf(message));
                });
            });
        } catch (final NullPointerException e) {
            this.L.error("Null Pointer: " + e.getLocalizedMessage());
            e.printStackTrace();
        }


        startFuture.complete();
    }

    @Override
    public void stop(final Future<Void> stopFuture) throws Exception {
        this.L.info("Connection to " + this.targetHost + ":" + this.port + "/" + this.path + " closed");
        this.client.close();
        stopFuture.complete();
    }

}
