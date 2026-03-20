package dev.tensor.backend.mixins;

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

@Mixin(value = Minecraft.class, priority = Integer.MAX_VALUE)
public final class MinecraftMixin implements Globals {

    @Inject(method = "runTick", at = @At("TAIL"))
    public void onRunTick(CallbackInfo ci) {
        if (this.failsNullCheck()) {
            return;
        }

        ClientTickEvent clientTickEvent = new ClientTickEvent();
        this.getEventManager().call(clientTickEvent);
    }

    /**
     * @author qe7
     * @skidded from https://github.com/qe7/Hephaestus-Babric/blob/main/src/main/java/io/github/qe7/mixins/MixinMinecraft.java#L6
     */
    @Inject(method = "runTick", at = @At(value = "INVOKE", target = "Lorg/lwjgl/input/Keyboard;getEventKeyState()Z", shift = At.Shift.AFTER, ordinal = 1))
    public void onGetEventKeyState(CallbackInfo ci) {
        if (this.failsNullCheck()) {
            return;
        }

        if (Keyboard.getEventKeyState()) {
            KeyPressedEvent event = new KeyPressedEvent(Keyboard.getEventKey() == 0 ? Keyboard.getEventCharacter() + 256 : Keyboard.getEventKey());
            this.getEventManager().call(event);
        }
    }

    @Inject(method = "shutdown", at = @At("HEAD"))
    public void onShutdown(CallbackInfo ci) {

        this.getTensor().saveManagers();
    }
}
