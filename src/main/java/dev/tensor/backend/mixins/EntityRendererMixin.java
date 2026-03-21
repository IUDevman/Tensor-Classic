package dev.tensor.backend.mixins;

import dev.tensor.backend.events.FOVModifierEvent;
import dev.tensor.backend.events.HurtCameraEvent;
import dev.tensor.misc.imp.Globals;
import net.minecraft.src.EntityRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * @author Hoosiers
 * @since 03-18-2026
 */

@Mixin(value = EntityRenderer.class, priority = Integer.MAX_VALUE)
public final class EntityRendererMixin implements Globals {

    @Inject(method = "getFOVModifier", at = @At("HEAD"), cancellable = true)
    public void onGetFOVModifier(float par1, CallbackInfoReturnable<Float> cir) {
        if (this.failsNullCheck()) {
            return;
        }

        FOVModifierEvent event = new FOVModifierEvent();
        this.getEventManager().call(event);

        if (event.isCancelled()) {
            cir.setReturnValue(event.FOV);
        }
    }

    @Inject(method = "hurtCameraEffect", at = @At("HEAD"), cancellable = true)
    public void onHurtCameraEffect(float par1, CallbackInfo ci) {
        if (this.failsNullCheck()) {
            return;
        }

        HurtCameraEvent event = new HurtCameraEvent();
        this.getEventManager().call(event);

        if (event.isCancelled()) {
            ci.cancel();
        }
    }
}
