package dev.tensor.backend.mixins;

import dev.tensor.backend.MixinPriority;
import dev.tensor.backend.events.EventCache;
import dev.tensor.backend.events.variable.AttackEntityEvent;
import dev.tensor.misc.imp.Globals;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.PlayerControllerMP;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author Hoosiers
 * @since 04-23-2026
 */

@Mixin(value = PlayerControllerMP.class, priority = MixinPriority.VALUE)
public final class PlayerControllerMPMixin implements Globals {

    @Inject(method = "attackEntity", at = @At("HEAD"))
    public void tensor_onAttackEntity(EntityPlayer arg2, Entity par2, CallbackInfo ci) {
        if (this.failsNullCheck()) {
            return;
        }

        if (this.getPlayer() != arg2) {
            return;
        }

        if (par2 == null) {
            return;
        }

        EventCache.getCachedEvent(AttackEntityEvent.class).withVariables(par2).call();
    }
}
