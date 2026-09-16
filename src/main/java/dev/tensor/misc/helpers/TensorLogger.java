package dev.tensor.misc.helpers;

import dev.tensor.TensorClassic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Hoosiers
 * @since 09-15-2026
 */

public final class TensorLogger {

    private static TensorLogger INSTANCE;

    public static TensorLogger getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new TensorLogger();
        }

        return INSTANCE;
    }

    private static final boolean USE_SYS_OUT = false;

    private final Logger LOGGER;

    private TensorLogger() {
        this.LOGGER = LoggerFactory.getLogger(TensorClassic.MOD_NAME);
    }

    public void log(String message, Level level) {
        if (USE_SYS_OUT) {
            System.out.println("[" + TensorClassic.MOD_NAME + "] " + message + " (" + level.name() + ")");
            return;
        }

        level.process(LOGGER, message);
    }

    public enum Level {
        INFO,
        WARN,
        ERROR;

        public void process(Logger LOGGER, String message) {
            switch (this) {
                case WARN:
                    LOGGER.warn(message);
                    break;
                case ERROR:
                    LOGGER.error(message);
                    break;
                default:
                    LOGGER.info(message);
                    break;
            }
        }
    }
}
