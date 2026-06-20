package dev.tensor.backend.mixins;

import dev.tensor.backend.MixinPriority;
import dev.tensor.backend.events.EventCache;
import dev.tensor.backend.events.constant.KnockbackEvent;
import dev.tensor.backend.events.constant.SocketInitEvent;
import dev.tensor.backend.events.variable.ChunkChangeEvent;
import dev.tensor.backend.events.variable.GeneralPacketEvent;
import dev.tensor.misc.imp.Globals;
import net.minecraft.src.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.net.Socket;

/**
 * @author Hoosiers
 * @since 03-02-2026
 */

@Mixin(value = NetworkManager.class, priority = MixinPriority.VALUE)
public final class NetworkManagerMixin implements Globals {

    @Inject(method = "readPacket", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z"), locals = LocalCapture.CAPTURE_FAILSOFT, cancellable = true)
    public void tensor_onReadPacket(CallbackInfoReturnable<Boolean> cir, int index, Packet packetToRead) {
        if (this.failsNullCheck() || packetToRead == null) {
            return;
        }

        if (this.shouldCallEvent(GeneralPacketEvent.class)) {
            GeneralPacketEvent event = EventCache.getCachedEvent(GeneralPacketEvent.class).withVariables(GeneralPacketEvent.Type.INBOUND, packetToRead).call(true);

            if (event.isCancelled()) {
                cir.setReturnValue(false);
                cir.cancel();
                return;
            }
        }

        if (packetToRead instanceof Packet28EntityVelocity) {
            Packet28EntityVelocity packet = (Packet28EntityVelocity) packetToRead;

            if (packet.entityId != this.getPlayer().entityId) {
                return;
            }

            if (!this.shouldCallEvent(KnockbackEvent.class)) {
                return;
            }

            EventCache.getCachedEvent(KnockbackEvent.class).call(true, event -> {

                if (event.isCancelled()) {
                    cir.setReturnValue(false);
                    cir.cancel();
                }
            });

            return;
        }

        if (packetToRead instanceof Packet52MultiBlockChange) {
            Packet52MultiBlockChange packet = (Packet52MultiBlockChange) packetToRead;

            if (!this.shouldCallEvent(ChunkChangeEvent.class)) {
                return;
            }

            EventCache.getCachedEvent(ChunkChangeEvent.class).withVariables(ChunkChangeEvent.Type.MULTI, packet).call(false);
            return;
        }

        if (packetToRead instanceof Packet53BlockChange) {
            Packet53BlockChange packet = (Packet53BlockChange) packetToRead;

            if (!this.shouldCallEvent(ChunkChangeEvent.class)) {
                return;
            }

            EventCache.getCachedEvent(ChunkChangeEvent.class).withVariables(ChunkChangeEvent.Type.SINGLE, packet).call(false);
        }
    }

    @Inject(method = "<init>", at = @At(value = "INVOKE", target = "Ljava/net/Socket;getRemoteSocketAddress()Ljava/net/SocketAddress;", shift = At.Shift.BEFORE))
    public void tensor_onInitSocket(Socket socket, String threadName, NetHandler netHandler, CallbackInfo ci) {
        if (socket == null) {
            return;
        }

        if (this.getTensor() == null) {
            return;
        }

        if (!this.shouldCallEvent(SocketInitEvent.class)) {
            return;
        }

        EventCache.getCachedEvent(SocketInitEvent.class).call(true, event -> {

            if (event.isCancelled()) {
                try {
                    socket.setTcpNoDelay(true);
                    this.getTensorLogger().info("TCP set to : {}!", socket.getTcpNoDelay());

                } catch (Exception ignored) {
                    this.getTensorLogger().info("TCP Failed to set!");
                }
            }
        });
    }
}
