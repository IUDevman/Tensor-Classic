package dev.tensor.feature.hacks.hud;

import dev.tensor.TensorClassic;
import dev.tensor.misc.imp.hack.Category;
import dev.tensor.misc.imp.hack.HUDObject;
import dev.tensor.misc.imp.hack.Hack;
import dev.tensor.misc.imp.setting.settings.ColorSetting;
import dev.tensor.misc.imp.setting.settings.IntegerSetting;

import java.awt.*;

/**
 * @author Hoosiers
 * @since 03-25-2026
 */

@Hack.Info(name = "ModInfo", category = Category.HUD)
public final class ModInfo extends Hack implements HUDObject {

    public final IntegerSetting x = new IntegerSetting("X", 0, 0, this.getMaxX());
    public final IntegerSetting y = new IntegerSetting("Y", 0, 0, this.getMaxY());
    public final ColorSetting textColor = new ColorSetting("Text Color", new Color(255, 255, 0, 255));

    private final String modInfo = TensorClassic.MOD_NAME + " " + TensorClassic.MOD_VERSION;

    @Override
    public IntegerSetting getMinX() {
        return this.x;
    }

    @Override
    public IntegerSetting getMinY() {
        return this.y;
    }

    @Override
    public int getWidth() {
        return this.getFontRenderer().getStringWidth(modInfo) + 2;
    }

    @Override
    public int getHeight() {
        return 12;
    }

    @Override
    public void render() {

        this.drawString(this.modInfo, this.x.getValue() + 1, this.y.getValue() + 1, this.textColor.getValue().getRGB());
    }
}
