package dev.tensor.backend.mixins;

import dev.tensor.backend.events.ChatSentEvent;
import dev.tensor.misc.imp.Globals;
import net.minecraft.src.NetClientHandler;
import net.minecraft.src.Packet;
import net.minecraft.src.Packet3Chat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author Hoosiers
 * @since 03-19-2026
 */

@Mixin(value = NetClientHandler.class, priority = Integer.MAX_VALUE)
public final class NetClientHandlerMixin implements Globals {

    @Inject(method = "addToSendQueue", at = @At("HEAD"), cancellable = true)
    public void onAddToSendQueue(Packet arg, CallbackInfo ci) {
        if (arg == null || this.failsNullCheck() || !this.isMultiplayer()) {
            return;
        }

        if (arg instanceof Packet3Chat) {
            Packet3Chat packet = (Packet3Chat) arg;

            if (packet.message == null) {
                return;
            }

            ChatSentEvent event = new ChatSentEvent(packet.message);
            this.getEventManager().call(event);

            if (event.isCancelled())  {
                ci.cancel();
            }
        }
    }
}
