package dev.tensor.backend.mixins;

import dev.tensor.backend.MixinPriority;
import dev.tensor.backend.events.EventCache;
import dev.tensor.backend.events.variable.GuiChatScreenEvent;
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

@Mixin(value = GuiChat.class, priority = MixinPriority.VALUE)
public final class GuiChatMixin extends GuiScreen implements Globals {

    @Shadow
    private String message;

    @Inject(method = "drawScreen", at = @At("HEAD"))
    public void tensor_onDrawScreen(int x, int y, float partialTicks, CallbackInfo ci) {
        if (this.failsNullCheck()) {
            return;
        }

        if (this.message == null) {
            return;
        }

        if (!this.shouldCallEvent(GuiChatScreenEvent.class)) {
            return;
        }

        EventCache.getCachedEvent(GuiChatScreenEvent.class).withVariables(this.message, 2, this.width - 2, this.height - 14, this.height - 2).call(true);
    }
}
