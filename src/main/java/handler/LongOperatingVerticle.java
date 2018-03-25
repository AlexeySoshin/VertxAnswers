package handler;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;

public class LongOperatingVerticle extends AbstractVerticle {

    @Override
    public void start() {

        final String pojo = "Very long file...";


        final Future<String> f = Future.future();
        this.vertx.deployVerticle(new AbstractVerticle() {
            @Override
            public void start() throws Exception {

                Thread.sleep(5000);
                f.complete("Ok");
            }
        }, new DeploymentOptions().setWorker(true));

        System.out.println("Will wait now " + this.deploymentID());

        f.setHandler((e) -> {
            System.out.println(e.result());
        });

    }

    @Override
    public void stop() {
        System.out.println("Stopping " + this.deploymentID());
    }
}
