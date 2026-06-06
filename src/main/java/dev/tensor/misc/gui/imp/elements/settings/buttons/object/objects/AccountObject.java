package dev.tensor.misc.gui.imp.elements.settings.buttons.object.objects;

import dev.tensor.misc.gui.TensorGUI;
import dev.tensor.misc.gui.imp.elements.settings.buttons.AccountListSettingButton;
import dev.tensor.misc.gui.imp.elements.settings.buttons.object.ButtonSettingObject;
import dev.tensor.misc.helpers.ChatColor;
import dev.tensor.misc.imp.account.Account;
import dev.tensor.misc.imp.setting.settings.IntegerSetting;

import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Hoosiers
 * @since 05-23-2026
 */

public final class AccountObject implements ButtonSettingObject {

    public final Account account;
    public final AccountListSettingButton button;
    private final IntegerSetting x;
    private final IntegerSetting y;
    private final IntegerSetting scrollY;
    private final int offsetX;
    private final int offsetY;

    public int number = 0;

    private AccountPasswordObject passwordToRemove = null;

    private boolean hovered = false;
    private boolean hoveredDelete = false;
    private boolean hoveredActive = false;

    private final ArrayList<AccountPasswordObject> passwordObjects = new ArrayList<>();

    public AccountObject(Account account, AccountListSettingButton button, IntegerSetting x, IntegerSetting y, IntegerSetting scrollY, int offsetX, int offsetY, int number) {
        this.account = account;
        this.button = button;
        this.x = x;
        this.y = y;
        this.scrollY = scrollY;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.number = number;

        AtomicInteger offsetPassY = new AtomicInteger(0);

        account.getServerPasswordsMap().forEach((s, s2) -> {
            AccountPasswordObject object = new AccountPasswordObject(this, s, s2, this.x, this.y, this.scrollY, 0, this.button.tensorGUI.titleBoxHeight + 45, offsetPassY.getAndAdd(1));
            this.passwordObjects.add(object);
        });
    }

    @Override
    public int getWidth() {
        return this.button.tensorGUI.guiWidth - (2 * this.offsetX);
    }

    @Override
    public int getHeight() {
        return 17;
    }

    @Override
    public int getPosX() {
        return this.x.getValue() + this.offsetX;
    }

    @Override
    public int getPosY() {
        return this.y.getValue() + this.scrollY.getValue() + this.offsetY + this.button.getAccountObjectOffsetY(this.number);
    }

    @Override
    public boolean hovered() {
        return this.hovered || this.hoveredActive || this.hoveredDelete;
    }

    @Override
    public void setHovered(boolean hovered) {
        if (!hovered) {
            this.hovered = false;
            this.hoveredActive = false;
            this.hoveredDelete = false;
            this.clearHover(null);
        } else {
            this.hovered = true;
        }
    }

    @Override
    public void onHovered(int mouseX, int mouseY) {
        if (this.isHovered(this.getPosX(), this.getPosY(), this.getPosX() + this.getWidth() - 110, this.getPosY() + getHeight() - 2, mouseX, mouseY)) {
            this.button.resetHover(this);
            this.clearHover(null);
            this.hovered = true;

        } else if (this.isHovered(this.getPosX() + this.getWidth() - 108, this.getPosY(), this.getPosX() + this.getWidth() - 55, this.getPosY() + this.getHeight() - 2, mouseX, mouseY)) {
            this.button.resetHover(this);
            this.clearHover(null);
            this.hoveredActive = true;

        } else if (this.isHovered(this.getPosX() + this.getWidth() - 53, this.getPosY(), this.getPosX() + this.getWidth(), this.getPosY() + this.getHeight() - 2, mouseX, mouseY)) {
            this.button.resetHover(this);
            this.clearHover(null);
            this.hoveredDelete = true;

        } else if (this.isHovered(this.getPosX(), this.getPosY() + this.getHeight(), this.getPosX() + this.getWidth(), this.getPosY() + this.getHeight() + this.getPasswordHeight(), mouseX, mouseY)) {
            this.passwordObjects.forEach(accountPasswordObject -> accountPasswordObject.onHovered(mouseX, mouseY));
            this.button.resetHover(this);
        } else {
            this.clearHover(null);
        }
    }

