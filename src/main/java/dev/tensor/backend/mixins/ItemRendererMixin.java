package dev.tensor.backend.mixins;

import dev.tensor.backend.events.RenderOverlayEvent;
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

@Mixin(value = ItemRenderer.class, priority = Integer.MAX_VALUE)
public final class ItemRendererMixin implements Globals {

    @Inject(method = "renderInsideOfBlock", at = @At("HEAD"), cancellable = true)
    public void onRenderInsideOfBlock(float i, int par2, CallbackInfo ci) {
        if (this.failsNullCheck()) {
            return;
        }

        RenderOverlayEvent event = new RenderOverlayEvent(RenderOverlayEvent.Type.blocks);
        this.getEventManager().call(event);

        if (event.isCancelled()) {
            ci.cancel();
        }
    }

    @Inject(method = "renderFireInFirstPerson", at = @At("HEAD"), cancellable = true)
    public void onRenderFireInFirstPerson(float par1, CallbackInfo ci) {
        if (this.failsNullCheck()) {
            return;
        }

        RenderOverlayEvent event = new RenderOverlayEvent(RenderOverlayEvent.Type.fire);
        this.getEventManager().call(event);

        if (event.isCancelled()) {
            ci.cancel();
        }
    }

    @Inject(method = "renderWarpedTextureOverlay", at = @At("HEAD"), cancellable = true)
    public void onRenderWarpedTextureOverlay(float par1, CallbackInfo ci) {
        if (this.failsNullCheck()) {
            return;
        }

        RenderOverlayEvent event = new RenderOverlayEvent(RenderOverlayEvent.Type.water);
        this.getEventManager().call(event);

        if (event.isCancelled()) {
            ci.cancel();
        }
    }
}
