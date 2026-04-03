package dev.tensor.backend.mixins;

import dev.tensor.backend.events.KnockbackEvent;
import dev.tensor.misc.imp.Globals;
import net.minecraft.src.NetworkManager;
import net.minecraft.src.Packet;
import net.minecraft.src.Packet28EntityVelocity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

/**
 * @author Hoosiers
 * @since 03-02-2026
 */

@Mixin(value = NetworkManager.class, priority = Integer.MAX_VALUE)
public final class NetworkManagerMixin implements Globals {

    @Inject(method = "readPacket", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z"), locals = LocalCapture.CAPTURE_FAILSOFT, cancellable = true)
    public void onReadPacket(CallbackInfoReturnable<Boolean> cir, int var1, Packet var2) {
        if (this.failsNullCheck()) {
            return;
        }

        if (var2 instanceof Packet28EntityVelocity) {
            Packet28EntityVelocity packet = (Packet28EntityVelocity) var2;

            if (packet.entityId != this.getPlayer().entityId) {
                return;
            }

            KnockbackEvent event = new KnockbackEvent();
            this.getEventManager().call(event);

            if (event.isCancelled()) {
                cir.setReturnValue(false);
                cir.cancel();
            }
        }
    }
}
