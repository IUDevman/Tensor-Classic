package dev.tensor.backend.mixins;

import dev.tensor.backend.MixinPriority;
import dev.tensor.backend.TensorRedirect;
import dev.tensor.backend.events.EventCache;
import dev.tensor.backend.events.constant.*;
import dev.tensor.backend.events.variable.FOVModifierEvent;
import dev.tensor.backend.events.variable.RenderOverlayEvent;
import dev.tensor.backend.events.variable.RenderSkyEvent;
import dev.tensor.misc.imp.Globals;
import net.minecraft.src.*;
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
    protected abstract FloatBuffer func_908_a(float red, float green, float blue, float alpha);

    @Inject(method = "getFOVModifier", at = @At("HEAD"), cancellable = true)
    public void tensor_onGetFOVModifier(float partialTicks, CallbackInfoReturnable<Float> cir) {
        if (this.failsNullCheck()) {
            return;
        }

        if (!this.shouldCallEvent(FOVModifierEvent.class)) {
            return;
        }

        EventCache.getCachedEvent(FOVModifierEvent.class).withVariables(70.0F).call(true, event -> {

            if (event.isCancelled()) {
                cir.setReturnValue(event.FOV);
            }
        });
    }

    @Inject(method = "hurtCameraEffect", at = @At("HEAD"), cancellable = true)
    public void tensor_onHurtCameraEffect(float partialTicks, CallbackInfo ci) {
        if (this.failsNullCheck()) {
            return;
        }

        if (!this.shouldCallEvent(HurtCameraEvent.class)) {
            return;
        }

        EventCache.getCachedEvent(HurtCameraEvent.class).call(true, event -> {

            if (event.isCancelled()) {
                ci.cancel();
            }
        });
    }

    @Inject(method = "setupViewBobbing", at = @At("HEAD"), cancellable = true)
    public void tensor_onSetupViewBobbing(float partialTicks, CallbackInfo ci) {
        if (this.failsNullCheck()) {
            return;
        }

        if (!this.shouldCallEvent(ViewBobEvent.class)) {
            return;
        }

        EventCache.getCachedEvent(ViewBobEvent.class).call(true, event -> {

            if (event.isCancelled()) {
                ci.cancel();
            }
        });
    }

    @Inject(method = "renderWorld", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/RenderGlobal;renderClouds(F)V", shift = At.Shift.AFTER))
    public void tensor_onRenderWorld(float partialTicks, long time, CallbackInfo ci) {
        if (this.failsNullCheck()) {
            return;
        }

        EventCache.getCachedEvent(Render3DEvent.class).call(false);
    }

    @Inject(method = "setupFog", at = @At("TAIL"))
    public void tensor_onSetupFogTail(int distance, float partialTicks, CallbackInfo ci) {
        if (this.failsNullCheck()) {
            return;
        }

        if (!this.shouldCallEvent(RenderSkyEvent.class)) {
            return;
        }

        EventCache.getCachedEvent(RenderSkyEvent.class).withVariables(RenderSkyEvent.Type.Fog).call(true, event -> {

            if (event.isCancelled()) {
                GL11.glFogi(GL11.GL_FOG_END, GL11.GL_LINEAR);

            } else if (event.isUpdated()) {
                Color color = event.getNewColor();

                GL11.glFog(GL11.GL_FOG_COLOR, this.func_908_a(color.getRed() / 255F, color.getGreen() / 255F, color.getBlue(), 1.0F));
            }
        });
    }

    @TensorRedirect
    @Redirect(method = "setupFog", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/EntityLiving;isInsideOfMaterial(Lnet/minecraft/src/Material;)Z"))
    public boolean tensor_onSetUpFogInvokeIsInsideOfMaterial(EntityLiving entityLiving, Material material) {
        if (this.failsNullCheck()) {
            return false;
        }

        if (!this.shouldCallEvent(RenderSkyEvent.class)) {
            return entityLiving.isInsideOfMaterial(material);
        }

        RenderSkyEvent event = EventCache.getCachedEvent(RenderSkyEvent.class).withVariables(RenderSkyEvent.Type.Fog_Fluids).call(true);

        if (event.isCancelled()) {
            return false;
        }

        return entityLiving.isInsideOfMaterial(material);
    }

    @TensorRedirect
    @Redirect(method = "setupCameraTransform", at = @At(value = "FIELD", target = "Lnet/minecraft/src/EntityPlayerSP;prevTimeInPortal:F"))
    public float tensor_onSetupCameraTransformPortalVomit(EntityPlayerSP entityPlayerSP) {
        if (this.failsNullCheck()) {
            return -100;
        }

        if (!this.shouldCallEvent(RenderOverlayEvent.class)) {
            return entityPlayerSP.prevTimeInPortal;
        }

        RenderOverlayEvent event = EventCache.getCachedEvent(RenderOverlayEvent.class).withVariables(RenderOverlayEvent.Type.vomit).call(true);

        if (event.isCancelled()) {
            return -100;
        }

        return entityPlayerSP.prevTimeInPortal;
    }

    @TensorRedirect
    @Redirect(method = "orientCamera", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/Vec3D;distanceTo(Lnet/minecraft/src/Vec3D;)D"))
    public double tensor_onOrientCameraDistanceTo(Vec3D movingObjectPosition, Vec3D target) {
        if (this.failsNullCheck()) {
            return movingObjectPosition.distanceTo(target);
        }

        if (!this.shouldCallEvent(CameraClipEvent.class)) {
            return movingObjectPosition.distanceTo(target);
        }

        CameraClipEvent event = EventCache.getCachedEvent(CameraClipEvent.class).call(true);

        if (event.isCancelled()) {
            return 100;
        }

        return movingObjectPosition.distanceTo(target);
    }

    @TensorRedirect
    @Redirect(method = "updateCameraAndRender", at = @At(value = "FIELD", target = "Lnet/minecraft/src/GameSettings;smoothCamera:Z"))
    public boolean tensor_onUpdateCameraAndRenderSmoothCamera(GameSettings gameSettings) {
        if (this.failsNullCheck()) {
            return gameSettings.smoothCamera;
        }

        if (!this.shouldCallEvent(SmoothCameraEvent.class)) {
            return gameSettings.smoothCamera;
        }

        SmoothCameraEvent event = EventCache.getCachedEvent(SmoothCameraEvent.class).call(true);

        if (event.isCancelled()) {
            return true;
        } else {
            return gameSettings.smoothCamera;
        }
    }
}
