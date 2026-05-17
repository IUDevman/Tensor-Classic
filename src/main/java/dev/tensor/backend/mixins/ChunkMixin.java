package dev.tensor.backend.mixins;

import dev.tensor.backend.MixinPriority;
import dev.tensor.backend.events.EventCache;
import dev.tensor.backend.events.variable.LightValueEvent;
import dev.tensor.misc.imp.Globals;
import net.minecraft.src.Chunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * @author Hoosiers
 * @since 03-20-2026
 */

@Mixin(value = Chunk.class, priority = MixinPriority.VALUE)
public final class ChunkMixin implements Globals {

    @Inject(method = "getBlockLightValue", at = @At("HEAD"), cancellable = true)
    public void tensor_onGetBlockLightValue(int j, int k, int l, int par4, CallbackInfoReturnable<Integer> cir) {
        if (this.failsNullCheck()) {
            return;
        }

        if (!this.shouldCallEvent(LightValueEvent.class)) {
            return;
        }

        EventCache.getCachedEvent(LightValueEvent.class).withVariables(false).call(true, event -> {

            if (event.isCancelled()) {
                cir.setReturnValue(event.darkMode ? 0 : 15);
            }
        });
    }
}
