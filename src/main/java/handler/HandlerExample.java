package handler;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;

public class HandlerExample {

    public static void main(final String[] args) {
        final Vertx vertx = Vertx.vertx();

        final String[] myId = {""};
        vertx.deployVerticle("handler.LongOperatingVerticle", new DeploymentOptions().setInstances(2), (h) -> {
            if (h.succeeded()) {
                System.out.println("BLABLA" + h.result());
                System.out.println(vertx.deploymentIDs());
                myId[0] = h.result();
                vertx.deployVerticle("handler.LongOperatingVerticle", new DeploymentOptions().setInstances(1));
            }
            else {
                System.out.println("CAUSE " + h.cause());
            }
        });

        vertx.undeploy(myId[0]);
    }




}

