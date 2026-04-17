package dev.tensor.backend.mixins.accessors;

import dev.tensor.backend.MixinPriority;
import net.minecraft.client.Minecraft;
import net.minecraft.src.Timer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/**
 * @author Hoosiers
 * @since 03-28-2026
 */

@Mixin(value = Minecraft.class, priority = MixinPriority.VALUE)
public interface MinecraftAccessor {

    @Accessor(value = "timer")
    Timer tensor_getTimer();
}
