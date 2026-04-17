package dev.tensor.backend.mixins.accessors;

import dev.tensor.backend.MixinPriority;
import net.minecraft.src.EnumToolMaterial;
import net.minecraft.src.ItemTool;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/**
 * @author Hoosiers
 * @since 04-13-2026
 */

@Mixin(value = ItemTool.class, priority = MixinPriority.VALUE)
public interface ItemToolAccessor {

    @Accessor(value = "toolMaterial")
    EnumToolMaterial tensor_getToolMaterial();
}
