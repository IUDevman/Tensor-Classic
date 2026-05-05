package dev.tensor.backend.mixins;

import dev.tensor.backend.MixinPriority;
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
    protected abstract FloatBuffer func_908_a(float f, float g, float h, float i);

    @Inject(method = "getFOVModifier", at = @At("HEAD"), cancellable = true)
    public void tensor_onGetFOVModifier(float par1, CallbackInfoReturnable<Float> cir) {
        if (this.failsNullCheck()) {
            return;
        }

        EventCache.getCachedEvent(FOVModifierEvent.class).withVariables(70.0F).call(event -> {

            if (event.isCancelled()) {
                cir.setReturnValue(event.FOV);
            }
        });
    }

    @Inject(method = "hurtCameraEffect", at = @At("HEAD"), cancellable = true)
    public void tensor_onHurtCameraEffect(float par1, CallbackInfo ci) {
        if (this.failsNullCheck()) {
            return;
        }

        EventCache.getCachedEvent(HurtCameraEvent.class).call(event -> {

            if (event.isCancelled()) {
                ci.cancel();
            }
        });
    }

    @Inject(method = "setupViewBobbing", at = @At("HEAD"), cancellable = true)
    public void tensor_onSetupViewBobbing(float par1, CallbackInfo ci) {
        if (this.failsNullCheck()) {
            return;
        }

        EventCache.getCachedEvent(ViewBobEvent.class).call(event -> {

            if (event.isCancelled()) {
                ci.cancel();
            }
        });
    }

    @Inject(method = "renderWorld", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/RenderGlobal;renderClouds(F)V", shift = At.Shift.AFTER))
    public void tensor_onRenderWorld(float l, long par2, CallbackInfo ci) {
        if (this.failsNullCheck()) {
            return;
        }

        EventCache.getCachedEvent(Render3DEvent.class).call();
    }

    @Inject(method = "setupFog", at = @At("TAIL"))
    public void tensor_onSetupFogTail(int f, float par2, CallbackInfo ci) {
        if (this.failsNullCheck()) {
            return;
        }

        EventCache.getCachedEvent(RenderSkyEvent.class).withVariables(RenderSkyEvent.Type.Fog).call(event -> {

            if (event.isCancelled()) {
                GL11.glFogi(GL11.GL_FOG_END, GL11.GL_LINEAR);

            } else if (event.isUpdated()) {
                Color color = event.getNewColor();

                GL11.glFog(GL11.GL_FOG_COLOR, this.func_908_a(color.getRed() / 255F, color.getGreen() / 255F, color.getBlue(), 1.0F));
            }
        });
    }

    @Redirect(method = "setupFog", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/EntityLiving;isInsideOfMaterial(Lnet/minecraft/src/Material;)Z"))
    public boolean tensor_onSetUpFogInvokeIsInsideOfMaterial(EntityLiving instance, Material material) {
        if (this.failsNullCheck()) {
            return false;
        }

        RenderSkyEvent event = EventCache.getCachedEvent(RenderSkyEvent.class).withVariables(RenderSkyEvent.Type.Fog_Fluids).call();

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

        RenderOverlayEvent event = EventCache.getCachedEvent(RenderOverlayEvent.class).withVariables(RenderOverlayEvent.Type.vomit).call();

        if (event.isCancelled()) {
            return -100;
        }

        return instance.prevTimeInPortal;
    }

    @Redirect(method = "orientCamera", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/Vec3D;distanceTo(Lnet/minecraft/src/Vec3D;)D"))
    public double tensor_onOrientCameraDistanceTo(Vec3D instance, Vec3D vec3D) {
        if (this.failsNullCheck()) {
            return instance.distanceTo(vec3D);
        }

        CameraClipEvent event = EventCache.getCachedEvent(CameraClipEvent.class).call();

        if (event.isCancelled()) {
            return 100;
        }

        return instance.distanceTo(vec3D);
    }

    @Redirect(method = "updateCameraAndRender", at = @At(value = "FIELD", target = "Lnet/minecraft/src/GameSettings;smoothCamera:Z"))
    public boolean tensor_onUpdateCameraAndRenderSmoothCamera(GameSettings instance) {
        if (this.failsNullCheck()) {
            return instance.smoothCamera;
        }

        SmoothCameraEvent event = EventCache.getCachedEvent(SmoothCameraEvent.class).call();

        if (event.isCancelled()) {
            return true;
        } else {
            return instance.smoothCamera;
        }
    }
}
