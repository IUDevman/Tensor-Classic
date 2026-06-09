package dev.tensor.backend.mixins;

import dev.tensor.backend.MixinPriority;
import dev.tensor.backend.events.EventCache;
import dev.tensor.backend.events.variable.UpdateCapeEvent;
import dev.tensor.misc.imp.Globals;
import dev.tensor.misc.imp.betafix.BetaFix;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
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

    public EntityPlayerMixin(World world) {
        super(world);
    }

    @Inject(method = "updateCloak", at = @At("HEAD"), cancellable = true)
    public void tensor_onUpdateCloak(CallbackInfo ci) {
        if (this.failsNullCheck()) {
            this.betaFix_updateCloak();
            ci.cancel();
            return;
        }

        if (!this.shouldCallEvent(UpdateCapeEvent.class)) {
            this.betaFix_updateCloak();
            ci.cancel();
            return;
        }

        String username = this.username;

        if (username.isEmpty()) {
            this.betaFix_updateCloak();
            ci.cancel();
            return;
        }

        EventCache.getCachedEvent(UpdateCapeEvent.class).withVariables(username).call(true, event -> {

            if (event.isCancelled()) {
                this.playerCloakUrl = event.playerCloakUrl;
                this.cloakUrl = this.playerCloakUrl;
                ci.cancel();
            }
        });
    }

    //Adapted from @author tweska 2023; MIT LICENSE; https://github.com/tweska/BetaFix/blob/master/LICENSE
    @Unique
    private void betaFix_updateCloak() {
        this.playerCloakUrl = BetaFix.getCapeURL(this.username);
        this.cloakUrl = this.playerCloakUrl;
    }
}
