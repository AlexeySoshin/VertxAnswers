package reactiveStreams;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.streams.Pump;
import io.vertx.core.streams.ReadStream;
import io.vertx.core.streams.WriteStream;
import io.vertx.ext.reactivestreams.ReactiveReadStream;
import io.vertx.ext.reactivestreams.ReactiveWriteStream;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import org.bson.Document;
import org.reactivestreams.Processor;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import reactor.core.publisher.Flux;

/**
 * https://stackoverflow.com/questions/47162868/how-to-convert-a-vert-x-reactivereadstreamdocument-to-reactivewritestreambuff/47209947#47209947
 */

public class ReactiveStreamExample {


}

class MyMongoVerticle extends AbstractVerticle {

    ReactiveMongoOperations operations;

    public void start() throws Exception {

        final Router router = Router.router(vertx);

        router.route().handler(BodyHandler.create());

        router.get("/myUrl").handler(ctx -> {

            // WebFlux mongo operations returns a ReactiveStreams compatible entity
            Flux<Document> mongoStream = operations.findAll(Document.class, "myCollection");

            ReactiveReadStream<Document> rrs = ReactiveReadStream.readStream();
            mongoStream.subscribe(rrs);

            HttpServerResponse outStream = ctx.response();
            rrs.handler(d -> {
                if (outStream.writeQueueFull()) {
                    outStream.drainHandler((s) -> {
                        rrs.resume();
                    });
                    rrs.pause();
                }
                else {
                    outStream.write(d.toJson());
                }
            }).endHandler(h -> {
                ctx.response().end();
            });
        });

        vertx.createHttpServer().requestHandler(router::accept).listen(8777);
    }
}
