package dev.tensor.backend.mixins;

import dev.tensor.backend.MixinPriority;
import dev.tensor.backend.events.EventCache;
import dev.tensor.backend.events.variable.LoadChunkEvent;
import dev.tensor.misc.imp.Globals;
import net.minecraft.src.Chunk;
import net.minecraft.src.ChunkCoordIntPair;
import net.minecraft.src.ChunkProvider;
import net.minecraft.src.IChunkProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Hoosiers
 * @since 09-19-2026
 */

@SuppressWarnings({"unchecked", "rawtypes"})
@Mixin(value = ChunkProvider.class, priority = MixinPriority.VALUE)
public abstract class ChunkProviderMixin implements Globals {

    @Shadow
    private Set droppedChunksSet;

    @Shadow
    private Map chunkMap;

    @Shadow
    protected abstract Chunk worldObj(int i, int j);

    @Shadow
    private IChunkProvider chunkProvider;

    @Shadow
    private Chunk field_28064_b;

    @Shadow
    private List chunkList;

    @Shadow
    public abstract boolean chunkExists(int i, int j);

    @Shadow
    public abstract void populate(IChunkProvider arg, int i, int j);

    @Shadow
    public abstract Chunk provideChunk(int i, int j);

    @Inject(method = "prepareChunk", at = @At("HEAD"), cancellable = true)
    public void tensor_prepareChunk(int i, int j, CallbackInfoReturnable<Chunk> cir) {
        if (this.failsNullCheck()) {
            return;
        }

        //This works in singleplayer only, but I need this for easier testing
        if (this.isMultiplayer()) {
            return;
        }

        if (!this.getEventManager().shouldCall(LoadChunkEvent.class)) {
            return;
        }

        int chunkID = ChunkCoordIntPair.chunkXZ2Int(i, j);

        this.droppedChunksSet.remove(chunkID);

        Chunk newChunk = (Chunk) this.chunkMap.get(chunkID);

        if (newChunk == null) {
            newChunk = this.worldObj(i, j);

            if (newChunk == null) {
                if (this.chunkProvider == null) {
                    newChunk = this.field_28064_b;
                } else {
                    newChunk = this.chunkProvider.provideChunk(i, j);
                }
            }

            this.chunkMap.put(chunkID, newChunk);
            this.chunkList.add(newChunk);

            if (newChunk != null) {
                newChunk.func_4143_d();
                newChunk.onChunkLoad();
            }

            if (newChunk != null && !newChunk.isTerrainPopulated && this.chunkExists(i + 1, j + 1) && this.chunkExists(i, j + 1) && this.chunkExists(i + 1, j)) {
                this.populate((IChunkProvider) this, i, j);
            }

            if (this.chunkExists(i - 1, j) && !this.provideChunk(i - 1, j).isTerrainPopulated && this.chunkExists(i - 1, j + 1) && this.chunkExists(i, j + 1) && this.chunkExists(i - 1, j)) {
                this.populate((IChunkProvider) this, i - 1, j);
            }

            if (this.chunkExists(i, j - 1) && !this.provideChunk(i, j - 1).isTerrainPopulated && this.chunkExists(i + 1, j - 1) && this.chunkExists(i, j - 1) && this.chunkExists(i + 1, j)) {
                this.populate((IChunkProvider) this, i, j - 1);
            }

            if (this.chunkExists(i - 1, j - 1) && !this.provideChunk(i - 1, j - 1).isTerrainPopulated && this.chunkExists(i - 1, j - 1) && this.chunkExists(i, j - 1) && this.chunkExists(i - 1, j)) {
                this.populate((IChunkProvider) this, i - 1, j - 1);
            }
        }

        EventCache.getCachedEvent(LoadChunkEvent.class).withVariables(newChunk).call(true);

        cir.setReturnValue(newChunk);
        cir.cancel();
    }
}
