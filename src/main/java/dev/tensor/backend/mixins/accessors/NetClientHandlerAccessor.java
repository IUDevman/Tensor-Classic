package dev.tensor.backend.mixins.accessors;

import dev.tensor.backend.MixinPriority;
import net.minecraft.src.NetClientHandler;
import net.minecraft.src.NetworkManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/**
 * @author Hoosiers
 * @since 04-09-2026
 */

@Mixin(value = NetClientHandler.class, priority = MixinPriority.VALUE)
public interface NetClientHandlerAccessor {

    @Accessor(value = "netManager")
    NetworkManager getNetManager();
}
