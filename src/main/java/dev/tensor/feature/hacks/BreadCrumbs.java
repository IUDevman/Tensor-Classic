package dev.tensor.feature.hacks;

import dev.tensor.backend.events.constant.ChangeWorldEvent;
import dev.tensor.misc.event.EventAction;
import dev.tensor.misc.imp.hack.Category;
import dev.tensor.misc.imp.hack.Hack;
import dev.tensor.misc.imp.hack.render.Render3DHack;
import dev.tensor.misc.imp.hack.render.RenderOrder;
import dev.tensor.misc.imp.setting.settings.ColorSetting;
import dev.tensor.misc.imp.setting.settings.GeneralButtonSetting;
import dev.tensor.misc.imp.setting.settings.IntegerSetting;
import net.minecraft.src.GuiGameOver;
import net.minecraft.src.RenderManager;

import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Hoosiers
 * @since 04-28-2026
 */

@Hack.Info(name = "BreadCrumbs", category = Category.Render)
public final class BreadCrumbs extends Render3DHack {

    @Override
    public RenderOrder getRenderOrder() {
        return RenderOrder.BreadCrumbs;
    }

    public final GeneralButtonSetting clearLogged = new GeneralButtonSetting("Clear Logged Positions", GeneralButtonSetting.ButtonType.CLICK_ACTION) {

        @Override
        public void onChange() {
            BreadCrumbs breadCrumbs = this.getHackManager().getHack(BreadCrumbs.class);

            if (breadCrumbs != null) {
                breadCrumbs.clearPositions();

                if (breadCrumbs.isEnabled() && !this.failsNullCheck()) {
                    this.sendClientMessageWithHeader("BreadCrumbs: Cleared logged positions!");
                }
            }
        }
    };
    public final IntegerSetting maxPos = new IntegerSetting("Max Positions", 1200, 600, 12000);
    public final IntegerSetting lineWidth = new IntegerSetting("Line Width", 2, 1, 5);
    public final ColorSetting color = new ColorSetting("Color", Color.GREEN);

    private final ArrayList<double[]> loggedPositions = new ArrayList<>();

    @Override
    public void onDisable(boolean isNull) {
        this.clearPositions();
    }

    @Override
    public void onClientTick() {
        if (this.getMinecraft().currentScreen instanceof GuiGameOver) {
            this.clearPositions();
            return;
        }

        HephFreecam freecam = this.getHackManager().getHack(HephFreecam.class);

        if (freecam != null && freecam.isEnabled()) {
            return;
        }

        if (this.isPlayerMoving3D()) {
            this.addCurrentPlayerPosition();
        }

        if (!this.loggedPositions.isEmpty()) {

            AtomicBoolean triggerRemove = new AtomicBoolean(false);
            AtomicInteger toRemoveInt = new AtomicInteger(0);

            for (int i = this.loggedPositions.size() - 1; i >= 0; i--) {

                double[] pos = this.loggedPositions.get(i);

                if (pos == null) {
                    continue;
                }

                if (!this.isPositionWithinXZRange(pos[0], pos[2], 256)) {
                    triggerRemove.set(true);
                    toRemoveInt.set(i);
                    break;
                }
            }

            if (triggerRemove.get()) {
                if (toRemoveInt.get() == 0) {
                    this.loggedPositions.remove(0);
                    return;
                }

                ArrayList<double[]> toRemove = new ArrayList<>();

                for (int i = 0; i <= toRemoveInt.get(); i++) {
                    toRemove.add(this.loggedPositions.get(i));
                }

                this.loggedPositions.removeAll(toRemove);
            }
        }
    }

    @Override
    public void onRender3D(float renderPartialTicks) {
        if (this.loggedPositions.isEmpty()) {
            return;
        }

        this.drawBreadCrumbs(this.loggedPositions, RenderManager.renderPosX, RenderManager.renderPosY, RenderManager.renderPosZ, this.lineWidth.getValue(), this.color.getValueRainbow());
    }

    @SuppressWarnings("unused")
    EventAction<ChangeWorldEvent> changeWorldEventAction = this.addEventAction(new EventAction<>(ChangeWorldEvent.class, event -> this.loggedPositions.clear()));

    private boolean isPlayerMoving3D() {
        boolean motionX = true;
        boolean motionY = true;
        boolean motionZ = true;

        if (this.getPlayer().motionX < 0.001D && this.getPlayer().motionX > -0.001D) {
            motionX = false;
        }

        if (this.getPlayer().motionY < 0 && this.getPlayer().motionY > -0.08D) {
            motionY = false;
        }

        if (this.getPlayer().motionZ < 0.001D && this.getPlayer().motionZ > -0.001D) {
            motionZ = false;
        }

        return !(!motionX && !motionY && !motionZ);
    }

    private double[] getCurrentPlayerPosition() {
        return new double[]{
                this.getPlayer().posX,
                this.getPlayer().posY - 1.4D,
                this.getPlayer().posZ
        };
    }

    private void addCurrentPlayerPosition() {
        if (this.getPlayer().ticksExisted < 1) {
            return;
        }

        double[] currentPosition = this.getCurrentPlayerPosition();

        if (this.loggedPositions.size() >= this.maxPos.getValue()) {
            this.loggedPositions.remove(0);
        }

        this.loggedPositions.add(currentPosition);
    }

    public void clearPositions() {
        this.loggedPositions.clear();
    }
}
