package dev.tensor.misc.imp.plugin.hack;

import dev.tensor.misc.imp.hack.render.Render3DHack;
import dev.tensor.misc.imp.hack.render.RenderOrder;

import java.lang.annotation.*;

/**
 * @author Hoosiers
 * @since 05-10-2026
 */

@SuppressWarnings("unused")
public class PluginRender3DHack extends Render3DHack implements PluginHack {

    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    public @interface PluginRenderOrder3D {
        int renderPriority() default RenderOrder.DEFAULT_3D;
    }

    private PluginRenderOrder3D getPluginRenderOrder() {
        return this.getClass().getAnnotation(PluginRender3DHack.PluginRenderOrder3D.class);
    }

    @Override
    public int getRenderOrderInt() {
        return this.getPluginRenderOrder().renderPriority();
    }
}
