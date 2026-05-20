package dev.tensor.feature.hacks;

import dev.tensor.misc.helpers.ChatColor;
import dev.tensor.misc.helpers.account.Account;
import dev.tensor.misc.imp.hack.Category;
import dev.tensor.misc.imp.hack.Hack;
import dev.tensor.misc.imp.setting.settings.GeneralButtonSetting;
import org.jetbrains.annotations.NotNull;

/**
 * @author Hoosiers
 * @since 05-20-2026
 */

@Hack.Info(name = "AltAccounts", category = Category.Client)
public final class AltAccounts extends Hack {

    /**
     * OVERVIEW OF FEATURE:
     * <p>
     * New Setting type ButtonSetting
     * -Enum for type
     * <p>
     * Make custom window for that with account manager kinda like waypoints.
     * - Also make a command for it!
     * <p>
     * Once you change it, you must relog to take effect!
     * <p>
     * Add: Username; Server; Password (must input all three)
     * - Adding repeat values overrides the old ones
     * - Option to delete individual accounts and settings
     * - Option to set account as active
     */

    public final GeneralButtonSetting buttonWindow = new GeneralButtonSetting("Accounts", GeneralButtonSetting.ButtonType.ACCOUNTS) {

        @Override
        public void onTriggerChange() {
            if (this.failsNullCheck()) {
                return;
            }

            AltAccounts altAccounts = this.getHackManager().getHack(AltAccounts.class);

            if (altAccounts != null && altAccounts.isEnabled()) {
                this.sendClientMessageWithHeader(ChatColor.RED.text() + "AltAccounts: Relog to update session!");
            }
        }
    };

    public String getPasswordOverrideForCurrentServer() {
        String password = this.getAccountManager().getPasswordForActiveAccountOnCurrentServer();

        if (password.equalsIgnoreCase(Account.DEFAULT_PASSWORD)) {

            @NotNull
            AutoLogin autoLogin = this.getHackManager().getHack(AutoLogin.class);

            return autoLogin.password.getValue();
        }

        return password;
    }
}
