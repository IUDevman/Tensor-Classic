package dev.tensor.backend.mixins;

import dev.tensor.backend.MixinPriority;
import dev.tensor.backend.events.EventCache;
import dev.tensor.backend.events.variable.UpdateCapeEvent;
import dev.tensor.misc.imp.Globals;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author Hoosiers
 * @since 05-02-2026
 */

@Mixin(value = EntityPlayer.class, priority = MixinPriority.VALUE)
public abstract class EntityPlayerMixin extends Entity implements Globals {

    @Shadow
    public String username;

    @Shadow
    public String playerCloakUrl;

    public EntityPlayerMixin(World arg) {
        super(arg);
    }

    @Inject(method = "updateCloak", at = @At("HEAD"), cancellable = true)
    public void tensor_onUpdateCloak(CallbackInfo ci) {
        if (this.failsNullCheck()) {
            return;
        }

        String username = this.username;

        if (username.isEmpty()) {
            return;
        }

        UpdateCapeEvent event = EventCache.getCachedEvent(UpdateCapeEvent.class).withVariables(username).call();

        if (event.isCancelled()) {
            this.playerCloakUrl = event.playerCloakUrl;
            this.cloakUrl = this.playerCloakUrl;
            ci.cancel();
        }
    }
}
