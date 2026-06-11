package dev.tensor.backend.mixins;

import dev.tensor.backend.MixinPriority;
import dev.tensor.backend.events.EventCache;
import dev.tensor.backend.events.variable.SignCollisionEvent;
import dev.tensor.misc.imp.Globals;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.BlockSign;
import net.minecraft.src.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * @author Hoosiers
 * @since 06-09-2026
 */

@Mixin(value = BlockSign.class, priority = MixinPriority.VALUE)
public final class BlockSignMixin implements Globals {

    @Inject(method = "getCollisionBoundingBoxFromPool", at = @At("HEAD"), cancellable = true)
    public void tensor_onGetCollisionBoundingBoxFromPool(World world, int x, int y, int z, CallbackInfoReturnable<AxisAlignedBB> cir) {
        if (this.failsNullCheck()) {
            return;
        }

        if (!this.shouldCallEvent(SignCollisionEvent.class)) {
            return;
        }

        EventCache.getCachedEvent(SignCollisionEvent.class).withVariables(y).call(true, event -> {

            if (event.isCancelled()) {
                cir.setReturnValue(AxisAlignedBB.getBoundingBox(x, y, z, x + 1, y + 1, z + 1));
            }
        });
    }
}
