package dev.tensor.backend.mixins;

import dev.tensor.backend.MixinPriority;
import dev.tensor.misc.imp.Globals;
import net.minecraft.src.Block;
import net.minecraft.src.BlockStairs;
import net.minecraft.src.Material;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * @author Hoosiers
 * @since 05-08-2026
 */

@Mixin(value = BlockStairs.class, priority = MixinPriority.VALUE)
public abstract class BlockStairsMixin extends Block implements Globals {

    protected BlockStairsMixin(int blockID, Material material) {
        super(blockID, material);
    }

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

        if (this.getXray().isOnXrayList(this.blockID)) {
            cir.setReturnValue(0);
        } else {
            cir.setReturnValue(1);
        }
    }
}
