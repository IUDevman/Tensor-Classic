package dev.tensor.backend.mixins;

import dev.tensor.backend.MixinPriority;
import dev.tensor.backend.events.EventCache;
import dev.tensor.backend.events.constant.ChangeWorldEvent;
import dev.tensor.backend.events.constant.ClientTickEvent;
import dev.tensor.backend.events.variable.BlockClickToPlaceEvent;
import dev.tensor.backend.events.variable.KeyPressedEvent;
import dev.tensor.backend.events.variable.MouseEvent;
import dev.tensor.misc.imp.Globals;
import net.minecraft.client.Minecraft;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.MovingObjectPosition;
import net.minecraft.src.World;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author Hoosiers
 * @since 03-18-2026
 */

@Mixin(value = Minecraft.class, priority = MixinPriority.VALUE)
public abstract class MinecraftMixin implements Globals {

    @Shadow
    public boolean inGameHasFocus;

    @Shadow
    public GuiScreen currentScreen;

    @Shadow
    protected abstract void clickMouse(int i);

    @Shadow
    private int mouseTicksRan;

    @Shadow
    private int ticksRan;

    @Shadow
    public MovingObjectPosition objectMouseOver;

    @Inject(method = "runTick", at = @At("TAIL"))
    public void tensor_onRunTick(CallbackInfo ci) {
        if (this.failsNullCheck()) {
            return;
        }

        EventCache.getCachedEvent(ClientTickEvent.class).call();
    }

    @Inject(method = "runTick", at = @At(value = "INVOKE", target = "Lorg/lwjgl/input/Keyboard;getEventKeyState()Z", shift = At.Shift.AFTER, ordinal = 1, remap = false))
    public void tensor_onGetEventKeyState(CallbackInfo ci) {
        if (this.failsNullCheck()) {
            return;
        }

        if (Keyboard.getEventKeyState()) {

            @SuppressWarnings("unused")
            KeyPressedEvent event = EventCache.getCachedEvent(KeyPressedEvent.class).withVariables(Keyboard.getEventKey() == 0 ? Keyboard.getEventCharacter() + 256 : Keyboard.getEventKey()).call();
        }
    }

    @SuppressWarnings("DiscouragedShift")
    @Inject(method = "runTick", at = @At(value = "INVOKE", target = "Lorg/lwjgl/input/Mouse;getEventDWheel()I", shift = At.Shift.BEFORE, remap = false))
    public void tensor_onGetMouseState(CallbackInfo ci) {
        if (this.failsNullCheck() || this.getMinecraft().currentScreen != null) {
            return;
        }

        @SuppressWarnings("unused")
        MouseEvent event = EventCache.getCachedEvent(MouseEvent.class).withVariables(Mouse.getDWheel(), Mouse.getEventButtonState(), Mouse.getEventButton()).call();
    }

    @Inject(method = "changeWorld", at = @At("HEAD"))
    public void tensor_onChangeWorld(World string, String arg2, EntityPlayer par3, CallbackInfo ci) {
        if (this.failsNullCheck()) {
            return;
        }

        @SuppressWarnings("unused")
        ChangeWorldEvent event = EventCache.getCachedEvent(ChangeWorldEvent.class).call();
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

        BlockClickToPlaceEvent event = EventCache.getCachedEvent(BlockClickToPlaceEvent.class).withVariables(1).call();

        if (event.isCancelled()) {
            for (int i = 0; i < event.count; i++) {
                this.tensor_runMouseClick();
            }
        }
    }

    @Unique
    private void tensor_runMouseClick() {
        this.clickMouse(1);

        this.mouseTicksRan = this.ticksRan;
    }
}
