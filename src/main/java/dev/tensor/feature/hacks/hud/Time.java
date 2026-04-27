package dev.tensor.feature.hacks.hud;

import dev.tensor.misc.imp.hack.Category;
import dev.tensor.misc.imp.hack.Hack;
import dev.tensor.misc.imp.hack.render.HUDObject;
import dev.tensor.misc.imp.hack.render.RenderOrder;
import dev.tensor.misc.imp.setting.settings.DoubleSetting;
import dev.tensor.misc.imp.setting.settings.EnumSetting;
import dev.tensor.misc.imp.setting.settings.IntegerSetting;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Hoosiers
 * @since 04-18-2026
 */

@Hack.Info(name = "Time", category = Category.HUD)
public final class Time extends Hack implements HUDObject {

    @Override
    public RenderOrder getRenderOrder() {
        return RenderOrder.HUD_Text;
    }

    public final IntegerSetting x = new IntegerSetting("X", 0, 0, this.getMaxX());
    public final IntegerSetting y = new IntegerSetting("Y", 356, 0, this.getMaxY());
    public final DoubleSetting scale = new DoubleSetting("Scale", 1.0, 0.5, 3.0, 1);
    public final EnumSetting direction = new EnumSetting("Direction", Direction.Left);
    public final EnumSetting format = new EnumSetting("Format", Format.Hour12$Min$XX);
    public final EnumSetting separator = new EnumSetting("Separator", Separator.Brackets);
    public final EnumSetting dateColor = new EnumSetting("Date Color", ChatColor.WHITE);
    public final EnumSetting separatorColor = new EnumSetting("Sep Color", ChatColor.YELLOW);

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
        return 65;
    }

    @Override
    public int getWidthScaled() {
        return (int) (this.getWidth() * this.scale.getValue());
    }

    @Override
    public int getHeight() {
        return 12;
    }

    @Override
    public int getHeightScaled() {
        return (int) (this.getHeight() * this.scale.getValue());
    }

    @Override
    public void render() {
        this.prepareScale();

        String leftSeparator = this.getSeparator(true);

        String dateFormat = this.getDateFormat();
        SimpleDateFormat format = new SimpleDateFormat(dateFormat);
        String timeStamp = format.format(new Date());

        String rightSeparator = this.getSeparator(false);

        String value = this.getColor((ChatColor) this.separatorColor.getValue())
                + leftSeparator + this.getColor((ChatColor) this.dateColor.getValue())
                + timeStamp + this.getColor((ChatColor) this.separatorColor.getValue())
                + rightSeparator;

        this.drawString(value, this.getOffSetX(value), 1, Color.WHITE.getRGB());

        this.finishScale();
    }

    private String getDateFormat() {
        switch ((Format) this.format.getValue()) {
            case Hour24$Min:
                return "K:mm";
            case Hour24$Min$Sec:
                return "K:mm:ss";
            case Hour12$Min:
                return "h:mm";
            case Hour12$Min$Sec:
                return "h:mm:ss";
            case Hour12$Min$XX:
                return "h:mm a";
            default:
                return "h:mm:ss a";
        }
    }

    private String getSeparator(boolean left) {
        switch ((Separator) this.separator.getValue()) {
            case Line:
                return "|";
            case Left_Arrow:
                if (left) return ">";
                else return "<";
            case Right_Arrow:
                if (left) return "<";
                else return ">";
            case Brackets:
                if (left) return "[";
                else return "]";
            case Curly_Brackets:
                if (left) return "{";
                else return "}";
            default:
                return "";
        }
    }

    private int getOffSetX(String value) {
        switch ((Direction) this.direction.getValue()) {
            case Left:
                return 1;
            case Right:
                return this.getWidth() - this.getFontRenderer().getStringWidth(value) - 1;
        }

        return 1;
    }

    private enum Direction {
        Left,
        Right
    }

    private enum Format {
        Hour24$Min,
        Hour24$Min$Sec,
        Hour12$Min,
        Hour12$Min$Sec,
        Hour12$Min$XX,
        Hour12$Min$Sec$XX
    }

    private enum Separator {
        Left_Arrow,
        Right_Arrow,
        Line,
        Brackets,
        Curly_Brackets,
        None
    }
}
