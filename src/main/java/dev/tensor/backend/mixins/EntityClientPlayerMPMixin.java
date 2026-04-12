package dev.tensor.backend.mixins;

import dev.tensor.backend.MixinPriority;
import dev.tensor.backend.events.UpdateClientPlayerActionEvent;
import dev.tensor.misc.imp.Globals;
import net.minecraft.src.EntityClientPlayerMP;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author Hoosiers
 * @since 04-11-2026
 */

@Mixin(value = EntityClientPlayerMP.class, priority = MixinPriority.VALUE)
public final class EntityClientPlayerMPMixin implements Globals {

    @Inject(method = "func_4056_N", at = @At("HEAD"), cancellable = true)
    public void tensor_onUpdateClientPlayerActions(CallbackInfo ci) {
        if (this.failsNullCheck()) {
            return;
        }

        UpdateClientPlayerActionEvent event = new UpdateClientPlayerActionEvent().call();

        if (event.isCancelled()) {
            ci.cancel();
        }
    }
}
