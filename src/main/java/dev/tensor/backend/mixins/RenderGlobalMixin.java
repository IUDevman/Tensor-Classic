package dev.tensor.backend.mixins;

import dev.tensor.backend.MixinPriority;
import dev.tensor.backend.events.EventCache;
import dev.tensor.backend.events.variable.RenderBlockBreakEvent;
import dev.tensor.backend.events.variable.RenderSelectionBoxEvent;
import dev.tensor.backend.events.variable.RenderSkyEvent;
import dev.tensor.misc.imp.Globals;
import net.minecraft.src.*;
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

        RenderSkyEvent event = EventCache.getCachedEvent(RenderSkyEvent.class).withVariables(RenderSkyEvent.Type.Clouds).call();

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

        RenderSkyEvent event = EventCache.getCachedEvent(RenderSkyEvent.class).withVariables(RenderSkyEvent.Type.Clouds).call();

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

        RenderSkyEvent event = EventCache.getCachedEvent(RenderSkyEvent.class).withVariables(RenderSkyEvent.Type.Clouds).call();

        if (event.isUpdated()) {
            Color color = event.getNewColor();

            Tessellator.instance.setColorRGBA_F(color.getRed() / 255F, color.getGreen() / 255F, color.getBlue() / 255F, 0.8F);
        }
    }

    @Inject(method = "renderCloudsFancy", at = @At("HEAD"))
    public void tensor_fixGLGlitchHead(float par1, CallbackInfo ci) {
        GL11.glPushMatrix();
    }

    @Inject(method = "renderCloudsFancy", at = @At("TAIL"))
    public void tensor_fixGLGlitchTail(float par1, CallbackInfo ci) {
        GL11.glPopMatrix();
    }

    @Inject(method = "drawSelectionBox", at = @At("HEAD"), cancellable = true)
    public void tensor_onDrawSelectionBox(EntityPlayer arg2, MovingObjectPosition i, int arg3, ItemStack f, float par5, CallbackInfo ci) {
        if (this.failsNullCheck()) {
            return;
        }

        if (arg3 == 0 && i.typeOfHit == EnumMovingObjectType.TILE) {
            RenderSelectionBoxEvent event = EventCache.getCachedEvent(RenderSelectionBoxEvent.class).withVariables(i).call();

            if (event.isCancelled()) {
                ci.cancel();
            }
        }
    }

    @Inject(method = "drawBlockBreaking", at = @At("HEAD"), cancellable = true)
    public void tensor_onDrawBlockBreaking(EntityPlayer arg2, MovingObjectPosition i, int arg3, ItemStack f, float par5, CallbackInfo ci) {
        if (this.failsNullCheck()) {
            return;
        }

        if (arg3 == 0 && i.typeOfHit == EnumMovingObjectType.TILE) {
            RenderBlockBreakEvent event = EventCache.getCachedEvent(RenderBlockBreakEvent.class).withVariables(i).call();

            if (event.isCancelled()) {
                ci.cancel();
            }
        }
    }
}
