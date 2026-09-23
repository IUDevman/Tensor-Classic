package dev.tensor.misc.gui.imp.description;

import dev.tensor.misc.gui.imp.description.imp.EnumSettingDescription;
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
            return new SimpleSettingDescription(setting, SimpleSettingDescription.DescriptionModifier.NONE);
        }

        if (setting instanceof BlockListSetting || setting instanceof EntityTypeListSetting || setting instanceof PacketTypeListSetting || setting instanceof SpammerListSetting || setting instanceof StringSetting || setting instanceof WaypointListSetting) {
            return new SimpleSettingDescription(setting, SimpleSettingDescription.DescriptionModifier.OPEN_PANEL);
        }

        if (setting instanceof GeneralButtonSetting) {
            return new SimpleSettingDescription(setting, ((GeneralButtonSetting) setting).getValue().equals(GeneralButtonSetting.ButtonType.ACCOUNTS) ? SimpleSettingDescription.DescriptionModifier.OPEN_PANEL : SimpleSettingDescription.DescriptionModifier.CLICK_ACTION);
        }

        if (setting instanceof EnumSetting) {
            return new EnumSettingDescription((EnumSetting) setting);
        }

        //todo: custom settings for color and enums

        return null;
    }

    private SettingDescriptionFinder() {

    }
}
