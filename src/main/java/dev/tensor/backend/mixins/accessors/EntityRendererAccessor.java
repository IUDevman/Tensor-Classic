package dev.tensor.backend.mixins.accessors;

import dev.tensor.backend.MixinPriority;
import net.minecraft.src.EntityRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/**
 * @author Hoosiers
 * @since 04-12-2026
 */

@Mixin(value = EntityRenderer.class, priority = MixinPriority.VALUE)
public interface EntityRendererAccessor {

    @Accessor(value = "field_22228_r")
    float getCameraDistance();

    @Accessor(value = "field_22228_r")
    void setCameraDistance(float distance);
}
