package dev.tensor.backend.mixins;

import dev.tensor.misc.imp.Globals;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author Hoosiers
 * @since 03-18-2026
 */

@Mixin(value = Minecraft.class, priority = Integer.MAX_VALUE)
public final class MinecraftMixin implements Globals {

    @Inject(method = "stop", at = @At("HEAD"))
    public void onStop(CallbackInfo ci) {

        this.getTensor().saveManagers();
    }
}
