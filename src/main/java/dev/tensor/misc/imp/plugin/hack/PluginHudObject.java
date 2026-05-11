package dev.tensor.misc.imp.plugin.hack;

import dev.tensor.misc.imp.hack.Hack;
import dev.tensor.misc.imp.hack.render.HUDObject;
import dev.tensor.misc.imp.hack.render.RenderOrder;

import java.lang.annotation.*;

/**
 * @author Hoosiers
 * @since 05-10-2026
 */

@SuppressWarnings("unused")
public abstract class PluginHudObject extends Hack implements PluginHack, HUDObject {

    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    public @interface PluginRenderOrder2D {
        int renderPriority() default RenderOrder.DEFAULT_2D;
    }

    private PluginRenderOrder2D getPluginRenderOrder() {
        return this.getClass().getAnnotation(PluginHudObject.PluginRenderOrder2D.class);
    }

    @Override
    public int getRenderOrderInt() {
        return this.getPluginRenderOrder().renderPriority();
    }
}
