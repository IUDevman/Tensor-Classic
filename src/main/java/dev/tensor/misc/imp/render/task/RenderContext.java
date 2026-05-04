package dev.tensor.misc.imp.render.task;

import java.awt.*;

/**
 * @author Hoosiers
 * @since 05-03-2026
 */

public interface RenderContext {

    int getLineWidth();

    Color getColor();

    boolean isEmpty();

    void addVertex(double x, double y, double z);

    void preload(boolean lines, boolean color);

    void draw();
}
