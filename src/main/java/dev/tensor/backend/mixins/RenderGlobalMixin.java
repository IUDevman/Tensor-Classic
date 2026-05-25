package dev.tensor.backend.mixins;

import dev.tensor.backend.MixinPriority;
import dev.tensor.backend.events.EventCache;
import dev.tensor.backend.events.variable.RenderBlockBreakEvent;
import dev.tensor.backend.events.variable.RenderEntityEvent;
import dev.tensor.backend.events.variable.RenderSelectionBoxEvent;
import dev.tensor.backend.events.variable.RenderSkyEvent;
import dev.tensor.feature.hacks.Chams;
import dev.tensor.misc.helpers.extensions.TensorImageBuffer;
import dev.tensor.misc.imp.Globals;
import net.minecraft.src.*;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;

/**
 * @author Hoosiers
 * @since 03-27-2026
 */

@Mixin(value = RenderGlobal.class, priority = MixinPriority.VALUE)
public final class RenderGlobalMixin implements Globals {

    @Shadow
    private RenderEngine renderEngine;

    @Inject(method = "renderClouds", at = @At("HEAD"), cancellable = true)
    public void tensor_onRenderClouds(float par1, CallbackInfo ci) {
        if (this.failsNullCheck()) {
            return;
        }

        if (this.isXrayEnabled() && !this.getXray().clouds.getValue()) {
            ci.cancel();
            return;
        }

        if (!this.shouldCallEvent(RenderSkyEvent.class)) {
            return;
        }

        EventCache.getCachedEvent(RenderSkyEvent.class).withVariables(RenderSkyEvent.Type.Clouds).call(true, event -> {

            if (event.isCancelled()) {
                ci.cancel();
            }
        });
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

        if (!this.shouldCallEvent(RenderSkyEvent.class)) {
            return;
        }

        EventCache.getCachedEvent(RenderSkyEvent.class).withVariables(RenderSkyEvent.Type.Clouds).call(true, event -> {

            if (event.isUpdated()) {
                Color color = event.getNewColor();

                Tessellator.instance.setColorRGBA_F(color.getRed() / 255F, color.getGreen() / 255F, color.getBlue() / 255F, 0.8F);
            }
        });
    }

    @Inject(method = "renderCloudsFancy", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/Tessellator;setColorRGBA_F(FFFF)V", shift = At.Shift.AFTER))
    public void tensor_onRenderCloudsFancyInvoke(float par1, CallbackInfo ci) {
        if (this.failsNullCheck()) {
            return;
        }

        if (this.getWorld().worldProvider.isHellWorld) {
            return;
        }

        if (!this.shouldCallEvent(RenderSkyEvent.class)) {
            return;
        }

        EventCache.getCachedEvent(RenderSkyEvent.class).withVariables(RenderSkyEvent.Type.Clouds).call(true, event -> {

            if (event.isUpdated()) {
                Color color = event.getNewColor();

                Tessellator.instance.setColorRGBA_F(color.getRed() / 255F, color.getGreen() / 255F, color.getBlue() / 255F, 0.8F);
            }
        });
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

        if (arg3 == 0 && i.typeOfHit == EnumMovingObjectType.TILE && this.shouldCallEvent(RenderSelectionBoxEvent.class)) {
            EventCache.getCachedEvent(RenderSelectionBoxEvent.class).withVariables(i).call(true, event -> {

                if (event.isCancelled()) {
                    ci.cancel();
                }
            });
        }
    }

    @Inject(method = "drawBlockBreaking", at = @At("HEAD"), cancellable = true)
    public void tensor_onDrawBlockBreaking(EntityPlayer arg2, MovingObjectPosition i, int arg3, ItemStack f, float par5, CallbackInfo ci) {
        if (this.failsNullCheck()) {
            return;
        }

        if (arg3 == 0 && i.typeOfHit == EnumMovingObjectType.TILE && this.shouldCallEvent(RenderBlockBreakEvent.class)) {
            EventCache.getCachedEvent(RenderBlockBreakEvent.class).withVariables(i).call(true, event -> {

                if (event.isCancelled()) {
                    ci.cancel();
                }
            });
        }
    }

    @Redirect(method = "obtainEntitySkin", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/RenderEngine;obtainImageData(Ljava/lang/String;Lnet/minecraft/src/ImageBuffer;)Lnet/minecraft/src/ThreadDownloadImageData;", opcode = 1))
    public ThreadDownloadImageData tensor_onObtainEntitySkinImageBuffer(RenderEngine instance, String arg, ImageBuffer imageBuffer) {
        if (arg.endsWith("files/tensor/cape.png")) {
            return this.renderEngine.obtainImageData(arg, new TensorImageBuffer());

        } else {
            return this.renderEngine.obtainImageData(arg, new ImageBufferDownload());
        }
    }

    @Inject(method = "renderEntities", at = @At("HEAD"), cancellable = true)
    public void tensor_onRenderEntities(Vec3D arg2, ICamera f, float par3, CallbackInfo ci) {
        if (this.failsNullCheck()) {
            return;
        }

        Chams chams = this.getHackManager().getHack(Chams.class);

        if (chams != null && chams.isEnabled()) {
            chams.resetEntityCache();
        }
    }

    @Redirect(method = "renderEntities", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/Entity;isInRangeToRenderVec3D(Lnet/minecraft/src/Vec3D;)Z"))
    public boolean tensor_onRenderEntitiesWithinRange(Entity instance, Vec3D vec3D) {
        if (this.failsNullCheck()) {
            return instance.isInRangeToRenderVec3D(vec3D);
        }

        if (this.isXrayEnabled()) {
            return instance.isInRangeToRenderVec3D(vec3D);
        }

        if (!this.shouldCallEvent(RenderEntityEvent.class)) {
            return instance.isInRangeToRenderVec3D(vec3D);
        }

        RenderEntityEvent event = EventCache.getCachedEvent(RenderEntityEvent.class).withVariables(instance).call(true);

        if (event.isCancelled()) {
            return true;
        }

        return instance.isInRangeToRenderVec3D(vec3D);
    }
}
