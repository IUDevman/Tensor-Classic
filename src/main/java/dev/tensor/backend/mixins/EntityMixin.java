package dev.tensor.backend.mixins;

import dev.tensor.backend.MixinPriority;
import dev.tensor.backend.events.EventCache;
import dev.tensor.backend.events.constant.MovementClipEvent;
import dev.tensor.backend.events.variable.EntityLightValueEvent;
import dev.tensor.backend.events.variable.VelocityEvent;
import dev.tensor.misc.imp.Globals;
import net.minecraft.src.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * @author Hoosiers
 * @since 04-02-2026
 */

@Mixin(value = Entity.class, priority = MixinPriority.VALUE)
public final class EntityMixin implements Globals {

    @Shadow
    public int entityId;

    @Inject(method = "applyEntityCollision", at = @At("HEAD"), cancellable = true)
    public void tensor_onApplyEntityCollision(Entity par1, CallbackInfo ci) {
        if (this.failsNullCheck()) {
            return;
        }

        EventCache.getCachedEvent(VelocityEvent.class).withVariables(VelocityEvent.Type.Entity).call(event -> {

            if (event.isCancelled()) {
                ci.cancel();
            }
        });
    }

    @Redirect(method = "moveEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/Entity;isSneaking()Z"))
    public boolean tensor_onMoveEntityInvokeIsSneaking(Entity instance) {
        if (this.failsNullCheck()) {
            return instance.isSneaking();
        }

        MovementClipEvent event = EventCache.getCachedEvent(MovementClipEvent.class).call();

        if (event.isCancelled()) {
            return true;
        }

        return instance.isSneaking();
    }

    @Inject(method = "getEntityBrightness", at = @At("HEAD"), cancellable = true)
    public void tensor_onGetEntityBrightness(float par1, CallbackInfoReturnable<Float> cir) {
        if (this.failsNullCheck()) {
            return;
        }

        if (this.isXrayEnabled()) {
            return;
        }

        Entity entity = this.getFilteredLoadedEntities().stream().filter(entity1 -> entity1.entityId == this.entityId).findFirst().orElse(null);

        if (entity == null || entity == this.getPlayer()) {
            return;
        }

        EventCache.getCachedEvent(EntityLightValueEvent.class).withVariables(entity).call(event -> {
            if (event.isCancelled()) {
                cir.setReturnValue(1F);
            }
        });
    }
}
