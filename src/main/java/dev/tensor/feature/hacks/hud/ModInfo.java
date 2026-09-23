package dev.tensor.feature.hacks.hud;

import dev.tensor.TensorClassic;
import dev.tensor.misc.imp.hack.Category;
import dev.tensor.misc.imp.hack.Hack;
import dev.tensor.misc.imp.hack.render.HUDObject;
import dev.tensor.misc.imp.hack.render.RenderOrder;
import dev.tensor.misc.imp.setting.settings.ColorNoAlphaSetting;
import dev.tensor.misc.imp.setting.settings.DoubleSetting;
import dev.tensor.misc.imp.setting.settings.IntegerSetting;

import java.awt.*;

/**
 * @author Hoosiers
 * @since 03-25-2026
 */

@Hack.Info(name = "ModInfo", category = Category.HUD, description = "Displays the client name and version.")
public final class ModInfo extends Hack implements HUDObject {

    @Override
    public RenderOrder getRenderOrder() {
        return RenderOrder.HUD_Text;
    }

    public final IntegerSetting x = new IntegerSetting("X", 0, 0, this.getMaxX(), "None", "Where the HUD object should begin rendering on the screen X axis.");
    public final IntegerSetting y = new IntegerSetting("Y", 0, 0, this.getMaxY(), "None", "Where the HUD object should begin rendering on the screen Y axis.");
    public final DoubleSetting scale = new DoubleSetting("Scale", 1.0, 0.5, 3.0, 1, "None", "How big the HUD object should be.");
    public final ColorNoAlphaSetting textColor = new ColorNoAlphaSetting("Text Color", new Color(255, 255, 0, 255), "Sets the text color.");

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
    public DoubleSetting getScale() {
        return this.scale;
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

        this.drawString(this.modInfo, 1, 1, this.textColor.getValueRainbow().getRGB());

        this.finishScale();
    }
}
