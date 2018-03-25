package router;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;

import java.lang.reflect.Field;
import java.util.Set;

/**
 * https://stackoverflow.com/questions/38463145/list-all-registered-routes-in-vertx
 */
public class InvestigateRoutes extends AbstractVerticle {

    private HttpServer httpServer = null;

    @Override
    public void start() throws Exception {


        this.httpServer = this.vertx.createHttpServer();
        // Example router setup
        final Router router = Router.router(this.vertx);
        router.route(HttpMethod.GET, "/").handler(routingContext -> {
            routingContext.response().end("Root");
        });

        router.route(HttpMethod.GET, "/users").handler(routingContext -> {
            routingContext.response().end("Post");
        });

        router.route(HttpMethod.POST, "/users").handler(routingContext -> {
            routingContext.response().end("Post");
        });

        // Getting the routes
        for (final Route r : router.getRoutes()) {
            final Field f = r.getClass().getDeclaredField("methods");
            f.setAccessible(true);
            final Set<HttpMethod> methods = (Set<HttpMethod>) f.get(r);
            System.out.println(methods.toString() + r.getPath());
        }

        this.httpServer.requestHandler(router::accept);


        this.httpServer.listen(9999);
    }
}