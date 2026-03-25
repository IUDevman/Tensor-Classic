package dev.tensor.feature.hacks.HUD;

import dev.tensor.misc.imp.hack.Category;
import dev.tensor.misc.imp.hack.HUDObject;
import dev.tensor.misc.imp.hack.Hack;
import dev.tensor.misc.imp.setting.settings.ColorSetting;
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

@Hack.Info(name = "HackList", category = Category.HUD, drawn = false)
public final class HackList extends Hack implements HUDObject {

    public final IntegerSetting x = new IntegerSetting("X", 0, 0, this.getMaxX());
    public final IntegerSetting y = new IntegerSetting("Y", 15, 0, this.getMaxY());
    public final EnumSetting direction = new EnumSetting("Direction", Direction.Left);
    public final EnumSetting add = new EnumSetting("Add", Add.Down);
    public final EnumSetting colorType = new EnumSetting("Color Type", ColorType.All);
    public final ColorSetting textColor = new ColorSetting("Text Color", new Color(255, 0, 0, 255));

    private final ArrayList<Hack> hacksToRender = new ArrayList<>();

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
        int width = 0;

        this.loadHacksToRender();

        Optional<Hack> longest = this.hacksToRender.stream().max(Comparator.comparing(hack -> hack.NAME.length()));

        if (longest.isPresent()) {
            width = this.getFontRenderer().getStringWidth(longest.get().NAME);
        }

        return width + 2;
    }

    @Override
    public int getHeight() {
        return (this.hacksToRender.size() * 10) + 2;
    }

    @Override
    public void render() {

        this.loadHacksToRender();

        for (int i = 0; i < this.hacksToRender.size(); i++) {

            Hack hack = this.hacksToRender.get(i);

            this.drawString(hack.NAME, this.x.getValue() + this.getDirectionOffset(hack.NAME), this.y.getValue() + this.getAddOffset(i), this.getColor(hack).getRGB());
        }
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
                return this.textColor.getValue();
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
                return Color.WHITE; //
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
