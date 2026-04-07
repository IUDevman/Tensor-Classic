package dev.tensor.misc.imp.hack.render;

import dev.tensor.misc.imp.render.Render3D;

/**
 * @author Hoosiers
 * @since 04-06-2026
 */

public interface Render3DObject extends Render3D {

    int getRenderOrder();

    void onRender3D();

    default void renderIn3D() {
        this.startBlockGL();

        this.onRender3D();

        this.finishBlockGL();
    }
}
