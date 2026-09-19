package dev.tensor.backend.mixins;

import dev.tensor.backend.MixinPriority;
import dev.tensor.backend.events.EventCache;
import dev.tensor.backend.events.variable.LoadChunkEvent;
import dev.tensor.misc.imp.Globals;
import net.minecraft.src.Chunk;
import net.minecraft.src.ChunkCoordIntPair;
import net.minecraft.src.ChunkProviderClient;
import net.minecraft.src.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Arrays;
import java.util.Map;

/**
 * @author Hoosiers
 * @since 09-19-2026
 */

@SuppressWarnings({"unchecked", "rawtypes"})
@Mixin(value = ChunkProviderClient.class, priority = MixinPriority.VALUE)
public final class ChunkProviderClientMixin implements Globals {

    @Shadow
    private World worldObj;

    @Shadow
    private Map chunkMapping;

    @Inject(method = "prepareChunk", at = @At("HEAD"), cancellable = true)
    public void tensor_PrepareChunk(int x, int z, CallbackInfoReturnable<Chunk> cir) {
        if (this.failsNullCheck()) {
            return;
        }

        if (!this.getEventManager().shouldCall(LoadChunkEvent.class)) {
            return;
        }

        Chunk newChunk = new Chunk(this.worldObj, new byte['耀'], x, z);

        Arrays.fill(newChunk.skylightMap.data, (byte) -1);

        this.chunkMapping.put(new ChunkCoordIntPair(x, z), newChunk);
        newChunk.isChunkLoaded = true;

        EventCache.getCachedEvent(LoadChunkEvent.class).withVariables(newChunk).call(true);

        cir.setReturnValue(newChunk);
        cir.cancel();
    }
}
