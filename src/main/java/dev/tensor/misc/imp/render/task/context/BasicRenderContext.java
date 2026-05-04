package dev.tensor.misc.imp.render.task.context;

import dev.tensor.misc.imp.render.task.RenderContext;
import net.minecraft.src.Tessellator;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;

/**
 * @author Hoosiers
 * @since 05-03-2026
 */

public final class BasicRenderContext implements RenderContext {

    private final int lineWidth;
    private final Color color;

    private final ArrayList<double[]> queue = new ArrayList<>();

    public BasicRenderContext(int lineWidth, Color color) {
        this.lineWidth = lineWidth;
        this.color = color;
    }

    @Override
    public int getLineWidth() {
        return this.lineWidth;
    }

    @Override
    public Color getColor() {
        return this.color;
    }

    @Override
    public boolean isEmpty() {
        return this.queue.isEmpty();
    }

    @Override
    public void addVertex(double x, double y, double z) {
        this.queue.add(new double[]{x, y, z});
    }

    @Override
    public void preload(boolean lines, boolean color) {
        if (lines) {
            GL11.glLineWidth(this.lineWidth);
        }

        if (color) {
            GL11.glColor4f(this.color.getRed() / 255F, this.color.getGreen() / 255F, this.color.getBlue() / 255F, this.color.getAlpha() / 255F);
        }
    }

    @Override
    public void draw() {
        if (this.isEmpty()) {
            return;
        }

        this.queue.forEach(doubles -> Tessellator.instance.addVertex(doubles[0], doubles[1], doubles[2]));
    }
}
