package dev.tensor.feature.hacks;

import dev.tensor.backend.events.variable.ChatReceivedEvent;
import dev.tensor.misc.event.EventAction;
import dev.tensor.misc.imp.account.Account;
import dev.tensor.misc.imp.hack.Category;
import dev.tensor.misc.imp.hack.Hack;
import dev.tensor.misc.imp.setting.settings.BooleanSetting;
import dev.tensor.misc.imp.setting.settings.EncryptedStringSetting;
import net.minecraft.src.Packet3Chat;

/**
 * @author Hoosiers
 * @since 03-24-2026
 */

@Hack.Info(name = "AutoLogin", category = Category.Misc, description = "Automatically answers server login prompts with your set password. Works with AutoLogin or a set password.")
public final class AutoLogin extends Hack {

    public final EncryptedStringSetting password = new EncryptedStringSetting("Password", "Set your password here. Config is encrypted!", "Set the password for AutoLogin to use.");
    public final BooleanSetting applyAll = new BooleanSetting("Try All Accounts", false, "Enable to send the login message with your set password for all accounts if a password is not defined in AltAccounts.");

    @SuppressWarnings("unused")
    private final EventAction<ChatReceivedEvent> chatReceivedEventAction = this.addHackEventAction(new EventAction<>(ChatReceivedEvent.class, event -> {
        if (!this.shouldAutoLogin()) {
            return;
        }

        String message = event.message;

        if (message.toLowerCase().contains("/login (your password)")) {
            this.sendPacket(new Packet3Chat("/login " + this.getPassword()));
        }
    }));

    private boolean shouldAutoLogin() {
        AltAccounts altAccounts = this.getHackManager().getHack(AltAccounts.class);

        if (altAccounts != null && altAccounts.isEnabled()) {
            String altPassword = altAccounts.getPasswordOverrideForCurrentServer(this);

            boolean altHasPassword = !altPassword.equalsIgnoreCase(this.password.getDefaultValue()) && !altPassword.equalsIgnoreCase(Account.DEFAULT_PASSWORD);

            if (!altHasPassword && this.applyAll.getValue()) {
                return !this.password.getValue().equalsIgnoreCase(this.password.getDefaultValue());
            }

            return altHasPassword;
        }

        return !this.password.getValue().equalsIgnoreCase(this.password.getDefaultValue());
    }

    private String getPassword() {
        AltAccounts altAccounts = this.getHackManager().getHack(AltAccounts.class);

        if (altAccounts != null && altAccounts.isEnabled()) {
            String password = altAccounts.getPasswordOverrideForCurrentServer(this);

            if (password.equalsIgnoreCase(Account.DEFAULT_PASSWORD) && this.applyAll.getValue()) {
                return this.password.getValue();
            }

            return password;
        }

        return this.password.getValue();
    }
}