    @Override
    public void render(TensorGUI tensorGUI, Color fillColor, Color selectedFillColor, Color outLineColor) {
        this.drawRectangle(this.getPosX(), this.getPosY(), this.getPosX() + this.getWidth() - 110, this.getPosY() + this.getHeight() - 2, this.hovered ? selectedFillColor.getRGB() : fillColor.getRGB());
        this.drawRectangle(this.getPosX() + this.getWidth() - 110, this.getPosY(), this.getPosX() + this.getWidth() - 108, this.getPosY() + this.getHeight() - 2, outLineColor.getRGB());
        this.drawRectangle(this.getPosX() + this.getWidth() - 55, this.getPosY(), this.getPosX() + this.getWidth() - 53, this.getPosY() + this.getHeight() - 2, outLineColor.getRGB());
        this.drawRectangle(this.getPosX() + this.getWidth() - 53, this.getPosY(), this.getPosX() + this.getWidth(), this.getPosY() + this.getHeight() - 2, this.account.isSessionAccount() ? this.hoveredDelete ? selectedFillColor.getRGB() : fillColor.getRGB() : this.hoveredDelete ? new Color(255, 0, 0, fillColor.getAlpha()).darker().getRGB() : new Color(255, 0, 0, fillColor.getAlpha()).getRGB());
        this.drawRectangle(this.getPosX() + this.getWidth() - 108, this.getPosY(), this.getPosX() + this.getWidth() - 55, this.getPosY() + this.getHeight() - 2, this.getAccountManager().isActiveAccount(this.account) ? this.hoveredActive ? new Color(0, 255, 0, fillColor.getAlpha()).darker().getRGB() : new Color(0, 255, 0, fillColor.getAlpha()).getRGB() : this.hoveredActive ? new Color(255, 255, 0, fillColor.getAlpha()).darker().getRGB() : new Color(255, 255, 0, fillColor.getAlpha()).getRGB());

        this.drawString("> Active <", this.getPosX() + this.getWidth() - 55 - ((this.getFontRenderer().getStringWidth("> Active <") + 53) / 2), this.getPosY() + 3, Color.YELLOW.getRGB());
        this.drawString(this.account.isSessionAccount() ? "> N/A <" : "> Delete <", this.getPosX() + this.getWidth() - ((this.getFontRenderer().getStringWidth(this.account.isSessionAccount() ? "> N/A <" : "> Delete <") + 53) / 2), this.getPosY() + 3, Color.YELLOW.getRGB());
        this.drawString("Account Username : " + ChatColor.LIGHT_GRAY.text() + this.account.getUsername(), this.getPosX() + 3, this.getPosY() + 3, Color.WHITE.getRGB());

        this.passwordObjects.forEach(accountPasswordObject -> accountPasswordObject.render(tensorGUI, fillColor, selectedFillColor, outLineColor));

        int passwordHeight = this.getPasswordHeight();

        this.drawRectangle(this.getPosX(), this.getPosY() + 15 + passwordHeight, this.getPosX() + this.getWidth(), this.getPosY() + 17 + passwordHeight, outLineColor.getRGB());
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        if (this.hoveredActive && !this.getAccountManager().isActiveAccount(this.account)) {
            this.getAccountManager().setActiveAccount(this.account);
            this.button.buttonSetting.onChange();

        } else if (this.hoveredDelete) {
            this.button.queueToRemoveAccount(account);

        } else if (!this.passwordObjects.isEmpty()) {
            AtomicBoolean hasDispatched = new AtomicBoolean(false);

            this.passwordObjects.forEach(accountPasswordObject -> {
                if (!hasDispatched.get() && this.isHovered(accountPasswordObject.getPosX(), accountPasswordObject.getPosY(), accountPasswordObject.getPosX() + accountPasswordObject.getWidth(), accountPasswordObject.getPosY() + accountPasswordObject.getHeight(), mouseX, mouseY)) {
                    hasDispatched.set(true);
                    accountPasswordObject.onClick(mouseX, mouseY);
                }
            });

            if (this.passwordToRemove != null) {
                this.removePassword(this.passwordToRemove);
            }
        }
    }

    public int getPasswordHeight() {
        AtomicInteger height = new AtomicInteger(0);

        this.passwordObjects.forEach(accountPasswordObject -> height.getAndAdd(accountPasswordObject.getHeight()));

        return height.get();
    }

    public void queuePasswordForRemoval(AccountPasswordObject accountPasswordObject) {
        if (this.passwordToRemove != null) {
            this.removePassword(accountPasswordObject);
        }

        this.passwordToRemove = accountPasswordObject;
    }

    private void removePassword(AccountPasswordObject accountPasswordObject) {
        this.account.removePasswordForServer(accountPasswordObject.server);
        this.passwordObjects.remove(accountPasswordObject);
        this.passwordToRemove = null;

        for (int i = 0; i < this.passwordObjects.size(); i++) {

            this.passwordObjects.get(i).number = i;
        }

        if (this.scrollY.getValue() < 0) {

            int value = this.scrollY.getValue() + accountPasswordObject.getHeight();

            if (value < 0) {
                this.scrollY.setValue(value);
            } else {
                this.scrollY.setValue(0);
            }
        }
    }

    public void resetPasswords() {
        this.passwordObjects.clear();

        AtomicInteger offsetPassY = new AtomicInteger(0);

        account.getServerPasswordsMap().forEach((s, s2) -> {
            AccountPasswordObject object = new AccountPasswordObject(this, s, s2, this.x, this.y, this.scrollY, 0, this.button.tensorGUI.titleBoxHeight + 45, offsetPassY.getAndAdd(1));
            this.passwordObjects.add(object);
        });
    }

    public void clearHover(AccountPasswordObject accountPasswordObject) {
        this.hovered = false;
        this.hoveredDelete = false;
        this.hoveredActive = false;

        if (accountPasswordObject == null) {
            this.passwordObjects.forEach(accountPasswordObject1 -> accountPasswordObject1.setHovered(false));
            return;
        }

        this.passwordObjects.forEach(accountPasswordObject1 -> {
            if (accountPasswordObject1 != accountPasswordObject) {
                accountPasswordObject1.setHovered(false);
            }
        });
    }
}
