package dev.tensor.feature.hacks;

import dev.tensor.misc.helpers.ChatColor;
import dev.tensor.misc.imp.account.Account;
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

    public final GeneralButtonSetting buttonWindow = new GeneralButtonSetting("Accounts List", GeneralButtonSetting.ButtonType.ACCOUNTS) {

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

    @Override
    public void onDisable(boolean isNull) {
        if (isNull) {
            return;
        }

        if (!this.getAccountManager().isActiveAccount(this.getAccountManager().getSessionAccount())) {
            this.getAccountManager().setActiveAccount(this.getAccountManager().getSessionAccount());
            this.sendClientMessageWithHeader(ChatColor.RED.text() + "AltAccounts: Relog to fully disable hack!");
        }
    }

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
