package dev.tensor.misc.event.imp.modifiers.cancellable;

/**
 * @author DarkMagician6
 * @since 08-27-2013
 */

public interface Cancellable {

    boolean isCancelled();

    void setCancelled(boolean cancelled);

    void cancel();
}
