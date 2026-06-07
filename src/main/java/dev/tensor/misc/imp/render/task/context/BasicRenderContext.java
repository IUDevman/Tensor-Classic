package dev.tensor.misc.imp.render.task.context;

import dev.tensor.misc.imp.render.task.RenderContext;
import net.minecraft.src.Tessellator;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;

/**
 * @author Hoosiers
 * @since 05-03-2026
 */

public final class BasicRenderContext implements RenderContext {

    private final int lineWidth;
    private final int color;

    private final ArrayList<double[]> queue = new ArrayList<>();

    public BasicRenderContext(int lineWidth, int color) {
        this.lineWidth = lineWidth;
        this.color = color;
    }

    @Override
    public int getLineWidth() {
        return this.lineWidth;
    }

    @Override
    public int getColor() {
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
            float[] colors = this.getColor(this.color, true);
            GL11.glColor4f(colors[0], colors[1], colors[2], colors[3]);
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
