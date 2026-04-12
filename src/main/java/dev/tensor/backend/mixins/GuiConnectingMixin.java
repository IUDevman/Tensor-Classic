package dev.tensor.backend.mixins;

import dev.tensor.backend.MixinPriority;
import dev.tensor.misc.imp.Globals;
import net.minecraft.client.Minecraft;
import net.minecraft.src.GuiConnecting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author Hoosiers
 * @since 04-11-2026
 */

@Mixin(value = GuiConnecting.class, priority = MixinPriority.VALUE)
public final class GuiConnectingMixin implements Globals {

    @Inject(method = "<init>", at = @At("TAIL"))
    public void tensor_onInit(Minecraft string, String i, int par3, CallbackInfo ci) {
        if (this.getAutoReconnect() != null) {
            this.getAutoReconnect().setServerLastName(i);
            this.getAutoReconnect().setServerLastPort(par3);
        }
    }
}
