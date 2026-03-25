package dev.tensor.feature.hacks.HUD.test;

import dev.tensor.misc.imp.hack.Category;
import dev.tensor.misc.imp.hack.HUDObject;
import dev.tensor.misc.imp.hack.Hack;
import dev.tensor.misc.imp.setting.settings.IntegerSetting;

import java.awt.*;

/**
 * @author Hoosiers
 * @since 03-25-2026
 */

@Hack.Info(name = "DummyHUDHack", category = Category.HUD)
public final class DummyHUDHack extends Hack implements HUDObject {

    public final IntegerSetting x = new IntegerSetting("X", 100, 0, this.getMaxX());
    public final IntegerSetting y = new IntegerSetting("Y", 100, 0, this.getMaxY());

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
        return 100;
    }

    @Override
    public int getHeight() {
        return 100;
    }

    @Override
    public void render() {
        this.drawRectangle(this.x.getValue(), this.y.getValue(), this.x.getValue() + 100, this.y.getValue() + 100, Color.BLACK.getRGB());
    }
}
