package dev.tensor.backend.mixins.accessors;

import net.minecraft.src.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/**
 * @author Hoosiers
 * @since 03-25-2026
 */

@Mixin(Entity.class)
public interface EntityAccessor {

    @Accessor("fallDistance")
    float fallDistance();

    @Accessor("fallDistance")
    void fallDistance(float fallDistance);
}
