package dev.tensor.backend.mixins.accessors;

import dev.tensor.backend.MixinPriority;
import net.minecraft.src.GuiIngame;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

/**
 * @author Hoosiers
 * @since 04-11-2026
 */

@Mixin(value = GuiIngame.class, priority = MixinPriority.VALUE)
public interface GuiIngameAccessor {

    @Accessor(value = "chatMessageList")
    List tensor_getChatMessageList();
}
