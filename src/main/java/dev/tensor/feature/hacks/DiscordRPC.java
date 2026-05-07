package dev.tensor.feature.hacks;

import dev.tensor.feature.managers.DiscordManager;
import dev.tensor.misc.imp.hack.Category;
import dev.tensor.misc.imp.hack.Hack;
import dev.tensor.misc.imp.setting.settings.BooleanSetting;
import dev.tensor.misc.imp.setting.settings.EnumSetting;
import dev.tensor.misc.imp.setting.settings.IntegerSetting;

/**
 * @author Hoosiers
 * @since 05-06-2026
 */

@Hack.Info(name = "DiscordRPC", category = Category.Client)
public final class DiscordRPC extends Hack {

    public final BooleanSetting showServer = new BooleanSetting("Show Server", true) {

        @Override
        public void setValue(Boolean value) {
            super.setValue(value);

            if (!this.failsNullCheck()) {
                DiscordRPC discordRPC = this.getHackManager().getHack(DiscordRPC.class);

                if (discordRPC != null && discordRPC.isEnabled()) {
                    DiscordManager discordManager = this.getDiscordManager();

                    if (discordManager != null) {
                        discordManager.updateServerCheck(value);
                    }
                }
            }
        }
    };
    public final IntegerSetting refreshTimeMS = new IntegerSetting("Refresh MS", 10000, 10000, 50000) {

        @Override
        public void setValue(Integer value) {
            super.setValue(value);

            if (!this.failsNullCheck()) {
                DiscordRPC discordRPC = this.getHackManager().getHack(DiscordRPC.class);

                if (discordRPC != null && discordRPC.isEnabled()) {
                    DiscordManager discordManager = this.getDiscordManager();

                    if (discordManager != null) {
                        discordManager.setUpdateTimeMS(value);
                    }
                }
            }
        }
    };
    public final EnumSetting icon = new EnumSetting("Icon", Icon.Test) {

        @Override
        public void setValue(Enum<?> value) {
            super.setValue(value);

            if (!this.failsNullCheck()) {
                DiscordRPC discordRPC = this.getHackManager().getHack(DiscordRPC.class);

                if (discordRPC != null && discordRPC.isEnabled()) {
                    DiscordManager discordManager = this.getDiscordManager();

                    if (discordManager != null) {
                        discordManager.updateIcon(((Icon) value).value());
                    }
                }
            }
        }
    };

    @Override
    public void onEnable(boolean isNull) {
        this.getDiscordManager().startInstance(this.showServer.getValue(), ((Icon) this.icon.getValue()).value(), this.refreshTimeMS.getValue());
    }

    @Override
    public void onDisable(boolean isNull) {
        this.getDiscordManager().stopInstance();
    }

    private enum Icon {
        Logo("logo_Tensor"),
        Classic("logo"),
        GameSense("logo_gamesense"),
        Lukflug("logo_Lukflug"),
        Test("gear");

        private final String value;

        Icon(String value) {
            this.value = value;
        }

        public String value() {
            return this.value;
        }
    }
}
