package dev.tensor.backend.mixins;

import dev.tensor.backend.MixinPriority;
import dev.tensor.misc.imp.Globals;
import net.minecraft.src.Block;
import net.minecraft.src.IBlockAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * @author Hoosiers
 * @since 03-26-2026
 */

@Mixin(value = Block.class, priority = MixinPriority.VALUE)
public final class BlockMixin implements Globals {

    @Inject(method = "getBlockBrightness", at = @At("HEAD"), cancellable = true)
    public void tensor_onGetBlockBrightness(IBlockAccess i, int j, int k, int par4, CallbackInfoReturnable<Float> cir) {
        if (this.failsNullCheck()) {
            return;
        }

        if (this.isXrayEnabled()) {
            return;
        }

        cir.setReturnValue(1.0F);
    }
}