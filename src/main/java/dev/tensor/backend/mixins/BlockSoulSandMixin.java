package dev.tensor.backend.mixins;

import dev.tensor.backend.events.VelocityEvent;
import dev.tensor.misc.imp.Globals;
import net.minecraft.src.BlockSoulSand;
import net.minecraft.src.Entity;
import net.minecraft.src.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author Hoosiers
 * @since 04-02-2026
 */

@Mixin(value = BlockSoulSand.class, priority = Integer.MAX_VALUE)
public final class BlockSoulSandMixin implements Globals {

    @Inject(method = "onEntityCollidedWithBlock", at = @At("HEAD"), cancellable = true)
    public void onOnentityCollidedWithBlock(World i, int j, int k, int arg2, Entity par5, CallbackInfo ci) {
        if (this.failsNullCheck()) {
            return;
        }

        VelocityEvent event = new VelocityEvent(VelocityEvent.Type.SoulSand);
        this.getEventManager().call(event);

        if (event.isCancelled()) {
            ci.cancel();
        }
    }
}
