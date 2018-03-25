package websocket.twoClients;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;

/**
 * https://stackoverflow.com/questions/47683263/vertx-websockets-starts-encoding-deconding-in-loop-after-connecting-second-clien/47690798#47690798
 */
public class Utils {
    public static Buffer jsonToBuf(final JsonObject message) {
        return message.toBuffer();
    }

    public static JsonObject bufToJson(final Buffer buf) {
        return buf.toJsonObject();
    }

    public static JsonObject msg(final String msg) {
        return new JsonObject("{\"value\":\"" + msg + "\"}");
    }
}
