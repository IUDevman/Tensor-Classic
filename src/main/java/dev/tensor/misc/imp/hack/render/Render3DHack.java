package dev.tensor.misc.imp.hack.render;

import dev.tensor.misc.imp.hack.Hack;
import dev.tensor.misc.imp.render.Render3D;
import dev.tensor.misc.imp.render.task.RenderTask;

/**
 * @author Hoosiers
 * @since 04-06-2026
 */

public abstract class Render3DHack extends Hack implements Render3D {

    public int getRenderOrderInt() {
        return this.getRenderOrder().value();
    }

    public RenderOrder getRenderOrder() {
        return RenderOrder.Default;
    }

    public boolean overridesBlockGL() {
        return false;
    }

    protected void onRender3D(float renderPartialTicks) {

    }

    public final void renderIn3D(float renderPartialTicks) {
        this.startBlockGL();

        this.onRender3D(renderPartialTicks);

        if (!this.overridesBlockGL()) {
            RenderTask.getInstance().render();
        }

        RenderTask.getInstance().clear();

        this.finishBlockGL();
    }
}
