package dev.tensor.backend.mixins;

import dev.tensor.backend.MixinPriority;
import dev.tensor.backend.TensorRedirect;
import dev.tensor.backend.events.EventCache;
import dev.tensor.backend.events.constant.PortalCloseGUIEvent;
import dev.tensor.backend.events.variable.SignEvent;
import dev.tensor.backend.events.variable.VelocityEvent;
import dev.tensor.misc.imp.Globals;
import net.minecraft.client.Minecraft;
import net.minecraft.src.EntityPlayerSP;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.TileEntitySign;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * @author Hoosiers
 * @since 04-02-2026
 */

@Mixin(value = EntityPlayerSP.class, priority = MixinPriority.VALUE)
public final class EntityPlayerSPMixin implements Globals {

    @Inject(method = "pushOutOfBlocks", at = @At("HEAD"), cancellable = true)
    public void tensor_onPushOutOfBlocks(double x, double y, double z, CallbackInfoReturnable<Boolean> cir) {
        if (this.failsNullCheck()) {
            return;
        }

        if (!this.shouldCallEvent(VelocityEvent.class)) {
            return;
        }

        EventCache.getCachedEvent(VelocityEvent.class).withVariables(VelocityEvent.Type.Block).call(true, event -> {

            if (event.isCancelled()) {
                cir.setReturnValue(false);
            }
        });
    }

    @TensorRedirect
    @Redirect(method = "onLivingUpdate", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;displayGuiScreen(Lnet/minecraft/src/GuiScreen;)V"))
    public void tensor_onLivingUpdateInvokeGuiScreen(Minecraft minecraft, GuiScreen guiScreen) {
        if (guiScreen == null && this.shouldCallEvent(PortalCloseGUIEvent.class)) {
            EventCache.getCachedEvent(PortalCloseGUIEvent.class).call(true, event -> {

                if (!event.isCancelled()) {
                    minecraft.displayGuiScreen(null);
                }
            });

        } else {
            minecraft.displayGuiScreen(guiScreen);
        }
    }

    @Inject(method = "displayGUIEditSign", at = @At("HEAD"), cancellable = true)
    public void tensor_onDisplayGUIEditSign(TileEntitySign tileEntitySign, CallbackInfo ci) {
        if (this.failsNullCheck()) {
            return;
        }

        if (!this.shouldCallEvent(SignEvent.class)) {
            return;
        }

        EventCache.getCachedEvent(SignEvent.class).withVariables(tileEntitySign).call(true, event -> {

            if (event.isCancelled()) {
                ci.cancel();
            }
        });
    }
}
