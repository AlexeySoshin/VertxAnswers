package executeBlocking;

import io.vertx.core.Vertx;

/**
 * https://stackoverflow.com/questions/49461049/vertx-executeblocking-use-event-loop-instead-of-worker-thread/49474120?noredirect=1#comment85950692_49474120
 */
public class ExecuteBlockingExample {

    public static void main(String[] args) {

        Vertx vertx = Vertx.vertx();

        vertx.setPeriodic(1000, h -> {
            System.out.println("Periodic on " + Thread.currentThread().getName());
           vertx.executeBlocking(f -> {
               // This is on worker thread
               System.out.println("Future on " + Thread.currentThread().getName());
               f.complete();
           }, r -> {
               // Back to event loop
               System.out.println("Result on " + Thread.currentThread().getName());
           });
        });
    }
}


