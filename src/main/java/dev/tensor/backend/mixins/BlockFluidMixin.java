package dev.tensor.backend.mixins;

import dev.tensor.backend.MixinPriority;
import dev.tensor.backend.events.EventCache;
import dev.tensor.backend.events.constant.FluidBlockEvent;
import dev.tensor.backend.events.variable.FluidCollisionEvent;
import dev.tensor.misc.imp.Globals;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.BlockFluid;
import net.minecraft.src.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * @author Hoosiers
 * @since 04-05-2026
 */

@Mixin(value = BlockFluid.class, priority = MixinPriority.VALUE)
public final class BlockFluidMixin implements Globals {

    @Inject(method = "getCollisionBoundingBoxFromPool", at = @At("HEAD"), cancellable = true)
    public void tensor_onGetCollisionBoundingBoxFromPool(World i, int j, int k, int par4, CallbackInfoReturnable<AxisAlignedBB> cir) {
        if (this.failsNullCheck()) {
            return;
        }

        if (!this.shouldCallEvent(FluidCollisionEvent.class)) {
            return;
        }

        EventCache.getCachedEvent(FluidCollisionEvent.class).withVariables(j, k, par4).call(true, event -> {

            if (event.isCancelled()) {
                cir.setReturnValue(AxisAlignedBB.getBoundingBox(j, k, par4, j + 1, k + 1, par4 + 1));
            }
        });
    }

    @Inject(method = "canCollideCheck", at = @At("HEAD"), cancellable = true)
    public void tensor_onCanCollideCheck(int bl, boolean par2, CallbackInfoReturnable<Boolean> cir) {
        if (this.failsNullCheck()) {
            return;
        }

        if (!this.shouldCallEvent(FluidBlockEvent.class)) {
            return;
        }

        EventCache.getCachedEvent(FluidBlockEvent.class).call(true, event -> {

            if (event.isCancelled()) {
                cir.setReturnValue(true);
            }
        });
    }
}
