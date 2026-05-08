package dev.tensor.backend.mixins;

import dev.tensor.backend.MixinPriority;
import dev.tensor.misc.imp.Globals;
import net.minecraft.src.Block;
import net.minecraft.src.BlockGlass;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * @author Hoosiers
 * @since 05-08-2026
 */

@Mixin(value = BlockGlass.class, priority = MixinPriority.VALUE)
public final class BlockGlassMixin implements Globals {

    @Inject(method = "getRenderBlockPass", at = @At("HEAD"), cancellable = true)
    public void tensor_getRenderBlockPass(CallbackInfoReturnable<Integer> cir) {
        if (this.failsNullCheck()) {
            return;
        }

        if (!this.isXrayEnabled()) {
            return;
        }

        if (!this.getXray().opacity.getValue()) {
            return;
        }

        if (!this.getXray().isOnXrayList(Block.glass.blockID)) {
            cir.setReturnValue(1);
        }
    }
}
