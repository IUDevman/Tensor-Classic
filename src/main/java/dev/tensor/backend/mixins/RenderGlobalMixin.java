package dev.tensor.backend.mixins;

import dev.tensor.misc.imp.Globals;
import net.minecraft.src.RenderGlobal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author Hoosiers
 * @since 03-27-2026
 */

@Mixin(value = RenderGlobal.class, priority = Integer.MAX_VALUE)
public final class RenderGlobalMixin implements Globals {

    @Inject(method = "renderClouds", at = @At("HEAD"), cancellable = true)
    public void onRenderClouds(float par1, CallbackInfo ci) {
        if (this.failsNullCheck()) {
            return;
        }

        if (!this.isXrayEnabled()) {
            return;
        }

        if (!this.getXray().opacity.getValue()) {
            return;
        }

        ci.cancel();
    }
}
