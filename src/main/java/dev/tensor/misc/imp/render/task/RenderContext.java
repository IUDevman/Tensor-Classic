package dev.tensor.misc.imp.render.task;

import dev.tensor.misc.imp.Globals;

/**
 * @author Hoosiers
 * @since 05-03-2026
 */

public interface RenderContext extends Globals {

    int getLineWidth();

    int getColor();

    boolean isEmpty();

    void addVertex(double x, double y, double z);

    void preload(boolean lines, boolean color);

    void draw();
}
