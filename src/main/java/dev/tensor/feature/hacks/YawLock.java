package dev.tensor.feature.hacks;

import dev.tensor.misc.imp.hack.Category;
import dev.tensor.misc.imp.hack.Hack;
import dev.tensor.misc.imp.setting.settings.BooleanSetting;
import dev.tensor.misc.imp.setting.settings.EnumSetting;
import dev.tensor.misc.imp.setting.settings.IntegerSetting;

/**
 * @author Hoosiers
 * @since 04-30-2026
 */

@Hack.Info(name = "YawLock", category = Category.Movement, description = "Sets your rotation to a defined position. Useful in combination with AutoWalk.")
public final class YawLock extends Hack {

    public final EnumSetting mode = new EnumSetting("Mode", Mode.Cardinal_45, new String[]{
            "This sets the direction your rotation will be locked to.",
            "Input® The angle defined in Input Value.",
            "Facing® The angle you are currently looking.",
            "Cardinal 45® The closest 45 degree angle.",
            "Cardinal 90® The closest 90 degree angle."
    });
    public final IntegerSetting value = new IntegerSetting("Input Value", 45, 0, 360);
    public final BooleanSetting freecam = new BooleanSetting("Freecam", false, "Enable to apply yaw lock when in freecam.");

    private boolean needsYaw = false;
    private float toYaw = 0;

    @Override
    public void onEnable(boolean isNull) {
        if (isNull) {
            this.needsYaw = true;
            return;
        }

        this.toYaw = this.getPlayerYaw();
    }

    @Override
    public void onDisable(boolean isNull) {
        this.needsYaw = false;
        this.toYaw = 0;
    }

    @Override
    public void onClientTick() {
        if (!this.freecam.getValue()) {
            HephFreecam freecam1 = this.getHackManager().getHack(HephFreecam.class);

            if (freecam1 != null && freecam1.isEnabled()) {
                return;
            }
        }

        if (this.needsYaw) {
            this.toYaw = this.getPlayerYaw();
            this.needsYaw = false;
        }

        float yaw = 0;

        switch ((Mode) this.mode.getValue()) {
            case Input:
                yaw = this.value.getValue();
                break;
            case Facing:
                yaw = this.toYaw;
                break;
            case Cardinal_45:
                yaw = this.cardinalPlayerYaw(45);
                break;
            case Cardinal_90:
                yaw = this.cardinalPlayerYaw(90);
                break;
        }

        if (this.getPlayer().rotationYaw != yaw) {
            this.getPlayer().rotationYaw = yaw;
        }
    }

    private float getPlayerYaw() {
        float yaw = this.getPlayer().rotationYaw;

        if (!this.freecam.getValue()) {
            HephFreecam freecam1 = this.getHackManager().getHack(HephFreecam.class);

            if (freecam1 != null && freecam1.isEnabled()) {
                yaw = freecam1.oldYaw;
            }
        }

        while (yaw <= 0) {
            yaw += 360;
        }

        while (yaw > 360) {
            yaw -= 360;
        }

        return yaw;
    }

    private float cardinalPlayerYaw(int degrees) {
        float currentYaw = this.getPlayerYaw();
        int count = Math.round(currentYaw / degrees);

        return count * degrees;
    }

    private enum Mode {
        Input,
        Facing,
        Cardinal_45,
        Cardinal_90
    }
}
