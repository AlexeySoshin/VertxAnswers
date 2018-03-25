package template;

import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.TemplateHandler;
import io.vertx.ext.web.templ.HandlebarsTemplateEngine;
import io.vertx.ext.web.templ.TemplateEngine;

/**
 * https://stackoverflow.com/questions/45664237/vert-x-file-root-versus-handlebars-templates-location/45721260#45721260
 */
public class HandlebarsExample {

    public static void main(String[] args) {

        Vertx vertx = Vertx.vertx();

        TemplateEngine engine = HandlebarsTemplateEngine.create();
        TemplateHandler handler = TemplateHandler.create(engine);

        Router router = Router.router(vertx);

        // This will render index.hbs for root
        router.get("/").handler(ctx -> {
            engine.render(ctx, "templates/index.hbs", res -> {
                if (res.succeeded()) {
                    ctx.response().end(res.result());
                }
            });
        });

        // This will render any other HBS called by name
       router.get("/*").handler(handler);

        vertx.createHttpServer().requestHandler(router::accept).listen(8080);
    }
}
