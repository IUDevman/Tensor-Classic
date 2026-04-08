package dev.tensor.backend.mixins;

import dev.tensor.backend.MixinPriority;
import dev.tensor.backend.events.ClientTickEvent;
import dev.tensor.backend.events.KeyPressedEvent;
import dev.tensor.misc.imp.Globals;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author Hoosiers
 * @since 03-18-2026
 */

@Mixin(value = Minecraft.class, priority = MixinPriority.VALUE)
public final class MinecraftMixin implements Globals {

    @Inject(method = "runTick", at = @At("TAIL"))
    public void tensor_onRunTick(CallbackInfo ci) {
        if (this.failsNullCheck()) {
            return;
        }

        @SuppressWarnings("unused")
        ClientTickEvent clientTickEvent = new ClientTickEvent().call();
    }

    @Inject(method = "runTick", at = @At(value = "INVOKE", target = "Lorg/lwjgl/input/Keyboard;getEventKeyState()Z", shift = At.Shift.AFTER, ordinal = 1))
    public void tensor_onGetEventKeyState(CallbackInfo ci) {
        if (this.failsNullCheck()) {
            return;
        }

        if (Keyboard.getEventKeyState()) {
            @SuppressWarnings("unused")
            KeyPressedEvent event = new KeyPressedEvent(Keyboard.getEventKey() == 0 ? Keyboard.getEventCharacter() + 256 : Keyboard.getEventKey()).call();
        }
    }
}
