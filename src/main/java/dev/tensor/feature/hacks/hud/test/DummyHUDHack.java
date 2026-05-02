package dev.tensor.feature.hacks.hud.test;

import dev.tensor.misc.imp.hack.Category;
import dev.tensor.misc.imp.hack.Hack;
import dev.tensor.misc.imp.hack.render.HUDObject;
import dev.tensor.misc.imp.setting.settings.DoubleSetting;
import dev.tensor.misc.imp.setting.settings.IntegerSetting;

import java.awt.*;

/**
 * @author Hoosiers
 * @since 03-25-2026
 */

@SuppressWarnings("unused")
@Hack.Info(name = "DummyHUDHack", category = Category.HUD)
public final class DummyHUDHack extends Hack implements HUDObject {

    public final IntegerSetting x = new IntegerSetting("X", 100, 0, this.getMaxX());
    public final IntegerSetting y = new IntegerSetting("Y", 100, 0, this.getMaxY());
    public final DoubleSetting scale = new DoubleSetting("Scale", 1.0, 0.5, 3.0, 1);

    @Override
    public IntegerSetting getMinX() {
        return this.x;
    }

    @Override
    public IntegerSetting getMinY() {
        return this.y;
    }

    @Override
    public DoubleSetting getScale() {
        return this.scale;
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
    public int getWidthScaled() {
        return (int) (this.getWidth() * this.scale.getValue());
    }

    @Override
    public int getHeightScaled() {
        return (int) (this.getHeight() * this.scale.getValue());
    }

    @Override
    public void render() {
        this.prepareScale();

        this.drawRectangle(0, 0, 100, 100, Color.BLACK.getRGB());

        this.finishScale();
    }
}
