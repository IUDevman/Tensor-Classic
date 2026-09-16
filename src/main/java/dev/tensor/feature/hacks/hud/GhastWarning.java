package dev.tensor.feature.hacks.hud;

import dev.tensor.backend.events.constant.ChangeWorldEvent;
import dev.tensor.misc.event.EventAction;
import dev.tensor.misc.imp.hack.Category;
import dev.tensor.misc.imp.hack.Hack;
import dev.tensor.misc.imp.hack.render.HUDObject;
import dev.tensor.misc.imp.hack.render.RenderOrder;
import dev.tensor.misc.imp.setting.settings.ColorNoAlphaSetting;
import dev.tensor.misc.imp.setting.settings.DoubleSetting;
import dev.tensor.misc.imp.setting.settings.IntegerSetting;
import net.minecraft.src.EntityFireball;

import java.awt.*;

@Hack.Info(name = "GhastWarning", category = Category.HUD)
public final class GhastWarning extends Hack implements HUDObject {

    @Override
    public RenderOrder getRenderOrder() {
        return RenderOrder.HUD_Text;
    }

    public final IntegerSetting x = new IntegerSetting("X", 100, 0, this.getMaxX());
    public final IntegerSetting y = new IntegerSetting("Y", 0, 0, this.getMaxY());
    public final DoubleSetting scale = new DoubleSetting("Scale", 1.0, 0.5, 3.0, 1);
    public final ColorNoAlphaSetting color = new ColorNoAlphaSetting("Color", new Color(255, 0, 0, 255));

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
        return this.stringWidth("Ghast fireball incoming!");
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

    private boolean hasFireBall = false;

    @Override
    public void onEnable(boolean isNull) {
        this.hasFireBall = false;
    }

    @Override
    public void onClientTick() {
        if (!this.isNetherNullSafe()) {
            this.hasFireBall = false;
            return;
        }

        this.hasFireBall = this.getFilteredLoadedEntities().stream().filter(entity -> entity instanceof EntityFireball).findFirst().orElse(null) != null;
    }

    @Override
    public void render() {
        if (!this.hasFireBall) {
            return;
        }

        this.prepareScale();

        this.drawString("Ghast fireball incoming!", 1, 1, this.color.getValueRainbow().getRGB());

        this.finishScale();
    }

    @SuppressWarnings("unused")
    EventAction<ChangeWorldEvent> changeWorldEventAction = this.addHackEventAction(new EventAction<>(ChangeWorldEvent.class, event -> this.hasFireBall = false));
}
