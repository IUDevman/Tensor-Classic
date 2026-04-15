package dev.tensor.feature.hacks.hud;

import dev.tensor.misc.imp.hack.Category;
import dev.tensor.misc.imp.hack.Hack;
import dev.tensor.misc.imp.hack.render.HUDObject;
import dev.tensor.misc.imp.setting.settings.DoubleSetting;
import dev.tensor.misc.imp.setting.settings.IntegerSetting;

/**
 * @author Hoosiers
 * @since 04-14-2026
 */

@Hack.Info(name = "FPS", category = Category.HUD)
public final class FPS extends Hack implements HUDObject {

    public final IntegerSetting x = new IntegerSetting("X", 0, 0, this.getMaxX());
    public final IntegerSetting y = new IntegerSetting("Y", 37, 0, this.getMaxY());
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
        return 12;
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

    }
}
