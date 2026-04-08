package dev.tensor.backend.mixins;

import dev.tensor.backend.MixinPriority;
import dev.tensor.backend.events.RenderSkyEvent;
import dev.tensor.misc.imp.Globals;
import net.minecraft.src.RenderGlobal;
import net.minecraft.src.Tessellator;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;

/**
 * @author Hoosiers
 * @since 03-27-2026
 */

@Mixin(value = RenderGlobal.class, priority = MixinPriority.VALUE)
public final class RenderGlobalMixin implements Globals {

    @Inject(method = "renderClouds", at = @At("HEAD"), cancellable = true)
    public void tensor_onRenderClouds(float par1, CallbackInfo ci) {
        if (this.failsNullCheck()) {
            return;
        }

        if (this.isXrayEnabled() && this.getXray().opacity.getValue()) {
            ci.cancel();
            return;
        }

        RenderSkyEvent event = new RenderSkyEvent(RenderSkyEvent.Type.Clouds).call();

        if (event.isCancelled()) {
            ci.cancel();
        }
    }

    @Inject(method = "renderClouds", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/Tessellator;setColorRGBA_F(FFFF)V", shift = At.Shift.AFTER))
    public void tensor_onRenderCloudsInvoke(float par1, CallbackInfo ci) {
        if (this.failsNullCheck()) {
            return;
        }

        if (this.getMinecraft().gameSettings.fancyGraphics) {
            return;
        }

        if (this.getWorld().worldProvider.isHellWorld) {
            return;
        }

        RenderSkyEvent event = new RenderSkyEvent(RenderSkyEvent.Type.Clouds).call();

        if (event.isUpdated()) {
            Color color = event.getNewColor();

            Tessellator.instance.setColorRGBA_F(color.getRed() / 255F, color.getGreen() / 255F, color.getBlue() / 255F, 0.8F);
        }
    }

    @Inject(method = "renderCloudsFancy", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/Tessellator;setColorRGBA_F(FFFF)V", shift = At.Shift.AFTER))
    public void tensor_onRenderCloudsFancyInvoke(float par1, CallbackInfo ci) {
        if (this.failsNullCheck()) {
            return;
        }

        if (this.getWorld().worldProvider.isHellWorld) {
            return;
        }

        RenderSkyEvent event = new RenderSkyEvent(RenderSkyEvent.Type.Clouds).call();

        if (event.isUpdated()) {
            Color color = event.getNewColor();

            Tessellator.instance.setColorRGBA_F(color.getRed() / 255F, color.getGreen() / 255F, color.getBlue() / 255F, 0.8F);
        }
    }

    @Inject(method = "renderCloudsFancy", at = @At("HEAD"))
    public void fixGLGlitchHead(float par1, CallbackInfo ci) {
        GL11.glPushMatrix();
    }

    @Inject(method = "renderCloudsFancy", at = @At("TAIL"))
    public void fixGLGlitchTail(float par1, CallbackInfo ci) {
        GL11.glPopMatrix();
    }
}
