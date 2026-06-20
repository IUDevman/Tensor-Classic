package dev.tensor.misc.imp.render.task;

import dev.tensor.misc.imp.render.task.context.BasicRenderContext;
import net.minecraft.src.Tessellator;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Hoosiers
 * @since 05-03-2026
 */

public final class RenderTask {

    private static RenderTask RENDER_TASK = new RenderTask();

    public static RenderTask getInstance() {
        if (RENDER_TASK == null) {
            RENDER_TASK = new RenderTask();
        }

        return RENDER_TASK;
    }

    private RenderTask() {

    }

    private final ArrayList<BasicRenderContext> strips = new ArrayList<>();
    private final ArrayList<BasicRenderContext> lines = new ArrayList<>();
    private final ArrayList<BasicRenderContext> quads = new ArrayList<>();

    public void loadStrip(BasicRenderContext strip) {
        this.strips.add(strip);
    }

    public void loadLine(BasicRenderContext line) {
        this.lines.add(line);
    }

    public void loadQuad(BasicRenderContext quad) {
        this.quads.add(quad);
    }

    public void render() {
        AtomicInteger prevLineWidth = new AtomicInteger(0);
        AtomicInteger prevColor = new AtomicInteger(-1);

        if (!this.quads.isEmpty()) {

            Tessellator.instance.startDrawing(GL11.GL_QUADS);

            this.quads.forEach(context -> {

                boolean color = context.getColor() != prevColor.get();

                if (color) {
                    Tessellator.instance.draw();
                    Tessellator.instance.startDrawing(GL11.GL_QUADS);
                }

                context.preload(false, color);

                context.draw();

                if (color) {
                    prevColor.set(context.getColor());
                }
            });

            Tessellator.instance.draw();
        }

        if (!this.strips.isEmpty()) {

            this.strips.forEach(context -> {

                boolean lines = context.getLineWidth() != prevLineWidth.get();
                boolean color = context.getColor() != prevColor.get();

                context.preload(lines, color);

                Tessellator.instance.startDrawing(GL11.GL_LINE_STRIP);

                context.draw();

                Tessellator.instance.draw();

                if (lines) {
                    prevLineWidth.set(context.getLineWidth());
                }

                if (color) {
                    prevColor.set(context.getColor());
                }
            });
        }

        if (!this.lines.isEmpty()) {

            Tessellator.instance.startDrawing(GL11.GL_LINES);

            this.lines.forEach(context -> {

                boolean lines = context.getLineWidth() != prevLineWidth.get();
                boolean color = context.getColor() != prevColor.get();

                if (lines || color) {
                    Tessellator.instance.draw();
                    Tessellator.instance.startDrawing(GL11.GL_LINES);
                }

                context.preload(lines, color);

                context.draw();

                if (lines) {
                    prevLineWidth.set(context.getLineWidth());
                }

                if (color) {
                    prevColor.set(context.getColor());
                }
            });

            Tessellator.instance.draw();
        }
    }

    public void clear() {
        this.strips.clear();
        this.lines.clear();
        this.quads.clear();
    }
}
