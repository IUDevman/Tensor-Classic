package dev.tensor.backend.mixins;

import dev.tensor.backend.MixinPriority;
import dev.tensor.backend.events.EventCache;
import dev.tensor.backend.events.constant.RenderItemEvent;
import dev.tensor.backend.events.variable.RenderEntityEvent;
import dev.tensor.misc.imp.Globals;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityItem;
import net.minecraft.src.RenderManager;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author Hoosiers
 * @since 03-29-2026
 */

@Mixin(value = RenderManager.class, priority = MixinPriority.VALUE)
public final class RenderManagerMixin implements Globals {

    @Unique
    private boolean hasChams = false;

    @Inject(method = "renderEntity", at = @At("HEAD"), cancellable = true)
    public void tensor_onRenderEntityHead(Entity f, float par2, CallbackInfo ci) {
        if (this.failsNullCheck()) {
            return;
        }

        if (f == null) {
            return;
        }

        if (f instanceof EntityItem) {
            EventCache.getCachedEvent(RenderItemEvent.class).call(event -> {

                if (event.isCancelled()) {
                    ci.cancel();
                }
            });
        }

        if (this.isXrayEnabled()) {
            return;
        }

        EventCache.getCachedEvent(RenderEntityEvent.class).withVariables(f).call(event -> {

            if (event.isCancelled()) {
                this.hasChams = true;
                GL11.glDepthMask(true);
                GL11.glEnable(GL11.GL_POLYGON_OFFSET_FILL);
                GL11.glDepthRange(0.0, 0.01);
            }
        });
    }

    @Inject(method = "renderEntity", at = @At("TAIL"), cancellable = true)
    public void tensor_onRenderEntityTail(Entity f, float par2, CallbackInfo ci) {
        if (this.failsNullCheck()) {
            return;
        }

        if (this.hasChams) {
            GL11.glDepthMask(false);
            GL11.glDisable(GL11.GL_POLYGON_OFFSET_FILL);
            GL11.glDepthRange(0.0, 1);
            this.hasChams = false;
        }
    }
}
