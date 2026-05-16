package dev.tensor.backend.mixins;

import dev.tensor.backend.MixinPriority;
import dev.tensor.backend.events.EventCache;
import dev.tensor.backend.events.variable.UpdateClientPlayerActionEvent;
import dev.tensor.misc.imp.Globals;
import net.minecraft.src.EntityClientPlayerMP;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author Hoosiers
 * @since 04-11-2026
 */

@Mixin(value = EntityClientPlayerMP.class, priority = MixinPriority.VALUE)
public abstract class EntityClientPlayerMPMixin implements Globals {

    @Shadow
    protected abstract void sendInventoryChanged();

    @Shadow
    private int field_9380_bx;

    @Inject(method = "func_4056_N", at = @At("HEAD"), cancellable = true)
    public void tensor_onUpdateClientPlayerActions(CallbackInfo ci) {
        if (this.failsNullCheck()) {
            return;
        }

        EventCache.getCachedEvent(UpdateClientPlayerActionEvent.class).withVariables(false).call(event -> {

            if (event.isCancelled()) {
                if (event.shouldUpdateInventory && this.field_9380_bx++ == 20) {
                    this.sendInventoryChanged();
                    this.field_9380_bx = 0;
                }
                ci.cancel();
            }
        });
    }
}
