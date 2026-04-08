package dev.tensor.backend.mixins;

import dev.tensor.backend.MixinPriority;
import dev.tensor.backend.events.*;
import dev.tensor.misc.imp.Globals;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityPlayerSP;
import net.minecraft.src.EntityRenderer;
import net.minecraft.src.Material;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.awt.*;
import java.nio.FloatBuffer;

/**
 * @author Hoosiers
 * @since 03-18-2026
 */

@Mixin(value = EntityRenderer.class, priority = MixinPriority.VALUE)
public abstract class EntityRendererMixin implements Globals {

    @Shadow
    protected abstract FloatBuffer func_908_a(float f, float g, float h, float i);

    @Inject(method = "getFOVModifier", at = @At("HEAD"), cancellable = true)
    public void tensor_onGetFOVModifier(float par1, CallbackInfoReturnable<Float> cir) {
        if (this.failsNullCheck()) {
            return;
        }

        FOVModifierEvent event = new FOVModifierEvent().call();

        if (event.isCancelled()) {
            cir.setReturnValue(event.FOV);
        }
    }

    @Inject(method = "hurtCameraEffect", at = @At("HEAD"), cancellable = true)
    public void tensor_onHurtCameraEffect(float par1, CallbackInfo ci) {
        if (this.failsNullCheck()) {
            return;
        }

        HurtCameraEvent event = new HurtCameraEvent().call();

        if (event.isCancelled()) {
            ci.cancel();
        }
    }

    @Inject(method = "setupViewBobbing", at = @At("HEAD"), cancellable = true)
    public void tensor_onSetupViewBobbing(float par1, CallbackInfo ci) {
        if (this.failsNullCheck()) {
            return;
        }

        ViewBobEvent event = new ViewBobEvent().call();

        if (event.isCancelled()) {
            ci.cancel();
        }
    }

    @Inject(method = "renderWorld", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/EntityRenderer;renderRainSnow(F)V", shift = At.Shift.BEFORE))
    public void tensor_onRenderWorld(float l, long par2, CallbackInfo ci) {
        if (this.failsNullCheck()) {
            return;
        }

        @SuppressWarnings("unused")
        Render3DEvent event = new Render3DEvent().call();
    }

    @Inject(method = "setupFog", at = @At("TAIL"))
    public void tensor_onSetupFogTail(int f, float par2, CallbackInfo ci) {
        if (this.failsNullCheck()) {
            return;
        }

        RenderSkyEvent event = new RenderSkyEvent(RenderSkyEvent.Type.Fog).call();

        if (event.isCancelled()) {
            GL11.glFogi(GL11.GL_FOG_END, GL11.GL_LINEAR);
        } else if (event.isUpdated()) {
            Color color = event.getNewColor();

            GL11.glFog(GL11.GL_FOG_COLOR, this.func_908_a(color.getRed() / 255F, color.getGreen() / 255F, color.getBlue(), 1.0F));
        }
    }

    //Slick redirect
    @Redirect(method = "setupFog", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/EntityLiving;isInsideOfMaterial(Lnet/minecraft/src/Material;)Z"))
    public boolean tensor_onSetUpFogInvokeIsInsideOfMaterial(EntityLiving instance, Material material) {
        if (this.failsNullCheck()) {
            return false;
        }

        RenderSkyEvent event = new RenderSkyEvent(RenderSkyEvent.Type.Fog_Fluids).call();

        if (event.isCancelled()) {
            return false;
        }

        return instance.isInsideOfMaterial(material);
    }

    @Redirect(method = "setupCameraTransform", at = @At(value = "FIELD", target = "Lnet/minecraft/src/EntityPlayerSP;prevTimeInPortal:F"))
    public float tensor_onSetupCameraTransformPortalVomit(EntityPlayerSP instance) {
        if (this.failsNullCheck()) {
            return -100;
        }

        RenderOverlayEvent event = new RenderOverlayEvent(RenderOverlayEvent.Type.vomit).call();

        if (event.isCancelled()) {
            return -100;
        }

        return instance.prevTimeInPortal;
    }
}
