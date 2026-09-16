package dev.tensor.misc.imp;

import dev.tensor.misc.helpers.TensorLogger;

/**
 * @author Hoosiers
 * @since 03-18-2026
 */

public interface Manager extends Globals {

    String getName();

    void load(TensorLogger LOGGER);

    void save(TensorLogger LOGGER);

    default void loadPost(TensorLogger LOGGER) {

    }
}
