package dev.tensor.backend.mixins;

import dev.tensor.backend.MixinPriority;
import dev.tensor.backend.events.EventCache;
import dev.tensor.backend.events.constant.MovementClipEvent;
import dev.tensor.backend.events.variable.EntityLightValueEvent;
import dev.tensor.backend.events.variable.VelocityEvent;
import dev.tensor.feature.hacks.Fullbright;
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

    @Shadow
    public float entityBrightness;

    @Inject(method = "applyEntityCollision", at = @At("HEAD"), cancellable = true)
    public void tensor_onApplyEntityCollision(Entity entity, CallbackInfo ci) {
        if (this.failsNullCheck()) {
            return;
        }

        if (!this.shouldCallEvent(VelocityEvent.class)) {
            return;
        }

        EventCache.getCachedEvent(VelocityEvent.class).withVariables(VelocityEvent.Type.Entity).call(true, event -> {

            if (event.isCancelled()) {
                ci.cancel();
            }
        });
    }

    @Redirect(method = "moveEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/Entity;isSneaking()Z"))
    public boolean tensor_onMoveEntityInvokeIsSneaking(Entity entity) {
        if (this.failsNullCheck()) {
            return entity.isSneaking();
        }

        if (!this.shouldCallEvent(MovementClipEvent.class)) {
            return entity.isSneaking();
        }

        MovementClipEvent event = EventCache.getCachedEvent(MovementClipEvent.class).call(true);

        if (event.isCancelled()) {
            return true;
        }

        return entity.isSneaking();
    }

    @Inject(method = "getEntityBrightness", at = @At("HEAD"), cancellable = true)
    public void tensor_onGetEntityBrightness(float partialTicks, CallbackInfoReturnable<Float> cir) {
        if (this.failsNullCheck()) {
            return;
        }

        if (this.entityBrightness == 1F) {
            return;
        }

        if (this.isXrayEnabled()) {
            return;
        }

        if (!this.shouldCallEvent(EntityLightValueEvent.class)) {
            return;
        }

        Fullbright fullbright = this.getHackManager().getHack(Fullbright.class);

        if (fullbright != null && fullbright.isEnabled() && !fullbright.ugandaMode.getValue()) {
            return;
        }

        if (this.entityId == this.getPlayer().entityId) {
            return;
        }

        EventCache.getCachedEvent(EntityLightValueEvent.class).withVariables(this.entityId).call(true, event -> {
            if (event.isCancelled()) {
                cir.setReturnValue(1F);
            }
        });
    }
}
