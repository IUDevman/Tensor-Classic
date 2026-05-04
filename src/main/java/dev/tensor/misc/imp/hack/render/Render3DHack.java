package dev.tensor.misc.imp.hack.render;

import dev.tensor.misc.imp.hack.Hack;
import dev.tensor.misc.imp.render.Render3D;
import dev.tensor.misc.imp.render.task.RenderTask;

/**
 * @author Hoosiers
 * @since 04-06-2026
 */

public abstract class Render3DHack extends Hack implements Render3D {

    public RenderOrder getRenderOrder() {
        return RenderOrder.Default;
    }

    public boolean overridesBlockGL() {
        return false;
    }

    public void onRender3D(float renderPartialTicks) {

    }

    public void renderIn3D(float renderPartialTicks) {
        this.startBlockGL();

        if (RenderTask.RENDER_TASK == null) {
            RenderTask.RENDER_TASK = new RenderTask();
        }

        this.onRender3D(renderPartialTicks);

        if (!this.overridesBlockGL()) {
            RenderTask.RENDER_TASK.render();
        }

        RenderTask.RENDER_TASK.clear();

        this.finishBlockGL();
    }
}
