package dev.tensor.misc.imp.hack.render;

import dev.tensor.misc.imp.hack.Hack;
import dev.tensor.misc.imp.render.Render3D;

/**
 * @author Hoosiers
 * @since 04-06-2026
 */

public abstract class Render3DHack extends Hack implements Render3D {

    public RenderOrder getRenderOrder() {
        return RenderOrder.Default;
    }

    public void onRender3D(float renderPartialTicks) {

    }

    public void renderIn3D(float renderPartialTicks) {
        this.startBlockGL();

        this.onRender3D(renderPartialTicks);

        this.finishBlockGL();
    }
}
