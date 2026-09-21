package dev.tensor.misc.gui.imp.description;

import dev.tensor.misc.gui.imp.description.imp.SimpleSettingDescription;
import dev.tensor.misc.imp.setting.Setting;
import dev.tensor.misc.imp.setting.settings.*;

/**
 * @author Hoosiers
 * @since 09-20-2026
 */

public final class SettingDescriptionFinder {

    public static GUIDescription getDescriptionForSetting(Setting<?> setting) {

        if (setting instanceof BooleanSetting || setting instanceof IntegerSetting || setting instanceof DoubleSetting) {
            return new SimpleSettingDescription(setting, false);
        }

        if (setting instanceof BlockListSetting || setting instanceof EncryptedStringSetting || setting instanceof EntityTypeListSetting) {
            return new SimpleSettingDescription(setting, true);
        }

        if (setting instanceof PacketTypeListSetting || setting instanceof SpammerListSetting || setting instanceof StringSetting || setting instanceof WaypointListSetting) {
            return new SimpleSettingDescription(setting, true);
        }

        //todo: custom settings for color and enums

        return null;
    }

    private SettingDescriptionFinder() {

    }
}
