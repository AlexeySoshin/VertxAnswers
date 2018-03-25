package periodic;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;

import static java.lang.Thread.sleep;

/**
 * https://stackoverflow.com/questions/43634490/vertx-check-if-database-config-is-available/43656988#43656988
 */
public class PeriodicExample {

    public static void main(final String[] args) throws InterruptedException {
        final Vertx vertx = Vertx.vertx();

        vertx.deployVerticle(new PeriodicVerticle());

        sleep(10000);
    }
}

class PeriodicVerticle extends AbstractVerticle {

    private Long timerId;

    @Override
    public void start() {
        System.out.println("Started");
        final Long timerId = this.vertx.setPeriodic(1000, (l) -> {

            final boolean result = checkDB();
            if (result) {
                cancelTimer();
            }
        });

        setTimerId(timerId);

    }

    private boolean checkDB() {
        // Does nothing
        return false;
    }

    private void cancelTimer() {
        System.out.println("Cancelling");
        getVertx().cancelTimer(this.timerId);
    }

    private void setTimerId(final Long timerId) {
        this.timerId = timerId;
    }
}
