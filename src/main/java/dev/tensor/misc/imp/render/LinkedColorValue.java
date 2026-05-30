package dev.tensor.misc.imp.render;

public final class LinkedColorValue {

    public final Type type;
    public final int value;

    public LinkedColorValue(Type type, int value) {
        this.type = type;
        this.value = value;
    }

    public enum Type {
        RED,
        GREEN,
        BLUE
    }

    public float toTestFloat() {
        float floatCovert = this.value / 255F;

        if (floatCovert >= 0.5F) {
            return 1.0F;
        } else {
            return 0.0F;
        }
    }
}
