package dev.tensor.misc.imp.setting;

import dev.tensor.misc.imp.Globals;

/**
 * @author Hoosiers
 * @since 04-12-2021
 */

public interface Setting<T> extends Globals {

    String getName();

    T getDefaultValue();

    T getValue();

    void setValue(T VALUE);

    default void reset() {
        this.setValue(this.getDefaultValue());
    }
}
