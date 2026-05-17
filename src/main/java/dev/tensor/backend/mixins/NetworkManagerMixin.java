package dev.tensor.backend.mixins;

import dev.tensor.backend.MixinPriority;
import dev.tensor.backend.events.EventCache;
import dev.tensor.backend.events.constant.KnockbackEvent;
import dev.tensor.backend.events.constant.SocketInitEvent;
import dev.tensor.misc.imp.Globals;
import net.minecraft.src.NetHandler;
import net.minecraft.src.NetworkManager;
import net.minecraft.src.Packet;
import net.minecraft.src.Packet28EntityVelocity;
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
    public void tensor_onReadPacket(CallbackInfoReturnable<Boolean> cir, int var1, Packet var2) {
        if (this.failsNullCheck()) {
            return;
        }

        if (var2 instanceof Packet28EntityVelocity) {
            Packet28EntityVelocity packet = (Packet28EntityVelocity) var2;

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
        }
    }

    @Inject(method = "<init>", at = @At(value = "INVOKE", target = "Ljava/net/Socket;getRemoteSocketAddress()Ljava/net/SocketAddress;", shift = At.Shift.BEFORE))
    public void tensor_onInitSocket(Socket string, String arg, NetHandler par3, CallbackInfo ci) {
        if (string == null) {
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
                    string.setTcpNoDelay(true);
                    this.getTensorLogger().info("TCP set to : {}!", string.getTcpNoDelay());

                } catch (Exception ignored) {
                    this.getTensorLogger().info("TCP Failed to set!");
                }
            }
        });
    }
}
