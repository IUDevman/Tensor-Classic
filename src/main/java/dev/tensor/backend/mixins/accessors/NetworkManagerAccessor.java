package dev.tensor.backend.mixins.accessors;

import dev.tensor.backend.MixinPriority;
import net.minecraft.src.NetworkManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.net.Socket;

/**
 * @author Hoosiers
 * @since 04-09-2026
 */

@Mixin(value = NetworkManager.class, priority = MixinPriority.VALUE)
public interface NetworkManagerAccessor {

    @Accessor(value = "networkSocket")
    Socket getNetworkSocket();
}
