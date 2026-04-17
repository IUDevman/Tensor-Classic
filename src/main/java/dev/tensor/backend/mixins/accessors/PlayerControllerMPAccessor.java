package dev.tensor.backend.mixins.accessors;

import dev.tensor.backend.MixinPriority;
import net.minecraft.src.PlayerControllerMP;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/**
 * @author Hoosiers
 * @since 04-16-2026
 */

@Mixin(value = PlayerControllerMP.class, priority = MixinPriority.VALUE)
public interface PlayerControllerMPAccessor {

    @Accessor(value = "blockHitDelay")
    int getBlockHitDelay();

    @Accessor(value = "blockHitDelay")
    void setBlockHitDelay(int blockHitDelay);
}
