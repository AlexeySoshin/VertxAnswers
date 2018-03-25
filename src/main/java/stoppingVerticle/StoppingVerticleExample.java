package stoppingVerticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;

import java.util.concurrent.TimeUnit;

/**
 * https://stackoverflow.com/questions/49219143/how-to-programmatically-stop-a-vert-x-verticle/49232764#49232764
 */
public class StoppingVerticleExample {

    public static void main(String[] args) {

        Vertx vertx = Vertx.vertx();

        vertx.deployVerticle(new StoppingVerticle());


    }
}

interface UndeployableVerticle {
    void undeploy();
}

class StoppingVerticle extends AbstractVerticle implements UndeployableVerticle {

    @Override
    public void start() {

        System.out.println("Starting");
        vertx.setTimer(TimeUnit.SECONDS.toMillis(5), (h) -> {
            undeploy();
        });
    }

    @Override
    public void stop() {
        System.out.println("Stopping");
    }

    @Override
    public void undeploy() {
        vertx.undeploy(deploymentID());
    }
}
