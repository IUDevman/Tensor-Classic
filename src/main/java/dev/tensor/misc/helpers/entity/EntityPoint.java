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
    private final int worldYDiff;
    private final boolean plusSymbol;
    private final int radius;

    public EntityPoint(int x, int y, int color, int worldYDiff, boolean plusSymbol, int radius) {
        this.x = x;
        this.y = y;
        this.color = color;
        this.worldYDiff = worldYDiff;
        this.plusSymbol = plusSymbol;
        this.radius = radius;
    }

    //Note: This method only adds vertices. @see Radar.class.
    public void render() {
        if (this.plusSymbol) {
            this.genRectangleVertices(this.x - this.radius, this.y - this.radius, this.x + this.radius, this.y + this.radius);
            this.genRectangleVertices(this.x + this.radius, this.y - this.radius, this.x + (2 * this.radius), this.y + this.radius);
            this.genRectangleVertices(this.x - (2 * this.radius), this.y - this.radius, this.x - this.radius, this.y + this.radius);
            this.genRectangleVertices(this.x - this.radius, this.y + this.radius, this.x + this.radius, this.y + (2 * this.radius));
            this.genRectangleVertices(this.x - this.radius, this.y - (2 * this.radius), this.x + this.radius, this.y - this.radius);

        } else {
            this.genRectangleVertices(this.x - this.radius, this.y - this.radius, this.x + this.radius, this.y + this.radius);
        }
    }

    public int getColor() {
        return this.color;
    }

    public int getWorldYDiff() {
        return this.worldYDiff;
    }
}
