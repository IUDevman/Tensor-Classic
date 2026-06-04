package dev.tensor.feature.managers;

import dev.tensor.misc.imp.Manager;
import org.slf4j.Logger;

/**
 * @author Hoosiers
 * @since 06-03-2026
 */

public final class TBotManager implements Manager {

    @Override
    public String getName() {
        return "TBotManager";
    }

    @Override
    public void load(Logger LOGGER) {
        LOGGER.info("Loaded: " + this.getName());
    }

    @Override
    public void save(Logger LOGGER) {
        LOGGER.info("Saved: " + this.getName());
    }
}
