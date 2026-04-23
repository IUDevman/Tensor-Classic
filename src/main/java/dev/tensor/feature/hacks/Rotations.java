package dev.tensor.feature.hacks;

import dev.tensor.feature.managers.RotationManager;
import dev.tensor.misc.imp.hack.Category;
import dev.tensor.misc.imp.hack.Hack;
import dev.tensor.misc.imp.setting.settings.EnumSetting;

/**
 * @author Hoosiers
 * @since 04-22-2026
 */

@Hack.Info(name = "Rotations", category = Category.Client, enabled = true)
public final class Rotations extends Hack {

    public final EnumSetting mode = new EnumSetting("Mode", Mode.Intercept) {

        @Override
        public void setValue(Enum<?> value) {
            super.setValue(value);

            Rotations rotations = this.getHackManager().getHack(Rotations.class);

            if (rotations != null && rotations.isEnabled()) {
                rotations.updateRotation((Mode) value);
            }
        }
    };

    @Override
    public void onEnable(boolean isNull) {
        this.updateRotation((Mode) this.mode.getValue());
    }

    @Override
    public void onDisable(boolean isNull) {
        this.updateRotation(null);
    }

    public void updateRotation(Mode mode) {
        if (mode == null) {
            this.getRotationManager().setRotationMode(RotationManager.Mode.None);
            return;
        }

        switch (mode) {
            case Packet:
                this.getRotationManager().setRotationMode(RotationManager.Mode.Packet);
                break;
            case Intercept:
                this.getRotationManager().setRotationMode(RotationManager.Mode.Intercept);
                break;
        }
    }

    public enum Mode {
        Packet,
        Intercept
    }
}
