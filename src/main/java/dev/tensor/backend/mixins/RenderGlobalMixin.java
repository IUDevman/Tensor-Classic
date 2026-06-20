package dev.tensor.backend.mixins;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.tensor.backend.MixinPriority;
import dev.tensor.backend.TensorRedirect;
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
    public void tensor_onRenderClouds(float partialTicks, CallbackInfo ci) {
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

        EventCache.getCachedEvent(RenderSkyEvent.class).withVariables(RenderSkyEvent.Type.CLOUDS).call(true, event -> {

            if (event.isCancelled()) {
                ci.cancel();
            }
        });
    }

    @Inject(method = "renderClouds", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/Tessellator;setColorRGBA_F(FFFF)V", shift = At.Shift.AFTER))
    public void tensor_onRenderCloudsInvoke(float partialTicks, CallbackInfo ci) {
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

        EventCache.getCachedEvent(RenderSkyEvent.class).withVariables(RenderSkyEvent.Type.CLOUDS).call(true, event -> {

            if (event.isUpdated()) {
                Color color = event.getNewColor();

                Tessellator.instance.setColorRGBA_F(color.getRed() / 255F, color.getGreen() / 255F, color.getBlue() / 255F, 0.8F);
            }
        });
    }

    @Inject(method = "renderCloudsFancy", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/Tessellator;setColorRGBA_F(FFFF)V", shift = At.Shift.AFTER))
    public void tensor_onRenderCloudsFancyInvoke(float partialTicks, CallbackInfo ci) {
        if (this.failsNullCheck()) {
            return;
        }

        if (this.getWorld().worldProvider.isHellWorld) {
            return;
        }

        if (!this.shouldCallEvent(RenderSkyEvent.class)) {
            return;
        }

        EventCache.getCachedEvent(RenderSkyEvent.class).withVariables(RenderSkyEvent.Type.CLOUDS).call(true, event -> {

            if (event.isUpdated()) {
                Color color = event.getNewColor();

                Tessellator.instance.setColorRGBA_F(color.getRed() / 255F, color.getGreen() / 255F, color.getBlue() / 255F, 0.8F);
            }
        });
    }

    @Inject(method = "renderCloudsFancy", at = @At("HEAD"))
    public void tensor_fixGLGlitchHead(float partialTicks, CallbackInfo ci) {
        GL11.glPushMatrix();
    }

    @Inject(method = "renderCloudsFancy", at = @At("TAIL"))
    public void tensor_fixGLGlitchTail(float partialTicks, CallbackInfo ci) {
        GL11.glPopMatrix();
    }

    @Inject(method = "drawSelectionBox", at = @At("HEAD"), cancellable = true)
    public void tensor_onDrawSelectionBox(EntityPlayer entityPlayer, MovingObjectPosition movingObjectPosition, int allFaces, ItemStack itemStack, float partialTicks, CallbackInfo ci) {
        if (this.failsNullCheck()) {
            return;
        }

        if (allFaces == 0 && movingObjectPosition.typeOfHit == EnumMovingObjectType.TILE && this.shouldCallEvent(RenderSelectionBoxEvent.class)) {
            EventCache.getCachedEvent(RenderSelectionBoxEvent.class).withVariables(movingObjectPosition).call(true, event -> {

                if (event.isCancelled()) {
                    ci.cancel();
                }
            });
        }
    }

    @Inject(method = "drawBlockBreaking", at = @At("HEAD"), cancellable = true)
    public void tensor_onDrawBlockBreaking(EntityPlayer entityPlayer, MovingObjectPosition movingObjectPosition, int allFaces, ItemStack itemStack, float partialTicks, CallbackInfo ci) {
        if (this.failsNullCheck()) {
            return;
        }

        if (allFaces == 0 && movingObjectPosition.typeOfHit == EnumMovingObjectType.TILE && this.shouldCallEvent(RenderBlockBreakEvent.class)) {
            EventCache.getCachedEvent(RenderBlockBreakEvent.class).withVariables(movingObjectPosition).call(true, event -> {

                if (event.isCancelled()) {
                    ci.cancel();
                }
            });
        }
    }

    @TensorRedirect
    @WrapOperation(method = "obtainEntitySkin", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/RenderEngine;obtainImageData(Ljava/lang/String;Lnet/minecraft/src/ImageBuffer;)Lnet/minecraft/src/ThreadDownloadImageData;", opcode = 1))
    public ThreadDownloadImageData tensor_onObtainEntitySkinImageBuffer(RenderEngine renderEngine, String skinURL, ImageBuffer imageBuffer, Operation<ThreadDownloadImageData> original) {
        if (skinURL.endsWith("files/tensor/cape.png")) {
            return renderEngine.obtainImageData(skinURL, new TensorImageBuffer());

        } else {
            return renderEngine.obtainImageData(skinURL, new ImageBufferDownload());
        }
    }

    @Inject(method = "renderEntities", at = @At("HEAD"))
    public void tensor_onRenderEntities(Vec3D player, ICamera camera, float partialTicks, CallbackInfo ci) {
        if (this.failsNullCheck()) {
            return;
        }

        Chams chams = this.getHackManager().getHack(Chams.class);

        if (chams != null && chams.isEnabled()) {
            chams.resetEntityCache();
        }
    }

    @TensorRedirect
    @WrapOperation(method = "renderEntities", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/Entity;isInRangeToRenderVec3D(Lnet/minecraft/src/Vec3D;)Z"))
    public boolean tensor_onRenderEntitiesWithinRange(Entity entity, Vec3D camera, Operation<Boolean> original) {
        if (this.failsNullCheck()) {
            return entity.isInRangeToRenderVec3D(camera);
        }

        if (this.isXrayEnabled()) {
            return entity.isInRangeToRenderVec3D(camera);
        }

        if (!this.shouldCallEvent(RenderEntityEvent.class)) {
            return entity.isInRangeToRenderVec3D(camera);
        }

        RenderEntityEvent event = EventCache.getCachedEvent(RenderEntityEvent.class).withVariables(entity).call(true);

        if (event.isCancelled()) {
            return true;
        }

        return entity.isInRangeToRenderVec3D(camera);
    }
}
