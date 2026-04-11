package dev.tensor.backend.mixins;

import dev.tensor.backend.MixinPriority;
import dev.tensor.backend.events.*;
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

@Mixin(value = NetClientHandler.class, priority = MixinPriority.VALUE)
public final class NetClientHandlerMixin implements Globals {

    @Inject(method = "addToSendQueue", at = @At("HEAD"), cancellable = true)
    public void tensor_onAddToSendQueue(Packet arg, CallbackInfo ci) {
        if (arg == null || this.failsNullCheck() || !this.isMultiplayer()) {
            return;
        }

        if (arg instanceof Packet3Chat) {
            Packet3Chat packet = (Packet3Chat) arg;

            if (packet.message == null) {
                return;
            }

            ChatSentEvent event = new ChatSentEvent(packet.message).call();

            if (event.isCancelled()) {
                ci.cancel();
            } else if (!event.ARGS.equals(packet.message)) {
                packet.message = event.ARGS;
            }

            return;
        }

        if (arg instanceof Packet14BlockDig) {
            Packet14BlockDig packet = (Packet14BlockDig) arg;

            @SuppressWarnings("unused")
            BlockBreakEvent event = new BlockBreakEvent(packet).call();
            return;
        }

        if (arg instanceof Packet10Flying) {
            Packet10Flying packet = (Packet10Flying) arg;

            Packet10FlyingEvent event = new Packet10FlyingEvent(packet).call();

            arg = event.packet;
            return;
        }

        if (arg instanceof Packet101CloseWindow) {
            Packet101CloseWindow packet = (Packet101CloseWindow) arg;

            if (packet.windowId == this.getPlayer().craftingInventory.windowId) {

                CloseInventoryEvent event = new CloseInventoryEvent().call();

                if (event.isCancelled()) {
                    ci.cancel();
                }
            }
        }
    }

    @Inject(method = "handleChat", at = @At("HEAD"), cancellable = true)
    public void onHandleChat(Packet3Chat par1, CallbackInfo ci) {
        if (this.failsNullCheck() || par1 == null) {
            return;
        }

        ChatReceivedEvent event = new ChatReceivedEvent(par1.message).call();

        if (event.isCancelled()) {
            this.sendMessageWithLinesColor(event.message);
            ci.cancel();
        }
    }
}
