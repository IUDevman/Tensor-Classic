package dev.tensor.feature.hacks;

import dev.tensor.misc.helpers.color.RainbowColor;
import dev.tensor.misc.imp.hack.Category;
import dev.tensor.misc.imp.hack.Hack;
import dev.tensor.misc.imp.setting.settings.GeneralButtonSetting;
import dev.tensor.misc.imp.setting.settings.IntegerSetting;

/**
 * @author Hoosiers
 * @since 06-01-2026
 */

@Hack.Info(name = "Rainbow", category = Category.Client, enabled = true, drawn = false, messages = false, toggleable = false)
public final class Rainbow extends Hack {

    public final GeneralButtonSetting reset = new GeneralButtonSetting("Reset Color", GeneralButtonSetting.ButtonType.CLICK_ACTION) {

        @Override
        public void onChange() {
            RainbowColor.reset();
        }
    };
    public final IntegerSetting speed = new IntegerSetting("Speed MS", (int) RainbowColor.DEFAULT_SLEEP_TIME, 1, 50) {

        @Override
        public void setValue(Integer VALUE) {
            super.setValue(VALUE);

            RainbowColor.setSleepTime(VALUE);
        }
    };
    public final IntegerSetting increment = new IntegerSetting("Increment Step", RainbowColor.DEFAULT_INCREMENT_STEP, 1, 12) {

        @Override
        public void setValue(Integer VALUE) {
            super.setValue(VALUE);

            RainbowColor.setIncrementStep(VALUE);
        }
    };

    @Override
    public void onEnable(boolean isNull) {
        RainbowColor.setSleepTimeAndIncrementStep(this.speed.getValue(), this.increment.getValue());
    }
}
