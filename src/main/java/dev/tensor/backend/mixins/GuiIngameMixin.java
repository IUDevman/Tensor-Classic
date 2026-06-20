package dev.tensor.backend.mixins;

import dev.tensor.backend.MixinPriority;
import dev.tensor.backend.events.EventCache;
import dev.tensor.backend.events.constant.Render2DEvent;
import dev.tensor.backend.events.variable.RenderOverlayEvent;
import dev.tensor.misc.imp.Globals;
import net.minecraft.client.Minecraft;
import net.minecraft.src.GuiIngame;
import net.minecraft.src.ItemStack;
import net.minecraft.src.ScaledResolution;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author Hoosiers
 * @since 03-20-2026
 */

@Mixin(value = GuiIngame.class, priority = MixinPriority.VALUE)
public abstract class GuiIngameMixin implements Globals {

    @Shadow
    private Minecraft mc;

    @Shadow
    protected abstract void renderInventorySlot(int slot, int width, int height, float partialTicks);

    @Inject(method = "renderGameOverlay", at = @At("TAIL"))
    public void tensor_onRenderGameOverlay(float partialTicks, boolean flag, int x, int y, CallbackInfo ci) {
        if (this.failsNullCheck() || this.getInGameGUI() == null) {
            return;
        }

        EventCache.getCachedEvent(Render2DEvent.class).call(false);
    }

    @Inject(method = "renderVignette", at = @At("HEAD"), cancellable = true)
    public void tensor_onRenderVignette(float partialTicks, int x, int y, CallbackInfo ci) {
        if (this.failsNullCheck()) {
            return;
        }

        if (!this.shouldCallEvent(RenderOverlayEvent.class)) {
            return;
        }

        EventCache.getCachedEvent(RenderOverlayEvent.class).withVariables(RenderOverlayEvent.Type.VIGNETTE).call(true, event -> {

            if (event.isCancelled()) {
                ci.cancel();
            }
        });
    }

    @Inject(method = "renderPumpkinBlur", at = @At("HEAD"), cancellable = true)
    public void tensor_onRenderPumpkinBlur(int x, int y, CallbackInfo ci) {
        if (this.failsNullCheck()) {
            return;
        }

        if (!this.shouldCallEvent(RenderOverlayEvent.class)) {
            return;
        }

        EventCache.getCachedEvent(RenderOverlayEvent.class).withVariables(RenderOverlayEvent.Type.PUMPKIN).call(true, event -> {

            if (event.isCancelled()) {
                ci.cancel();
            }
        });
    }

    @Inject(method = "renderPortalOverlay", at = @At("HEAD"), cancellable = true)
    public void tensor_onRenderPortalOverlay(float partialTicks, int x, int y, CallbackInfo ci) {
        if (this.failsNullCheck()) {
            return;
        }

        if (!this.shouldCallEvent(RenderOverlayEvent.class)) {
            return;
        }

        EventCache.getCachedEvent(RenderOverlayEvent.class).withVariables(RenderOverlayEvent.Type.PORTAL).call(true, event -> {
            if (event.isCancelled()) {
                ci.cancel();
            }
        });
    }

    @Inject(method = "renderGameOverlay", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/RenderHelper;disableStandardItemLighting()V", shift = At.Shift.BEFORE))
    public void tensor_onRenderGameOverlaySlot9Fix(float partialTicks, boolean flag, int x, int y, CallbackInfo ci) {
        if (this.failsNullCheck()) {
            return;
        }

        if (this.getInventory().currentItem != 9) {
            return;
        }

        ItemStack slot9 = this.getInventory().getStackInSlot(9);

        if (slot9 == null) {
            return;
        }

        ScaledResolution sr = new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
        this.renderInventorySlot(9, sr.getScaledWidth() / 2 - 90 + 9 * 20 + 2, sr.getScaledHeight() - 16 - 3, partialTicks);
    }
}
