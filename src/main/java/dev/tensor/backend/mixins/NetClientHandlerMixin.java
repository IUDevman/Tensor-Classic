package dev.tensor.backend.mixins;

import dev.tensor.backend.events.BlockBreakEvent;
import dev.tensor.backend.events.ChatSentEvent;
import dev.tensor.backend.events.CloseInventoryEvent;
import dev.tensor.backend.events.Packet10FlyingEvent;
import dev.tensor.misc.imp.Globals;
import net.minecraft.src.*;
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

            if (event.isCancelled()) {
                ci.cancel();
            } else if (!event.ARGS.equals(packet.message)) {
                packet.message = event.ARGS;
            }
        }

        if (arg instanceof Packet14BlockDig) {

            Packet14BlockDig packet = (Packet14BlockDig) arg;

            BlockBreakEvent event = new BlockBreakEvent(packet);
            this.getEventManager().call(event);
        }

        if (arg instanceof Packet10Flying) {

            Packet10Flying packet = (Packet10Flying) arg;

            Packet10FlyingEvent event = new Packet10FlyingEvent(packet);
            this.getEventManager().call(event);

            arg = event.packet;
        }

        if (arg instanceof Packet101CloseWindow) {

            Packet101CloseWindow packet = (Packet101CloseWindow) arg;

            if (packet.windowId == this.getPlayer().craftingInventory.windowId) {

                CloseInventoryEvent event = new CloseInventoryEvent();
                this.getEventManager().call(event);

                if (event.isCancelled()) {
                    ci.cancel();
                }
            }
        }
    }
}
