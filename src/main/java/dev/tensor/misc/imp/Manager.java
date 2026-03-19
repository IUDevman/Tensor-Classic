package dev.tensor.misc.imp;

import org.slf4j.Logger;

/**
 * @author Hoosiers
 * @since 03-18-2026
 */

public interface Manager extends Globals {

    String getName();

    void load(Logger LOGGER);

    void save(Logger LOGGER);
}
