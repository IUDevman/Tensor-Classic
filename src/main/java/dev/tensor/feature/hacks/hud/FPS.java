package dev.tensor.feature.hacks.hud;

import dev.tensor.misc.imp.hack.Category;
import dev.tensor.misc.imp.hack.Hack;
import dev.tensor.misc.imp.hack.render.HUDObject;
import dev.tensor.misc.imp.hack.render.RenderOrder;
import dev.tensor.misc.imp.setting.settings.*;

import java.awt.*;
import java.util.ArrayDeque;

/**
 * @author Hoosiers
 * @since 04-14-2026
 */

@Hack.Info(name = "FPS", category = Category.HUD, description = "Displays the client frames per second (FPS).")
public final class FPS extends Hack implements HUDObject {

    @Override
    public RenderOrder getRenderOrder() {
        return RenderOrder.HUD_Text;
    }

    public final IntegerSetting x = new IntegerSetting("X", 0, 0, this.getMaxX());
    public final IntegerSetting y = new IntegerSetting("Y", 50, 0, this.getMaxY());
    public final DoubleSetting scale = new DoubleSetting("Scale", 1.0, 0.5, 3.0, 1);
    public final EnumSetting direction = new EnumSetting("Direction", Direction.Left);
    public final BooleanSetting average = new BooleanSetting("Average FPS", true, "Enable to render the average FPS instead of the current.") {

        @Override
        public void setValue(Boolean value) {
            super.setValue(value);

            if (!this.failsNullCheck()) {
                FPS fps = this.getHackManager().getHack(FPS.class);

                if (fps != null && fps.isEnabled()) {
                    fps.resetFPS();
                }
            }
        }
    };
    public final IntegerSetting collectionTicks = new IntegerSetting("Collection Ticks", 200, 20, 500) {

        @Override
        public void setValue(Integer value) {
            super.setValue(value);

            if (!this.failsNullCheck()) {
                FPS fps = this.getHackManager().getHack(FPS.class);

                if (fps != null && fps.isEnabled() && fps.average.getValue()) {
                    fps.resetFPS();
                }
            }
        }
    };
    public final ColorSetting color = new ColorSetting("Color", new Color(255, 255, 0, 255));

    private final ArrayDeque<Integer> fpsDeque = new ArrayDeque<>();
    private int currentFPS = 0;
    private int currentAverageFPS = 0;

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
    public void onClientTick() {
        int fps = this.getFPS();

        if (this.average.getValue()) {
            this.fpsDeque.add(fps);

            while (!this.fpsDeque.isEmpty() && this.fpsDeque.size() > this.collectionTicks.getValue()) {
                this.fpsDeque.poll();
            }

            this.currentAverageFPS = this.getAverage();
        }

        this.currentFPS = fps;
    }

    @Override
    public void render() {
        this.prepareScale();

        String value = "FPS: " + this.currentFPS + (this.average.getValue() ? " (AVG: " + this.currentAverageFPS + ")" : "");
        this.drawString(value, this.getOffSetX(value), 1, this.color.getValueRainbow().getRGB());

        this.finishScale();
    }

    private int getFPS() {
        int fps = 0;

        String fpsValue = this.getMinecraft().debug.split(" fps")[0];

        try {
            fps = Integer.parseInt(fpsValue);

        } catch (Exception ignored) {
        }

        return fps;
    }

    private int getAverage() {
        if (this.fpsDeque.isEmpty()) {
            return this.currentFPS;
        }

        int sum = 0;
        int size = 0;

        for (int element : this.fpsDeque) {
            sum += element;
            size++;
        }

        return sum / size;
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

    public void resetFPS() {
        this.currentFPS = 0;
        this.currentAverageFPS = 0;
        this.fpsDeque.clear();
    }

    private enum Direction {
        Left,
        Right
    }
}
