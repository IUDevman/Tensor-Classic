package dev.tensor.backend.mixins;

import dev.tensor.backend.MixinPriority;
import dev.tensor.misc.imp.Globals;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityOtherPlayerMP;
import net.minecraft.src.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author Hoosiers
 * @since 06-09-2026
 */

@Mixin(value = EntityOtherPlayerMP.class, priority = MixinPriority.VALUE)
public abstract class EntityOtherPlayerMPMixin extends Entity implements Globals {

    public EntityOtherPlayerMPMixin(World world) {
        super(world);
    }

    //Adapted from @author tweska 2023; MIT LICENSE; https://github.com/tweska/BetaFix/blob/master/LICENSE
    @Inject(method = "<init>", at = @At("TAIL"))
    public void betaFix_init(World world, String username, CallbackInfo ci) {
        if (username != null && !username.isEmpty()) {
            this.skinUrl = "http://s3.amazonaws.com/MinecraftSkins/" + username + ".png";
        }
    }
}
