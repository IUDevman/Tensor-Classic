package dev.tensor.misc.event.imp;

import dev.tensor.misc.imp.Globals;

/**
 * @author DarkMagician6
 * @since 07-30-2013
 */

public interface Event extends Globals {

    @SuppressWarnings("unchecked")
    default <T extends Event> T call() {

        this.getEventManager().call(this);

        return (T) this;
    }

    default void reset() {

    }
}
