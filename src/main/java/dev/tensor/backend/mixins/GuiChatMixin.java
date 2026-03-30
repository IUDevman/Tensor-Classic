package dev.tensor.backend.mixins;

import dev.tensor.backend.events.GuiChatScreenEvent;
import dev.tensor.misc.imp.Globals;
import net.minecraft.src.GuiChat;
import net.minecraft.src.GuiScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author Hoosiers
 * @since 03-24-2026
 */

@Mixin(value = GuiChat.class, priority = Integer.MAX_VALUE)
public final class GuiChatMixin extends GuiScreen implements Globals {

    @Shadow
    private String message;

    @Inject(method = "drawScreen", at = @At("HEAD"))
    public void onDrawScreen(int j, int f, float par3, CallbackInfo ci) {
        if (this.failsNullCheck()) {
            return;
        }

        if (this.message == null) {
            return;
        }

        GuiChatScreenEvent event = new GuiChatScreenEvent(this.message, 2, this.width - 2, this.height - 14, this.height - 2);
        this.getEventManager().call(event);
    }
}
