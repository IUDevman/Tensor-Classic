package dev.tensor.backend.mixins;

import dev.tensor.backend.events.PortalCloseGUIEvent;
import dev.tensor.backend.events.VelocityEvent;
import dev.tensor.misc.imp.Globals;
import net.minecraft.client.Minecraft;
import net.minecraft.src.EntityPlayerSP;
import net.minecraft.src.GuiScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * @author Hoosiers
 * @since 04-02-2026
 */

@Mixin(value = EntityPlayerSP.class, priority = Integer.MAX_VALUE)
public final class EntityPlayerSPMixin implements Globals {

    @Inject(method = "pushOutOfBlocks", at = @At("HEAD"), cancellable = true)
    public void onPushOutOfBlocks(double e, double f, double par3, CallbackInfoReturnable<Boolean> cir) {
        if (this.failsNullCheck()) {
            return;
        }

        VelocityEvent event = new VelocityEvent(VelocityEvent.Type.Block);
        this.getEventManager().call(event);

        if (event.isCancelled()) {
            cir.setReturnValue(false);
        }
    }

    @Redirect(method = "onLivingUpdate", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;displayGuiScreen(Lnet/minecraft/src/GuiScreen;)V"))
    public void onLivingUpdateInvokeGuiScreen(Minecraft instance, GuiScreen guiScreen) {
        if (guiScreen == null) {
            PortalCloseGUIEvent event = new PortalCloseGUIEvent();
            this.getEventManager().call(event);

            if (!event.isCancelled()) {
                instance.displayGuiScreen(null);
            }
        } else {
            instance.displayGuiScreen(guiScreen);
        }
    }
}
