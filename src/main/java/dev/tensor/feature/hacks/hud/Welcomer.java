package dev.tensor.feature.hacks.hud;

import dev.tensor.TensorClassic;
import dev.tensor.misc.imp.hack.Category;
import dev.tensor.misc.imp.hack.Hack;
import dev.tensor.misc.imp.hack.render.HUDObject;
import dev.tensor.misc.imp.hack.render.RenderOrder;
import dev.tensor.misc.imp.setting.settings.ColorSetting;
import dev.tensor.misc.imp.setting.settings.DoubleSetting;
import dev.tensor.misc.imp.setting.settings.IntegerSetting;

import java.awt.*;

/**
 * @author Hoosiers
 * @since 03-25-2026
 */

@Hack.Info(name = "Welcomer", category = Category.HUD, enabled = true)
public final class Welcomer extends Hack implements HUDObject {

    @Override
    public RenderOrder getRenderOrder() {
        return RenderOrder.HUD_Text;
    }

    public final IntegerSetting x = new IntegerSetting("X", 550, 0, this.getMaxX());
    public final IntegerSetting y = new IntegerSetting("Y", 0, 0, this.getMaxY());
    public final DoubleSetting scale = new DoubleSetting("Scale", 1.0, 0.5, 3.0, 1);
    public final ColorSetting textColor = new ColorSetting("Text Color", new Color(255, 255, 0, 255));

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
        return this.getFontRenderer().getStringWidth(this.getWelcomer()) + 2;
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
        this.prepareScale();

        String welcome = this.getWelcomer();
        this.drawCenteredString(welcome, 1 + (this.getFontRenderer().getStringWidth(welcome) / 2), 1, this.textColor.getValueRainbow().getRGB());

        this.finishScale();
    }

    private String getWelcomer() {

        if (this.failsNullCheck()) {
            return "Welcome Null to " + TensorClassic.MOD_NAME + " " + TensorClassic.MOD_VERSION + "!";
        }

        return "Welcome " + this.getPlayer().username + " to " + TensorClassic.MOD_NAME + " " + TensorClassic.MOD_VERSION + "!";
    }
}
