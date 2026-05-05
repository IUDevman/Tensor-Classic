package dev.tensor.backend.mixins;

import dev.tensor.backend.MixinPriority;
import dev.tensor.backend.events.EventCache;
import dev.tensor.backend.events.constant.RenderItemEvent;
import dev.tensor.misc.imp.Globals;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityItem;
import net.minecraft.src.RenderManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author Hoosiers
 * @since 03-29-2026
 */

@Mixin(value = RenderManager.class, priority = MixinPriority.VALUE)
public final class RenderManagerMixin implements Globals {

    @Inject(method = "renderEntity", at = @At("HEAD"), cancellable = true)
    public void tensor_onRenderEntity(Entity f, float par2, CallbackInfo ci) {
        if (this.failsNullCheck()) {
            return;
        }

        if (f instanceof EntityItem) {
            EventCache.getCachedEvent(RenderItemEvent.class).call(event -> {

                if (event.isCancelled()) {
                    ci.cancel();
                }
            });
        }
    }
}
