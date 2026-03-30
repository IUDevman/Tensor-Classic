package dev.tensor.backend.mixins;

import dev.tensor.backend.events.ChatReceivedEvent;
import dev.tensor.backend.events.Render2DEvent;
import dev.tensor.backend.events.RenderOverlayEvent;
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

@Mixin(value = GuiIngame.class, priority = Integer.MAX_VALUE)
public final class GuiIngameMixin implements Globals {

    @Inject(method = "renderGameOverlay", at = @At("TAIL"))
    public void onRenderGameOverlay(float bl, boolean i, int j, int par4, CallbackInfo ci) {
        if (this.failsNullCheck() || this.getInGameGUI() == null) {
            return;
        }

        Render2DEvent event = new Render2DEvent();
        this.getEventManager().call(event);
    }

    @Inject(method = "addChatMessage", at = @At("HEAD"))
    public void onAddChatMessage(String par1, CallbackInfo ci) {
        if (this.failsNullCheck()) {
            return;
        }

        ChatReceivedEvent event = new ChatReceivedEvent(par1);
        this.getEventManager().call(event);
    }

    @Inject(method = "renderVignette", at = @At("HEAD"), cancellable = true)
    public void onRenderVignette(float i, int j, int par3, CallbackInfo ci) {
        if (this.failsNullCheck()) {
            return;
        }

        RenderOverlayEvent event = new RenderOverlayEvent(RenderOverlayEvent.Type.vignette);
        this.getEventManager().call(event);

        if (event.isCancelled()) {
            ci.cancel();
        }
    }

    @Inject(method = "renderPumpkinBlur", at = @At("HEAD"), cancellable = true)
    public void onRenderPumpkinBlur(int j, int par2, CallbackInfo ci) {
        if (this.failsNullCheck()) {
            return;
        }

        RenderOverlayEvent event = new RenderOverlayEvent(RenderOverlayEvent.Type.pumpkin);
        this.getEventManager().call(event);

        if (event.isCancelled()) {
            ci.cancel();
        }
    }

    @Inject(method = "renderPortalOverlay", at = @At("HEAD"), cancellable = true)
    public void onRenderPortalOverlay(float i, int j, int par3, CallbackInfo ci) {
        if (this.failsNullCheck()) {
            return;
        }

        RenderOverlayEvent event = new RenderOverlayEvent(RenderOverlayEvent.Type.portals);
        this.getEventManager().call(event);

        if (event.isCancelled()) {
            ci.cancel();
        }
    }
}
