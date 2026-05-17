package dev.tensor.backend.mixins;

import dev.tensor.backend.MixinPriority;
import dev.tensor.backend.events.EventCache;
import dev.tensor.backend.events.constant.ParticleEvent;
import dev.tensor.misc.imp.Globals;
import net.minecraft.src.EffectRenderer;
import net.minecraft.src.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author Hoosiers
 * @since 04-07-2026
 */

@Mixin(value = EffectRenderer.class, priority = MixinPriority.VALUE)
public final class EffectRendererMixin implements Globals {

    @Inject(method = "renderParticles", at = @At("HEAD"), cancellable = true)
    public void tensor_onRenderParticles(Entity f, float par2, CallbackInfo ci) {
        if (this.failsNullCheck()) {
            return;
        }

        if (!this.shouldCallEvent(ParticleEvent.class)) {
            return;
        }

        EventCache.getCachedEvent(ParticleEvent.class).call(true, event -> {

            if (event.isCancelled()) {
                ci.cancel();
            }
        });
    }
}
