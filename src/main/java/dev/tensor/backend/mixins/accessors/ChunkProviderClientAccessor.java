package dev.tensor.backend.mixins.accessors;

import dev.tensor.backend.MixinPriority;
import net.minecraft.src.ChunkProviderClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

/**
 * @author Hoosiers
 * @since 09-19-2026
 */

@Mixin(value = ChunkProviderClient.class, priority = MixinPriority.VALUE)
public interface ChunkProviderClientAccessor {

    @Accessor(value = "chunkMapping")
    Map tensor_getChunkMapping();
}
