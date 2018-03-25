import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;

/**
 * Following this question http://stackoverflow.com/questions/40035385/how-to-parse-query-argument-using-vertx
 */
public class RequestQueryParamsExample {

    public static void main(String[] args) {

        Vertx vertx = Vertx.vertx();

        HttpServer server = vertx.createHttpServer();

        server.requestHandler(request -> {
            String startTime = request.getParam("start_time");
            String endTime = request.getParam("end_time");

            // This handler gets called for each request that arrives on the server
            HttpServerResponse response = request.response();
            response.putHeader("content-type", "text/plain");

            // Write to the response and end it
            response.end(String.format("Got start time %s, end time %s", startTime, endTime));
        });

        server.listen(8888);
    }
}
