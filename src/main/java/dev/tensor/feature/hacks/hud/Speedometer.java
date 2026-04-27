package dev.tensor.feature.hacks.hud;

import dev.tensor.backend.mixins.accessors.MinecraftAccessor;
import dev.tensor.misc.imp.hack.Category;
import dev.tensor.misc.imp.hack.Hack;
import dev.tensor.misc.imp.hack.render.HUDObject;
import dev.tensor.misc.imp.hack.render.RenderOrder;
import dev.tensor.misc.imp.setting.settings.*;

import java.awt.*;
import java.text.DecimalFormat;
import java.util.ArrayDeque;

/**
 * @author Luna5ama (GameSense)
 * @author Hoosiers
 * @since 03-30-2026
 * <p>
 * https://github.com/IUDevman/gamesense-client/commit/0dd0e740a1e75f21de19f7306895fd51127853ad
 */

@Hack.Info(name = "Speedometer", category = Category.HUD)
public final class Speedometer extends Hack implements HUDObject {

    @Override
    public RenderOrder getRenderOrder() {
        return RenderOrder.HUD_Text;
    }

    public final IntegerSetting x = new IntegerSetting("X", 0, 0, this.getMaxX());
    public final IntegerSetting y = new IntegerSetting("Y", 37, 0, this.getMaxY());
    public final DoubleSetting scale = new DoubleSetting("Scale", 1.0, 0.5, 3.0, 1);
    public final EnumSetting direction = new EnumSetting("Direction", Direction.Left);
    public final EnumSetting unit = new EnumSetting("Units", Units.BPS) {
        @Override
        public void setValue(Enum<?> value) {
            super.setValue(value);

            if (!this.failsNullCheck()) {
                Speedometer speedometer = this.getHackManager().getHack(Speedometer.class);

                if (speedometer.isEnabled()) {
                    speedometer.resetSpeed();
                }
            }
        }
    };
    public final BooleanSetting average = new BooleanSetting("Average Speed", true);
    public final IntegerSetting collectionTicks = new IntegerSetting("Collection Ticks", 20, 5, 100);
    public final ColorSetting color = new ColorSetting("Color", new Color(255, 255, 0, 255));

    private final DecimalFormat coordinateFormat = new DecimalFormat("#0.00");

    private final ArrayDeque<Double> speedDeque = new ArrayDeque<>();
    private double currentSpeed = 0;

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
    public void onEnable(boolean isNull) {
        this.resetSpeed();
    }

    @Override
    public void onDisable(boolean isNull) {
        this.resetSpeed();
    }

    @Override
    public void onClientTick() {
        double speed = this.getCurrentSpeed();

        if (this.average.getValue()) {

            if (speed > 0 || this.getPlayer().ticksExisted % 4 == 0) {
                this.speedDeque.add(speed);
            } else {
                this.speedDeque.pollFirst();
            }

            while (!this.speedDeque.isEmpty() && this.speedDeque.size() > this.collectionTicks.getValue()) {
                this.speedDeque.poll();
            }

            this.currentSpeed = this.getAverage();

        } else {
            this.currentSpeed = speed;
        }
    }

    @Override
    public void render() {
        this.prepareScale();

        String value = "Speed: " + this.coordinateFormat.format(this.currentSpeed) + " " + this.getUnits();
        this.drawString(value, this.getOffSetX(value), 1, this.color.getValue().getRGB());

        this.finishScale();
    }

    private double getCurrentSpeed() {
        double dX = this.getPlayer().posX - this.getPlayer().prevPosX;
        double dZ = this.getPlayer().posZ - this.getPlayer().prevPosZ;

        double speed = Math.hypot(dX, dZ) * ((MinecraftAccessor) this.getMinecraft()).tensor_getTimer().ticksPerSecond;

        switch ((Units) this.unit.getValue()) {
            case MPS:
            case BPS:
                return speed;
            case KPH:
                return speed * 3.6;
            case MPH:
                return speed * 2.237;
        }

        return 0;
    }

    private double getAverage() {
        if (this.speedDeque.isEmpty()) {
            return 0;
        }

        double sum = 0;
        int size = 0;

        for (double element : this.speedDeque) {
            sum += element;
            size++;
        }

        return sum / size;
    }

    private String getUnits() {
        switch ((Units) this.unit.getValue()) {
            case BPS:
                return "b/s";
            case MPS:
                return "m/s";
            case KPH:
                return "km/h";
            case MPH:
                return "mph";
        }

        return "";
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

    public void resetSpeed() {
        this.currentSpeed = 0;
        this.speedDeque.clear();
    }

    private enum Direction {
        Left,
        Right
    }

    private enum Units {
        BPS,
        MPS,
        KPH,
        MPH
    }
}
