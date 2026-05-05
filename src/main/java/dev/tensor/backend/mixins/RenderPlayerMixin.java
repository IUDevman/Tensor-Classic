package dev.tensor.backend.mixins;

import dev.tensor.backend.MixinPriority;
import dev.tensor.backend.events.EventCache;
import dev.tensor.backend.events.constant.NameTagEvent;
import dev.tensor.misc.imp.Globals;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.RenderPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author Hoosiers
 * @since 04-06-2026
 */

@Mixin(value = RenderPlayer.class, priority = MixinPriority.VALUE)
public final class RenderPlayerMixin implements Globals {

    @Inject(method = "passSpecialRender(Lnet/minecraft/src/EntityPlayer;DDD)V", at = @At("HEAD"), cancellable = true)
    public void tensor_onPassSpecialRender(EntityPlayer d, double e, double f, double par4, CallbackInfo ci) {
        if (this.failsNullCheck()) {
            return;
        }

        EventCache.getCachedEvent(NameTagEvent.class).call(event -> {

            if (event.isCancelled()) {
                ci.cancel();
            }
        });
    }
}
