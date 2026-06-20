package dev.tensor.misc.helpers.color;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Hoosiers
 * @since 06-01-2026
 */

public final class RainbowColor {

    private RainbowColor() {

    }

    public static AtomicInteger[] getRainbow() {
        if (FIRST_RUN) {
            FIRST_RUN = false;
            INCREMENT_THREAD.start();
        }

        return RAINBOW;
    }

    private static boolean FIRST_RUN = true;

    private static final AtomicInteger[] RAINBOW = new AtomicInteger[]{new AtomicInteger(255), new AtomicInteger(0), new AtomicInteger(0)};

    private static final int MIN_INCREMENT = 0;
    private static final int MAX_INCREMENT = 1530;

    private static final AtomicInteger INCREMENT = new AtomicInteger(MIN_INCREMENT);

    public static final long DEFAULT_SLEEP_TIME = 10;
    public static final int DEFAULT_INCREMENT_STEP = 3;

    private static long SLEEP_TIME = DEFAULT_SLEEP_TIME;
    private static int INCREMENT_STEP = DEFAULT_INCREMENT_STEP;

    private static final Thread INCREMENT_THREAD = new Thread(() -> {
        while (true) {
            try {
                Thread.sleep(getSleepTime());

            } catch (Exception ignored) {
            }

            setRainbowFromIncrement(increment().get());
        }
    });

    private static long getSleepTime() {
        return SLEEP_TIME;
    }

    public static void setSleepTime(long NEW_SLEEP_TIME) {
        SLEEP_TIME = NEW_SLEEP_TIME;
    }

    private static int getIncrementStep() {
        return INCREMENT_STEP;
    }

    public static void setIncrementStep(int NEW_INCREMENT_STEP) {
        INCREMENT_STEP = NEW_INCREMENT_STEP;
    }

    public static void setSleepTimeAndIncrementStep(long NEW_SLEEP_TIME, int NEW_INCREMENT_STEP) {
        setSleepTime(NEW_SLEEP_TIME);
        setIncrementStep(NEW_INCREMENT_STEP);
    }

    public static void reset() {
        INCREMENT.set(MIN_INCREMENT);
        RAINBOW[0].set(255);
        RAINBOW[1].set(0);
        RAINBOW[2].set(0);
    }

    private static AtomicInteger increment() {
        int newIncrement = INCREMENT.get() + getIncrementStep();

        if (newIncrement > MAX_INCREMENT) {
            INCREMENT.set(MIN_INCREMENT + (newIncrement - MAX_INCREMENT));
        } else {
            INCREMENT.set(newIncrement);
        }

        return INCREMENT;
    }

    private static void setRainbowFromIncrement(int increment) {
        int normalizedIncrement = Math.floorMod(increment, MAX_INCREMENT);

        int colorType = normalizedIncrement / 255;
        int remainder = normalizedIncrement % 255;

        switch (colorType) {
            case 0:
                RAINBOW[0].set(255);
                RAINBOW[1].set(remainder);
                RAINBOW[2].set(0);
                break;

            case 1:
                RAINBOW[0].set(255 - remainder);
                RAINBOW[1].set(255);
                RAINBOW[2].set(0);
                break;

            case 2:
                RAINBOW[0].set(0);
                RAINBOW[1].set(255);
                RAINBOW[2].set(remainder);
                break;

            case 3:
                RAINBOW[0].set(0);
                RAINBOW[1].set(255 - remainder);
                RAINBOW[2].set(255);
                break;

            case 4:
                RAINBOW[0].set(remainder);
                RAINBOW[1].set(0);
                RAINBOW[2].set(255);
                break;

            case 5:
            default:
                RAINBOW[0].set(255);
                RAINBOW[1].set(0);
                RAINBOW[2].set(255 - remainder);
                break;
        }
    }
}
