package dev.tensor.misc.imp.setting;

/**
 * @author Hoosiers
 * @since 03-18-2026
 */

public interface NumberSetting<T> extends Setting<T> {

    T getMin();

    T getMax();
}
