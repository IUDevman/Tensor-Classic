package dev.tensor.misc.helpers.entity;

import dev.tensor.misc.imp.render.Render2D;

/**
 * @author Hoosiers
 * @since 05-01-2026
 */

public final class EntityPoint implements Render2D {

    private final int x;
    private final int y;
    private final int color;
    private final int dWY;

    public EntityPoint(int x, int y, int color, int dWY) {
        this.x = x;
        this.y = y;
        this.color = color;
        this.dWY = dWY;
    }

    public void render(int sphereRadius) {
        this.drawRectangle(this.x - sphereRadius, this.y - sphereRadius, this.x + sphereRadius, this.y + sphereRadius, this.color);
    }

    public int getWorldYDiff() {
        return this.dWY;
    }
}
