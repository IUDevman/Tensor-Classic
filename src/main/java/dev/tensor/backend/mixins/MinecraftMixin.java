package dev.tensor.backend.mixins;

import dev.tensor.backend.MixinPriority;
import dev.tensor.backend.events.*;
import dev.tensor.misc.imp.Globals;
import net.minecraft.client.Minecraft;
import net.minecraft.src.*;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author Hoosiers
 * @since 03-18-2026
 */

@Mixin(value = Minecraft.class, priority = MixinPriority.VALUE)
public abstract class MinecraftMixin implements Globals {

    @Shadow public boolean inGameHasFocus;

    @Shadow public GuiScreen currentScreen;

    @Shadow protected abstract void clickMouse(int i);

    @Shadow private int mouseTicksRan;

    @Shadow private int ticksRan;

    @Shadow public MovingObjectPosition objectMouseOver;

    @Inject(method = "runTick", at = @At("TAIL"))
    public void tensor_onRunTick(CallbackInfo ci) {
        if (this.failsNullCheck()) {
            return;
        }

        @SuppressWarnings("unused")
        ClientTickEvent clientTickEvent = new ClientTickEvent().call();
    }

    @Inject(method = "runTick", at = @At(value = "INVOKE", target = "Lorg/lwjgl/input/Keyboard;getEventKeyState()Z", shift = At.Shift.AFTER, ordinal = 1, remap = false))
    public void tensor_onGetEventKeyState(CallbackInfo ci) {
        if (this.failsNullCheck()) {
            return;
        }

        if (Keyboard.getEventKeyState()) {
            @SuppressWarnings("unused")
            KeyPressedEvent event = new KeyPressedEvent(Keyboard.getEventKey() == 0 ? Keyboard.getEventCharacter() + 256 : Keyboard.getEventKey()).call();
        }
    }

    @SuppressWarnings("DiscouragedShift")
    @Inject(method = "runTick", at = @At(value = "INVOKE", target = "Lorg/lwjgl/input/Mouse;getEventDWheel()I", shift = At.Shift.BEFORE, remap = false))
    public void tensor_onGetMouseState(CallbackInfo ci) {
        if (this.failsNullCheck() || this.getMinecraft().currentScreen != null) {
            return;
        }

        @SuppressWarnings("unused")
        MouseEvent event = new MouseEvent(Mouse.getDWheel(), Mouse.getEventButtonState(), Mouse.getEventButton()).call();
    }

    @Inject(method = "changeWorld", at = @At("HEAD"))
    public void tensor_onChangeWorld(World string, String arg2, EntityPlayer par3, CallbackInfo ci) {
        if (this.failsNullCheck()) {
            return;
        }

        @SuppressWarnings("unused")
        ChangeWorldEvent event = new ChangeWorldEvent().call();
    }

    @Inject(method = "runTick", at = @At(value = "INVOKE", target = "Lorg/lwjgl/input/Mouse;isButtonDown(I)Z", ordinal = 1, remap = false))
    public void tensor_onIsMouseButtonDown(CallbackInfo ci) {
        if (this.failsNullCheck()) {
            return;
        }

        if (!this.inGameHasFocus) {
            return;
        }

        if (this.currentScreen != null) {
            return;
        }

        if (!Mouse.isButtonDown(1)) {
            return;
        }

        if (this.objectMouseOver == null) {
            return;
        }

        if (!this.objectMouseOver.typeOfHit.equals(EnumMovingObjectType.TILE)) {
            return;
        }

        BlockClickToPlaceEvent event = new BlockClickToPlaceEvent().call();

        if (event.isCancelled()) {
            this.clickMouse(1);

            this.mouseTicksRan = this.ticksRan;
        }
    }
}
