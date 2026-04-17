package dev.tensor.backend.mixins.accessors;

import dev.tensor.backend.MixinPriority;
import net.minecraft.src.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/**
 * @author Hoosiers
 * @since 03-25-2026
 */

@Mixin(value = Entity.class, priority = MixinPriority.VALUE)
public interface EntityAccessor {

    @Accessor(value = "fallDistance")
    float tensor_getFallDistance();

    @Accessor(value = "fallDistance")
    void tensor_setFallDistance(float fallDistance);
}
