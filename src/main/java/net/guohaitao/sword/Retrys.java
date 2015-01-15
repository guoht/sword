package net.guohaitao.sword;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.base.Throwables;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.util.concurrent.Callable;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by i@guohaitao.net on 14-9-17.
 * Description: 异步Retry
 */
public final class Retrys {

    private static final Logger logger = Logger.getLogger(Retrys.class.getName());

    private Retrys() {
    }

    /**
     * 异步重试执行
     *
     * @param func
     * @param shouldRetry
     * @param maxTries
     * @param <T>
     * @return
     * @throws Exception
     */
    public static <T> T retry(@Nonnull final Callable<T> func, @Nonnull Predicate<Throwable> shouldRetry, @Nonnegative final int maxTries) throws Exception {
        Preconditions.checkArgument(maxTries > 0, "maxTries > 0");
        int nTry = 0;
        while (true) {
            try {
                nTry++;
                return func.call();
            } catch (Throwable e) {
                if (nTry < maxTries && shouldRetry.apply(e)) {
                    awaitNextRetry(nTry);
                } else {
                    Throwables.propagateIfInstanceOf(e, Exception.class);
                    throw Throwables.propagate(e);
                }
            }
        }
    }

    private static final long BASE_SLEEP_MILLIS = 1000;
    private static final long MAX_SLEEP_MILLIS = 60000;

    private static void awaitNextRetry(final int nTry) throws InterruptedException {
        final double fuzzyMultiplier = Math.min(Math.max(1 + 0.2 * ThreadLocalRandom.current().nextGaussian(), 0), 2);
        final long sleepMillis = (long) (Math.min(MAX_SLEEP_MILLIS, BASE_SLEEP_MILLIS * Math.pow(2, nTry)) * fuzzyMultiplier);
        logger.log(Level.WARNING, String.format("Failed on try %d, retrying in %,dms.", nTry, sleepMillis));
        Thread.sleep(sleepMillis);
    }
}
