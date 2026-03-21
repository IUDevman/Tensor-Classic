package dev.tensor.backend.mixins;

import dev.tensor.backend.events.Render2DEvent;
import dev.tensor.misc.imp.Globals;
import net.minecraft.src.GuiIngame;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author Hoosiers
 * @since 03-20-2026
 */

@Mixin(value = GuiIngame.class, priority = Integer.MAX_VALUE)
public final class GuiIngameMixin implements Globals {

    @Inject(method = "renderGameOverlay", at = @At("TAIL"))
    public void onRenderGameOverlay(float bl, boolean i, int j, int par4, CallbackInfo ci) {
        if (this.failsNullCheck() || this.getInGameGUI() == null) {
            return;
        }

        Render2DEvent event = new Render2DEvent();
        this.getEventManager().call(event);
    }
}
