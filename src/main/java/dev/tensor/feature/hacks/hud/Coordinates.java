package dev.tensor.feature.hacks.hud;

import dev.tensor.misc.imp.hack.Category;
import dev.tensor.misc.imp.hack.Hack;
import dev.tensor.misc.imp.hack.render.HUDObject;
import dev.tensor.misc.imp.hack.render.RenderOrder;
import dev.tensor.misc.imp.setting.settings.*;
import net.minecraft.src.Vec3D;

import java.awt.*;
import java.text.DecimalFormat;

/**
 * @author Hoosiers
 * @since 03-30-2026
 */

@Hack.Info(name = "Coordinates", category = Category.HUD, description = "Displays current coordinates and direction.")
public final class Coordinates extends Hack implements HUDObject {

    @Override
    public RenderOrder getRenderOrder() {
        return RenderOrder.HUD_Text;
    }

    public final IntegerSetting x = new IntegerSetting("X", 0, 0, this.getMaxX());
    public final IntegerSetting y = new IntegerSetting("Y", 13, 0, this.getMaxY());
    public final DoubleSetting scale = new DoubleSetting("Scale", 1.0, 0.5, 3.0, 1);
    public final BooleanSetting compass = new BooleanSetting("Compass", true, "Enable to draw a compass on the radar.");
    public final EnumSetting direction = new EnumSetting("Direction", Direction.Left);
    public final EnumSetting type = new EnumSetting("Type", Type.Both);
    public final ColorNoAlphaSetting overworldColor = new ColorNoAlphaSetting("Overworld Color", new Color(255, 255, 255, 255));
    public final ColorNoAlphaSetting netherColor = new ColorNoAlphaSetting("Nether Color", new Color(255, 0, 0, 255));

    private final String coordinatesString = "({X}, {Y}, {Z})";
    private final String coordinatesStringWithDirection = this.coordinatesString + "{D}";
    private final DecimalFormat coordinateFormat = new DecimalFormat("#.0");

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
        return 210;
    }

    @Override
    public int getHeight() {
        return 23;
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
        Vec3D vec3D = this.getPlayerCoordsOverworld();

        double posX = vec3D.xCoord;
        double posY = vec3D.yCoord;
        double posZ = vec3D.zCoord;

        this.prepareScale();

        switch ((Type) this.type.getValue()) {
            case Overworld:
                String coordinatesOverworld = this.coordinatesStringWithDirection.replace("{X}", coordinateFormat.format(posX)).replace("{Y}", coordinateFormat.format(posY)).replace("{Z}", coordinateFormat.format(posZ));
                coordinatesOverworld = coordinatesOverworld.replace("{D}", this.compass.getValue() ? " [" + this.getDirection() + "]" : "");

                this.renderCoordinate(coordinatesOverworld, 1, this.overworldColor.getValueRainbow());
                break;
            case Nether:
                String coordinatesNether = this.coordinatesStringWithDirection.replace("{X}", coordinateFormat.format(posX / 8)).replace("{Y}", coordinateFormat.format(posY)).replace("{Z}", coordinateFormat.format(posZ / 8));
                coordinatesNether = coordinatesNether.replace("{D}", this.compass.getValue() ? " [" + this.getDirection() + "]" : "");

                this.renderCoordinate(coordinatesNether, 1, this.netherColor.getValueRainbow());
                break;
            case Both:
                if (this.isNetherNullSafe()) {
                    String coordinatesNether1 = this.coordinatesStringWithDirection.replace("{X}", coordinateFormat.format(posX / 8)).replace("{Y}", coordinateFormat.format(posY)).replace("{Z}", coordinateFormat.format(posZ / 8));
                    coordinatesNether1 = coordinatesNether1.replace("{D}", this.compass.getValue() ? " [" + this.getDirection() + "]" : "");
                    this.renderCoordinate(coordinatesNether1, 1, this.netherColor.getValueRainbow());

                    String coordinatesOverworld1 = this.coordinatesString.replace("{X}", coordinateFormat.format(posX)).replace("{Y}", coordinateFormat.format(posY)).replace("{Z}", coordinateFormat.format(posZ));
                    this.renderCoordinate(coordinatesOverworld1, 12, this.overworldColor.getValueRainbow());

                } else {
                    String coordinatesOverworld1 = this.coordinatesStringWithDirection.replace("{X}", coordinateFormat.format(posX)).replace("{Y}", coordinateFormat.format(posY)).replace("{Z}", coordinateFormat.format(posZ));
                    coordinatesOverworld1 = coordinatesOverworld1.replace("{D}", this.compass.getValue() ? " [" + this.getDirection() + "]" : "");
                    this.renderCoordinate(coordinatesOverworld1, 1, this.overworldColor.getValueRainbow());

                    String coordinatesNether1 = this.coordinatesString.replace("{X}", coordinateFormat.format(posX / 8)).replace("{Y}", coordinateFormat.format(posY)).replace("{Z}", coordinateFormat.format(posZ / 8));
                    this.renderCoordinate(coordinatesNether1, 12, this.netherColor.getValueRainbow());
                    break;
                }
        }

        this.finishScale();
    }

    private Vec3D getPlayerCoordsOverworld() {

        double posX = this.getPlayer().posX;
        double posY = this.getPlayer().posY;
        double posZ = this.getPlayer().posZ;

        if (this.getWorld().worldProvider.isNether) {
            posX *= 8;
            posZ *= 8;
        }

        return Vec3D.createVectorHelper(posX, posY, posZ);
    }

    private String getDirection() {

        float yaw = this.getPlayer().rotationYaw;

        while (yaw <= 0) {
            yaw += 360;
        }

        while (yaw > 360) {
            yaw -= 360;
        }

        if (yaw >= 45 && yaw < 135) {
            return "West";
        } else if (yaw >= 135 && yaw < 225) {
            return "North";
        } else if (yaw >= 225 && yaw < 315) {
            return "East";
        } else {
            return "South";
        }
    }

    private void renderCoordinate(String coordinateString, int offY, Color color) {
        int offX = this.getOffSetX(coordinateString);

        this.drawString(coordinateString, offX, offY, color.getRGB());
    }

    private int getOffSetX(String coordinatesString) {

        switch ((Direction) this.direction.getValue()) {
            case Left:
                return 1;
            case Right:
                return this.getWidth() - 1 - this.getFontRenderer().getStringWidth(coordinatesString);
            case Center:
                return (this.getWidth() / 2) - (this.getFontRenderer().getStringWidth(coordinatesString) / 2);
        }

        return 0;
    }

    private enum Direction {
        Left,
        Right,
        Center
    }

    private enum Type {
        Overworld,
        Nether,
        Both
    }
}
