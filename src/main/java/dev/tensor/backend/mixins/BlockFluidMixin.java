package dev.tensor.backend.mixins;

import dev.tensor.backend.events.FluidBlockEvent;
import dev.tensor.backend.events.FluidCollisionEvent;
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

@Mixin(value = BlockFluid.class, priority = Integer.MAX_VALUE)
public final class BlockFluidMixin implements Globals {

    @Inject(method = "getCollisionBoundingBoxFromPool", at = @At("HEAD"), cancellable = true)
    public void onGetCollisionBoundingBoxFromPool(World i, int j, int k, int par4, CallbackInfoReturnable<AxisAlignedBB> cir) {
        if (this.failsNullCheck()) {
            return;
        }

        FluidCollisionEvent event = new FluidCollisionEvent(j, k, par4);
        this.getEventManager().call(event);

        if (event.isCancelled()) {
            cir.setReturnValue(AxisAlignedBB.getBoundingBox(j, k, par4, j + 1, k + 1, par4 + 1));
        }
    }

    @Inject(method = "canCollideCheck", at = @At("HEAD"), cancellable = true)
    public void onCanCollideCheck(int bl, boolean par2, CallbackInfoReturnable<Boolean> cir) {
        if (this.failsNullCheck()) {
            return;
        }

        FluidBlockEvent event = new FluidBlockEvent();
        this.getEventManager().call(event);

        if (event.isCancelled()) {
            cir.setReturnValue(true);
        }
    }
}
