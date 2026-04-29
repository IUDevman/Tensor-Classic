package dev.tensor.backend.mixins;

import dev.tensor.backend.MixinPriority;
import dev.tensor.backend.events.EventCache;
import dev.tensor.backend.events.constant.CloseInventoryEvent;
import dev.tensor.backend.events.variable.*;
import dev.tensor.misc.gui.TensorGuiConnectFailed;
import dev.tensor.misc.imp.Globals;
import net.minecraft.src.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author Hoosiers
 * @since 03-19-2026
 */

@Mixin(value = NetClientHandler.class, priority = MixinPriority.VALUE)
public final class NetClientHandlerMixin implements Globals {

    @Shadow
    private NetworkManager netManager;

    @Shadow
    private boolean disconnected;

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

            ChatSentEvent event = EventCache.getCachedEvent(ChatSentEvent.class).withVariables(packet.message).call();

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
            BlockBreakEvent event = EventCache.getCachedEvent(BlockBreakEvent.class).withVariables(packet).call();

            if (event.isCancelled()) {
                ci.cancel();
            }

            return;
        }

        if (arg instanceof Packet10Flying) {
            Packet10Flying packet = (Packet10Flying) arg;

            Packet10FlyingEvent event = EventCache.getCachedEvent(Packet10FlyingEvent.class).withVariables(packet).call();

            if (event.isCancelled()) {
                ci.cancel();
                return;
            }

            arg = event.packet;
            return;
        }

        if (arg instanceof Packet101CloseWindow) {
            Packet101CloseWindow packet = (Packet101CloseWindow) arg;

            if (packet.windowId == this.getPlayer().craftingInventory.windowId) {

                CloseInventoryEvent event = EventCache.getCachedEvent(CloseInventoryEvent.class).call();

                if (event.isCancelled()) {
                    ci.cancel();
                }
            }

            return;
        }

        if (arg instanceof Packet7UseEntity) {
            Packet7UseEntity packet = (Packet7UseEntity) arg;

            if (packet.playerEntityId != this.getPlayer().entityId) {
                return;
            }

            if (packet.isLeftClick != 1) {
                return;
            }

            Packet7UseEntityEvent event = EventCache.getCachedEvent(Packet7UseEntityEvent.class).withVariables(packet).call();

            if (event.isCancelled()) {
                ci.cancel();
            }
        }
    }

    @Inject(method = "handleChat", at = @At("HEAD"), cancellable = true)
    public void tensor_onHandleChat(Packet3Chat par1, CallbackInfo ci) {
        if (this.failsNullCheck() || par1 == null) {
            return;
        }

        ChatReceivedEvent event = EventCache.getCachedEvent(ChatReceivedEvent.class).withVariables(par1.message).call();

        if (event.isCancelled()) {
            this.sendMessageWithLinesColor(event.message);
            ci.cancel();
        }
    }

    @Inject(method = "handleKickDisconnect", at = @At("HEAD"), cancellable = true)
    public void tensor_onHandleKickDisconnect(Packet255KickDisconnect par1, CallbackInfo ci) {
        if (this.failsNullCheck() || par1 == null) {
            return;
        }

        if (this.isAutoReconnectEnabled()) {
            this.netManager.networkShutdown("disconnect.kicked");
            this.disconnected = true;
            this.getMinecraft().changeWorld1(null);
            this.getMinecraft().displayGuiScreen(new TensorGuiConnectFailed("disconnect.disconnected", "disconnect.genericReason", par1.reason));

            ci.cancel();
        }
    }

    @Inject(method = "handleErrorMessage", at = @At("HEAD"), cancellable = true)
    public void tensor_onHandleErrorMessage(String objects, Object[] par2, CallbackInfo ci) {
        if (this.failsNullCheck() || this.disconnected) {
            return;
        }

        if (this.isAutoReconnectEnabled()) {
            this.disconnected = true;
            this.getMinecraft().changeWorld1(null);
            this.getMinecraft().displayGuiScreen(new TensorGuiConnectFailed("disconnect.lost", objects, par2));

            ci.cancel();
        }
    }
}
