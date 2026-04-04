package dev.tensor.feature.hacks;

import dev.tensor.misc.event.EventTarget;
import dev.tensor.misc.imp.hack.Category;
import dev.tensor.misc.imp.hack.Hack;
import dev.tensor.misc.imp.setting.settings.BooleanSetting;
import net.minecraft.src.GuiChat;
import net.minecraft.src.GuiEditSign;
import org.lwjgl.input.Keyboard;

/**
 * @author Hoosiers
 * @since 04-04-2026
 */

@Hack.Info(name = "InventoryMove", category = Category.Movement)
public final class InventoryMove extends Hack {

    public final BooleanSetting jump = new BooleanSetting("Jump", true);
    public final BooleanSetting sneak = new BooleanSetting("Sneak", false);

    @Override
    public void onDisable(boolean isNull) {
        if (!isNull) {
            this.handleMove(true);
        }
    }

    @SuppressWarnings("unused")
    @EventTarget
    public void onClientTick() {
        if (this.getMinecraft().currentScreen == null) {
            return;
        }

        ClickGUI clickGUI = this.getHackManager().getHack(ClickGUI.class);

        if (clickGUI.isEnabled() && clickGUI.tensorGUI != null && clickGUI.tensorGUI.isSearching()) {
            this.handleMove(true);
            return;
        }

        if (this.getMinecraft().currentScreen instanceof GuiChat || this.getMinecraft().currentScreen instanceof GuiEditSign) {
            this.handleMove(true);
            return;
        }

        this.handleMove(false);
    }

    private void handleMove(boolean stop) {

        AutoWalk autoWalk = this.getHackManager().getHack(AutoWalk.class);

        if (!autoWalk.isEnabled()) {
            this.getPlayer().handleKeyPress(this.getMinecraft().gameSettings.keyBindForward.keyCode, !stop && Keyboard.isKeyDown(this.getMinecraft().gameSettings.keyBindForward.keyCode));
        } else {
            this.getPlayer().handleKeyPress(this.getMinecraft().gameSettings.keyBindForward.keyCode, !Keyboard.isKeyDown(this.getMinecraft().gameSettings.keyBindBack.keyCode));
        }

        this.getPlayer().handleKeyPress(this.getMinecraft().gameSettings.keyBindBack.keyCode, !stop && Keyboard.isKeyDown(this.getMinecraft().gameSettings.keyBindBack.keyCode));
        this.getPlayer().handleKeyPress(this.getMinecraft().gameSettings.keyBindLeft.keyCode, !stop && Keyboard.isKeyDown(this.getMinecraft().gameSettings.keyBindLeft.keyCode));
        this.getPlayer().handleKeyPress(this.getMinecraft().gameSettings.keyBindRight.keyCode, !stop && Keyboard.isKeyDown(this.getMinecraft().gameSettings.keyBindRight.keyCode));

        if (this.jump.getValue()) {
            this.getPlayer().handleKeyPress(this.getMinecraft().gameSettings.keyBindJump.keyCode, !stop && Keyboard.isKeyDown(this.getMinecraft().gameSettings.keyBindJump.keyCode));
        }

        if (this.sneak.getValue()) {
            this.getPlayer().handleKeyPress(this.getMinecraft().gameSettings.keyBindSneak.keyCode, !stop && Keyboard.isKeyDown(this.getMinecraft().gameSettings.keyBindSneak.keyCode));
        }
    }
}
