package dev.tensor.backend.mixins;

import dev.tensor.misc.imp.Globals;
import net.minecraft.src.Tessellator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author Hoosiers
 * @since 03-26-2026
 */

@Mixin(value = Tessellator.class)
public abstract class TessellatorMixin implements Globals {

    @Shadow public abstract void setColorRGBA(int i, int j, int k, int l);

    @Inject(method = "setColorOpaque", at = @At("HEAD"), cancellable = true)
    public void onSetColorOpaque(int j, int k, int par3, CallbackInfo ci) {
        if (this.failsNullCheck()) {
            return;
        }

        if (!this.isXrayEnabled()) {
            return;
        }

        if (this.getXray().shouldRenderOpacity()) {
            this.setColorRGBA(j, k, par3, this.getXray().opacityValue.getValue());
            ci.cancel();
        }
    }
}