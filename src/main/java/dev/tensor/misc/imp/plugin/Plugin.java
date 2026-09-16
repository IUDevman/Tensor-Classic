package dev.tensor.misc.imp.plugin;

import dev.tensor.misc.helpers.TensorLogger;

/**
 * @author Hoosiers
 * @since 05-10-2026
 */

@SuppressWarnings("unused")
public interface Plugin extends PluginGlobal {

    String name();

    String version();

    String[] authors();

    void load(TensorLogger LOGGER);

    void save(TensorLogger LOGGER);

    default void loadPost(TensorLogger LOGGER) {

    }
}
