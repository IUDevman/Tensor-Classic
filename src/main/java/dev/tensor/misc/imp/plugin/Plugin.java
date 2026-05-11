package dev.tensor.misc.imp.plugin;

import org.slf4j.Logger;

/**
 * @author Hoosiers
 * @since 05-10-2026
 */

@SuppressWarnings("unused")
public interface Plugin extends PluginGlobal {

    String name();

    String version();

    String[] authors();

    void load(Logger LOGGER);

    void save(Logger LOGGER);

    default void loadPost(Logger LOGGER) {

    }
}
