package dev.tensor.misc.imp;

import dev.tensor.misc.helpers.TensorLogger;

/**
 * @author Hoosiers
 * @since 03-18-2026
 */

public interface Manager extends Globals {

    String getName();

    default void load(TensorLogger LOGGER) {
        LOGGER.log("Loaded: " + this.getName(), TensorLogger.Level.INFO);

        this.onLoad(LOGGER);
    }

    default void onLoad(TensorLogger LOGGER) {

    }

    default void save(TensorLogger LOGGER) {
        LOGGER.log("Saved: " + this.getName(), TensorLogger.Level.INFO);

        this.onSave(LOGGER);
    }

    default void onSave(TensorLogger LOGGER) {

    }

    default void onLoadPost(TensorLogger LOGGER) {

    }
}
