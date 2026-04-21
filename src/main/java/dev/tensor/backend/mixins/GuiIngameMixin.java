package dev.tensor.backend.mixins;

import dev.tensor.backend.MixinPriority;
import dev.tensor.backend.events.EventCache;
import dev.tensor.backend.events.constant.Render2DEvent;
import dev.tensor.backend.events.variable.RenderOverlayEvent;
import dev.tensor.misc.imp.Globals;
import net.minecraft.src.GuiIngame;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author Hoosiers
 * @since 03-20-2026
 */

@Mixin(value = GuiIngame.class, priority = MixinPriority.VALUE)
public final class GuiIngameMixin implements Globals {

    @Inject(method = "renderGameOverlay", at = @At("TAIL"))
    public void tensor_onRenderGameOverlay(float bl, boolean i, int j, int par4, CallbackInfo ci) {
        if (this.failsNullCheck() || this.getInGameGUI() == null) {
            return;
        }

        @SuppressWarnings("unused")
        Render2DEvent event = EventCache.getCachedEvent(Render2DEvent.class).call();
    }

    @Inject(method = "renderVignette", at = @At("HEAD"), cancellable = true)
    public void tensor_onRenderVignette(float i, int j, int par3, CallbackInfo ci) {
        if (this.failsNullCheck()) {
            return;
        }

        RenderOverlayEvent event = EventCache.getCachedEvent(RenderOverlayEvent.class).withVariables(RenderOverlayEvent.Type.vignette).call();

        if (event.isCancelled()) {
            ci.cancel();
        }
    }

    @Inject(method = "renderPumpkinBlur", at = @At("HEAD"), cancellable = true)
    public void tensor_onRenderPumpkinBlur(int j, int par2, CallbackInfo ci) {
        if (this.failsNullCheck()) {
            return;
        }

        RenderOverlayEvent event = EventCache.getCachedEvent(RenderOverlayEvent.class).withVariables(RenderOverlayEvent.Type.pumpkin).call();

        if (event.isCancelled()) {
            ci.cancel();
        }
    }

    @Inject(method = "renderPortalOverlay", at = @At("HEAD"), cancellable = true)
    public void tensor_onRenderPortalOverlay(float i, int j, int par3, CallbackInfo ci) {
        if (this.failsNullCheck()) {
            return;
        }

        RenderOverlayEvent event = EventCache.getCachedEvent(RenderOverlayEvent.class).withVariables(RenderOverlayEvent.Type.portals).call();

        if (event.isCancelled()) {
            ci.cancel();
        }
    }
}
