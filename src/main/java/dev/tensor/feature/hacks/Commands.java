package dev.tensor.feature.hacks;

import dev.tensor.misc.imp.hack.Category;
import dev.tensor.misc.imp.hack.Hack;
import dev.tensor.misc.imp.setting.settings.StringSetting;

/**
 * @author Hoosiers
 * @since 03-18-2026
 */

@Hack.Info(name = "Commands", category = Category.Client, drawn = false, enabled = true)
public final class Commands extends Hack {

    public final StringSetting prefix = new StringSetting("Prefix", "-") {

        @Override
        public void setValue(String VALUE) {
            if (VALUE.length() > 1) {

                this.sendClientMessageWithPrefix(this.getColor(ChatColor.RED) + "Prefix: Failed to set prefix '" + this.getColor(ChatColor.AQUA) + VALUE + this.getColor(ChatColor.RED) + "' (Length > 1)!");
                return;
            }

            super.setValue(VALUE);
        }
    };
}
