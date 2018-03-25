import io.vertx.core.*;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**
 * https://stackoverflow.com/questions/40323028/vert-x-make-a-promise-from-vertx-executeblocking
 */
public class BlockingWithLatchExample {


    public static void main(String[] args) throws InterruptedException {

        Vertx vertx = Vertx.vertx();

        // This should be equal to number of operations to complete
        CountDownLatch latch = new CountDownLatch(3);

        Long start = System.currentTimeMillis();

        // Start your operations
        vertx.deployVerticle(new BlockingVerticle(latch));
        vertx.deployVerticle(new BlockingVerticle(latch));
        vertx.deployVerticle(new BlockingVerticle(latch));

        // Always use await with timeout
        latch.await(2, TimeUnit.SECONDS);

        System.out.println("Took me " + (System.currentTimeMillis() - start) + " millis");
    }


    private static class BlockingVerticle extends AbstractVerticle {

        private final CountDownLatch latch;

        public BlockingVerticle(CountDownLatch latch) {
            this.latch = latch;
        }

        @Override
        public void start() throws InterruptedException {


            long millis = 1000 + ThreadLocalRandom.current().nextInt(500);

            System.out.println("It will take me " + millis + " to complete");

            // Wait for some random time, but no longer that 1.5 seconds
            Thread.sleep(millis);

            latch.countDown();
        }

    }
}
