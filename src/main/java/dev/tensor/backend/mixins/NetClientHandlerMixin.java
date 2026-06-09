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

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * @author Hoosiers
 * @since 03-19-2026
 */

@Mixin(value = NetClientHandler.class, priority = MixinPriority.VALUE)
public abstract class NetClientHandlerMixin implements Globals {

    @Shadow
    private NetworkManager netManager;

    @Shadow
    private boolean disconnected;

    @Inject(method = "addToSendQueue", at = @At("HEAD"), cancellable = true)
    public void tensor_onAddToSendQueue(Packet packetToSend, CallbackInfo ci) {
        if (packetToSend == null || this.failsNullCheck() || !this.isMultiplayer()) {
            return;
        }

        if (this.shouldCallEvent(GeneralPacketEvent.class)) {
            GeneralPacketEvent event = EventCache.getCachedEvent(GeneralPacketEvent.class).withVariables(GeneralPacketEvent.Type.OUTBOUND, packetToSend).call(true);

            if (event.isCancelled()) {
                ci.cancel();
                return;
            }
        }

        if (packetToSend instanceof Packet3Chat) {
            Packet3Chat packet = (Packet3Chat) packetToSend;

            if (packet.message == null) {
                return;
            }

            EventCache.getCachedEvent(ChatSentEvent.class).withVariables(packet.message).call(false, event -> {

                if (event.isCancelled()) {
                    ci.cancel();

                } else if (!event.ARGS.equals(packet.message)) {
                    packet.message = event.ARGS;

                    this.sendPacketBypass(packet);
                    ci.cancel();
                }
            });

            return;
        }

        if (packetToSend instanceof Packet14BlockDig) {
            Packet14BlockDig packet = (Packet14BlockDig) packetToSend;

            if (!this.shouldCallEvent(BlockBreakEvent.class)) {
                return;
            }

            EventCache.getCachedEvent(BlockBreakEvent.class).withVariables(packet).call(true, event -> {

                if (event.isCancelled()) {
                    ci.cancel();
                }
            });

            return;
        }

        if (packetToSend instanceof Packet10Flying) {
            Packet10Flying packet = (Packet10Flying) packetToSend;

            EventCache.getCachedEvent(Packet10FlyingEvent.class).withVariables(packet).call(false, event1 -> {

                if (event1.isCancelled()) {
                    ci.cancel();
                    return;
                }

                this.sendPacketBypass(event1.packet);
                ci.cancel();
            });

            return;
        }

        if (packetToSend instanceof Packet101CloseWindow) {
            Packet101CloseWindow packet = (Packet101CloseWindow) packetToSend;

            if (packet.windowId == this.getPlayer().craftingInventory.windowId && this.shouldCallEvent(CloseInventoryEvent.class)) {

                EventCache.getCachedEvent(CloseInventoryEvent.class).call(true, event -> {

                    if (event.isCancelled()) {
                        ci.cancel();
                    }
                });
            }

            return;
        }

        if (packetToSend instanceof Packet7UseEntity) {
            Packet7UseEntity packet = (Packet7UseEntity) packetToSend;

            if (packet.playerEntityId != this.getPlayer().entityId) {
                return;
            }

            if (!this.shouldCallEvent(Packet7UseEntityEvent.class)) {
                return;
            }

            EventCache.getCachedEvent(Packet7UseEntityEvent.class).withVariables(packet).call(true, event -> {

                if (event.isCancelled()) {
                    ci.cancel();
                }
            });
        }
    }

    @Inject(method = "handleChat", at = @At("HEAD"), cancellable = true)
    public void tensor_onHandleChat(Packet3Chat packet, CallbackInfo ci) {
        if (this.failsNullCheck() || packet == null) {
            return;
        }

        if (!this.shouldCallEvent(ChatReceivedEvent.class)) {
            return;
        }

        EventCache.getCachedEvent(ChatReceivedEvent.class).withVariables(packet.message).call(true, event -> {

            if (event.isCancelled()) {
                this.sendMessageWithLinesColor(event.message);
                ci.cancel();
            }
        });
    }

    @Inject(method = "handleKickDisconnect", at = @At("HEAD"), cancellable = true)
    public void tensor_onHandleKickDisconnect(Packet255KickDisconnect packet, CallbackInfo ci) {
        if (this.failsNullCheck() || packet == null) {
            return;
        }

        if (this.isAutoReconnectEnabled()) {
            this.netManager.networkShutdown("disconnect.kicked");
            this.disconnected = true;
            this.getMinecraft().changeWorld1(null);
            this.getMinecraft().displayGuiScreen(new TensorGuiConnectFailed("disconnect.disconnected", "disconnect.genericReason", packet.reason));

            ci.cancel();
        }
    }

    @Inject(method = "handleErrorMessage", at = @At("HEAD"), cancellable = true)
    public void tensor_onHandleErrorMessage(String error, Object[] details, CallbackInfo ci) {
        if (this.failsNullCheck() || this.disconnected) {
            return;
        }

        if (this.isAutoReconnectEnabled()) {
            this.disconnected = true;
            this.getMinecraft().changeWorld1(null);
            this.getMinecraft().displayGuiScreen(new TensorGuiConnectFailed("disconnect.lost", error, details));

            ci.cancel();
        }
    }

    //Adapted from @author tweska 2023; MIT LICENSE; https://github.com/tweska/BetaFix/blob/master/LICENSE
    @Inject(method = "handleHandshake", at = @At("HEAD"), cancellable = true)
    public void betaFix_handleHandshake(Packet2Handshake packet2Handshake, CallbackInfo ci) {
        if (packet2Handshake.username.equals("-")) {
            this.sendPacket(new Packet1Login(this.getMinecraft().session.username, 14));

            ci.cancel();
            return;
        }

        try {
            URL url = new URL("http://session.minecraft.net/game/joinserver.jsp?user=" + this.getMinecraft().session.username + "&sessionId=" + this.getMinecraft().session.sessionId + "&serverId=" + packet2Handshake.username);

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(url.openStream()));

            String result = bufferedReader.readLine();
            bufferedReader.close();

            if (result.equalsIgnoreCase("ok")) {
                this.sendPacket(new Packet1Login(this.getMinecraft().session.username, 14));

            } else {
                this.netManager.networkShutdown("disconnect.loginFailedInfo", result);
            }

        } catch (Exception e) {
            e.printStackTrace();
            this.netManager.networkShutdown("disconnect.genericReason", "Internal client error: " + e);
        }

        ci.cancel();
    }
}
