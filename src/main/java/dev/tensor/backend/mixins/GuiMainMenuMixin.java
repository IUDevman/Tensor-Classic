package dev.tensor.backend.mixins;

import dev.tensor.backend.MixinPriority;
import dev.tensor.misc.gui.TensorMainMenu;
import dev.tensor.misc.imp.render.Render2D;
import net.minecraft.src.GuiButton;
import net.minecraft.src.GuiMainMenu;
import net.minecraft.src.GuiScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author Hoosiers
 * @since 06-07-2026
 */

@Mixin(value = GuiMainMenu.class, priority = MixinPriority.VALUE)
public final class GuiMainMenuMixin extends GuiScreen implements Render2D {

    @Inject(method = "initGui", at = @At("HEAD"), cancellable = true)
    public void tensor_onInitGui(CallbackInfo ci) {
        TensorMainMenu.getInstance().onInitialization(this.width, this.height, this.controlList);

        if (TensorMainMenu.getInstance().isActive()) {
            ci.cancel();
        }
    }

    @Inject(method = "actionPerformed", at = @At("HEAD"), cancellable = true)
    public void tensor_onActionPerformed(GuiButton guiButton, CallbackInfo ci) {
        TensorMainMenu.getInstance().onActionPerformed(this, guiButton);

        if (TensorMainMenu.getInstance().isActive()) {
            ci.cancel();
        }
    }

    @Inject(method = "drawScreen", at = @At("HEAD"), cancellable = true)
    public void tensor_onDrawScreen(int x, int y, float partialTicks, CallbackInfo ci) {
        TensorMainMenu.getInstance().onDrawScreen(this.width, this.height);

        if (TensorMainMenu.getInstance().isActive()) {
            super.drawScreen(x, y, partialTicks);

            ci.cancel();
        }
    }
}
