package dev.tensor.backend.mixins;

import dev.tensor.backend.MixinPriority;
import dev.tensor.backend.events.EventCache;
import dev.tensor.backend.events.variable.RenderOverlayEvent;
import dev.tensor.misc.imp.Globals;
import net.minecraft.src.ItemRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author Hoosiers
 * @since 03-28-2026
 */

@Mixin(value = ItemRenderer.class, priority = MixinPriority.VALUE)
public final class ItemRendererMixin implements Globals {

    @Inject(method = "renderInsideOfBlock", at = @At("HEAD"), cancellable = true)
    public void tensor_onRenderInsideOfBlock(float i, int par2, CallbackInfo ci) {
        if (this.failsNullCheck()) {
            return;
        }

        EventCache.getCachedEvent(RenderOverlayEvent.class).withVariables(RenderOverlayEvent.Type.blocks).call(event -> {

            if (event.isCancelled()) {
                ci.cancel();
            }
        });
    }

    @Inject(method = "renderFireInFirstPerson", at = @At("HEAD"), cancellable = true)
    public void tensor_onRenderFireInFirstPerson(float par1, CallbackInfo ci) {
        if (this.failsNullCheck()) {
            return;
        }

        EventCache.getCachedEvent(RenderOverlayEvent.class).withVariables(RenderOverlayEvent.Type.fire).call(event -> {

            if (event.isCancelled()) {
                ci.cancel();
            }
        });
    }

    @Inject(method = "renderWarpedTextureOverlay", at = @At("HEAD"), cancellable = true)
    public void tensor_onRenderWarpedTextureOverlay(float par1, CallbackInfo ci) {
        if (this.failsNullCheck()) {
            return;
        }

        EventCache.getCachedEvent(RenderOverlayEvent.class).withVariables(RenderOverlayEvent.Type.water).call(event -> {

            if (event.isCancelled()) {
                ci.cancel();
            }
        });
    }
}
