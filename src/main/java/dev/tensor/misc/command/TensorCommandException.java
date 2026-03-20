package dev.tensor.misc.command;

/**
 * @author Hoosiers
 * @since 03-19-2026
 */

public final class TensorCommandException extends Exception {

    private final Class<?> FROM_CLASS;

    public TensorCommandException(Class<?> FROM_CLASS) {
        this.FROM_CLASS = FROM_CLASS;
    }

    public String className() {
        return this.FROM_CLASS.getName();
    }
}
