package dev.tensor.feature.hacks.hud;

import dev.tensor.misc.imp.hack.Category;
import dev.tensor.misc.imp.hack.Hack;
import dev.tensor.misc.imp.hack.render.HUDObject;
import dev.tensor.misc.imp.hack.render.RenderOrder;
import dev.tensor.misc.imp.setting.settings.ColorNoAlphaSetting;
import dev.tensor.misc.imp.setting.settings.DoubleSetting;
import dev.tensor.misc.imp.setting.settings.EnumSetting;
import dev.tensor.misc.imp.setting.settings.IntegerSetting;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Optional;

/**
 * @author Hoosiers
 * @since 03-25-2026
 */

@Hack.Info(name = "HackList", category = Category.HUD, drawn = false, description = "Displays the currently enabled hacks.")
public final class HackList extends Hack implements HUDObject {

    @Override
    public RenderOrder getRenderOrder() {
        return RenderOrder.HUD_Text;
    }

    public final IntegerSetting x = new IntegerSetting("X", 0, 0, this.getMaxX());
    public final IntegerSetting y = new IntegerSetting("Y", 63, 0, this.getMaxY());
    public final DoubleSetting scale = new DoubleSetting("Scale", 1.0, 0.5, 2.0, 1);
    public final EnumSetting direction = new EnumSetting("Direction", Direction.Left, new String[]{
            "This sets how the text will be rendered.",
            "Left® Align text to the left.",
            "Right® Align text to the right."
    });
    public final EnumSetting add = new EnumSetting("Add", Add.Down, new String[]{
            "This is how hacks are added to the list.",
            "Up® Shifts the list up.",
            "Down® Shifts the list down"
    });
    public final EnumSetting colorType = new EnumSetting("Color Type", ColorType.All, new String[]{
            "This is how hacks are colored on the list.",
            "All® All hacks are colored to the same defined color from the following setting.",
            "Category® Hacks are colored by what category they belong to."
    });
    public final ColorNoAlphaSetting textColor = new ColorNoAlphaSetting("Text Color", new Color(255, 0, 0, 255), "Sets the color of hacks if the Color Type is set to 'All'.");

    private final ArrayList<Hack> hacksToRender = new ArrayList<>();

    private int widthCache = -1;

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
        if (this.widthCache != -1) {
            return this.widthCache;
        }

        int width = 0;

        Optional<Hack> longest = this.getHackManager().getHacks().stream().max(Comparator.comparing(hack -> this.getFontRenderer().getStringWidth(hack.NAME)));

        if (longest.isPresent()) {
            width = this.getFontRenderer().getStringWidth(longest.get().NAME);
        } else {
            this.widthCache = -1;
            return width + 2;
        }

        this.widthCache = width + 2;

        return this.widthCache;
    }

    @Override
    public int getHeight() {
        return 250;
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
    public void onDisable(boolean isNull) {
        this.widthCache = -1;
    }

    @Override
    public void onClientTick() {
        this.loadHacksToRender();
    }

    @Override
    public void render() {
        this.prepareScale();

        for (int i = 0; i < this.hacksToRender.size(); i++) {

            Hack hack = this.hacksToRender.get(i);

            this.drawString(hack.NAME, this.getDirectionOffset(hack.NAME), this.getAddOffset(this.add.getValue().equals(Add.Up) ? this.hacksToRender.size() - 1 - i : i), this.getColor(hack).getRGB());
        }

        this.finishScale();
    }

    private void loadHacksToRender() {
        this.hacksToRender.clear();
        this.getHackManager().getHacks().forEach(hack -> {
            if (hack.isEnabled() && hack.DRAWN) {
                this.hacksToRender.add(hack);
            }
        });
    }

    private int getDirectionOffset(String value) {
        if (this.direction.getValue().equals(Direction.Left)) {
            return 1;
        } else {

            int valueWidth = this.getFontRenderer().getStringWidth(value);

            return (this.getWidth() - valueWidth - 1);

        }
    }

    private int getAddOffset(int i) {
        if (this.add.getValue().equals(Add.Down)) {
            return (i * 10) + 1;
        } else {
            return (this.getHeight() - 10 - 1 - (i * 10));
        }
    }

    private Color getColor(Hack hack) {

        switch ((ColorType) this.colorType.getValue()) {
            case All:
                return this.textColor.getValueRainbow();
            case Category:
                return this.getColorFromCategory(hack.CATEGORY);
        }

        return new Color(255, 255, 255, 255);
    }

    private Color getColorFromCategory(Category category) {
        switch (category) {
            case HUD:
                return Color.LIGHT_GRAY;
            case Client:
                return Color.WHITE;
            case Combat:
                return Color.RED.brighter();
            case Movement:
                return Color.MAGENTA.brighter();
            case Render:
                return Color.CYAN.brighter();
            case Player:
                return Color.GREEN.brighter();
            case Misc:
                return Color.YELLOW.darker();
        }

        return new Color(255, 255, 255, 255);
    }

    private enum Direction {
        Left,
        Right
    }

    private enum Add {
        Up,
        Down
    }

    private enum ColorType {
        All,
        Category
    }
}
