package dev.tensor.misc.gui.imp.elements.settings.buttons;

import dev.tensor.feature.hacks.ClickGUI;
import dev.tensor.misc.gui.TensorGUI;
import dev.tensor.misc.gui.imp.elements.ButtonSetting;
import dev.tensor.misc.gui.imp.elements.HackElement;
import dev.tensor.misc.gui.imp.elements.settings.buttons.object.objects.AccountObject;
import dev.tensor.misc.helpers.text.ChatColor;
import dev.tensor.misc.imp.account.Account;
import dev.tensor.misc.imp.setting.settings.GeneralButtonSetting;
import dev.tensor.misc.imp.setting.settings.IntegerSetting;
import net.minecraft.src.ChatAllowedCharacters;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Hoosiers
 * @since 05-23-2026
 */

public final class AccountListSettingButton implements ButtonSetting {

    public final TensorGUI tensorGUI;
    private final ClickGUI clickGUI;
    private final HackElement hackElement;
    public final GeneralButtonSetting buttonSetting;

    private boolean searchingUsername = false;
    private boolean searchingPassword = false;

    private String usernameValue = "";
    private String passwordValue = "";

    private String prevUsername = "prev";
    private String prevPassword = "prev";

    private boolean active;

    private boolean hoveredOnBack = false;
    private boolean hoveredOnAdd = false;
    private boolean hoveredOnMainBox = false;

    private Account accountToDelete = null;

    private final IntegerSetting scrollY = new IntegerSetting("AccountList Scroll Y", 0, -1000, 1000);

    private final ArrayList<AccountObject> accountObjects = new ArrayList<>();

    public AccountListSettingButton(TensorGUI tensorGUI, ClickGUI clickGUI, HackElement hackElement, GeneralButtonSetting buttonSetting) {
        this.tensorGUI = tensorGUI;
        this.clickGUI = clickGUI;
        this.hackElement = hackElement;
        this.buttonSetting = buttonSetting;

        AtomicInteger offsetY = new AtomicInteger(0);

        this.getAccountManager().getAccounts().forEach(account -> {

            AccountObject object = new AccountObject(account, this, this.clickGUI.x, this.clickGUI.y, this.scrollY, 0, this.tensorGUI.titleBoxHeight + 45, offsetY.getAndAdd(1));
            this.accountObjects.add(object);
        });
    }

    @Override
    public int getWidth() {
        return this.tensorGUI.guiWidth;
    }

    @Override
    public int getHeight() {
        return this.tensorGUI.guiHeight.get();
    }

    @Override
    public int getPosX() {
        return this.clickGUI.x.getValue();
    }

    @Override
    public int getPosY() {
        return this.clickGUI.y.getValue();
    }

    @Override
    public void render(TensorGUI tensorGUI, Color fillColor, Color selectedFillColor, Color outLineColor) {
        if (this.getAccountManager().hasUpdated()) {
            this.resetAccountObjects();
            this.getAccountManager().setUpdated(false);
        }

        this.startFlatGL(false);
        this.prepRectangle(fillColor.getRGB());

        this.genRectangleVertices(this.getPosX(), this.getPosY(), this.getPosX() + this.getWidth() - 102, this.getPosY() + this.tensorGUI.titleBoxHeight);

        this.swapRectangleColor(outLineColor.getRGB());

        this.genRectangleVertices(this.getPosX() - 2, this.getPosY() - 2, this.getPosX(), this.getPosY() + this.tensorGUI.titleBoxHeight + 2);
        this.genRectangleVertices(this.getPosX() + this.getWidth(), this.getPosY() - 2, this.getPosX() + this.getWidth() + 2, this.getPosY() + this.tensorGUI.titleBoxHeight + 2);
        this.genRectangleVertices(this.getPosX(), this.getPosY() - 2, this.getPosX() + this.getWidth(), this.getPosY());
        this.genRectangleVertices(this.getPosX(), this.getPosY() + this.tensorGUI.titleBoxHeight, this.getPosX() + this.getWidth(), this.getPosY() + this.tensorGUI.titleBoxHeight + 2);
        this.genRectangleVertices(this.getPosX() + this.getWidth() - 102, this.getPosY(), this.getPosX() + this.getWidth() - 100, this.getPosY() + this.tensorGUI.titleBoxHeight);
        this.genRectangleVertices(this.getPosX() - 2, this.getPosY() + this.tensorGUI.titleBoxHeight + 2, this.getPosX(), this.getPosY() + this.getHeight() + 2);
        this.genRectangleVertices(this.getPosX() + this.getWidth(), this.getPosY() + this.tensorGUI.titleBoxHeight + 2, this.getPosX() + this.getWidth() + 2, this.getPosY() + this.getHeight() + 2);
        this.genRectangleVertices(this.getPosX(), this.getPosY() + this.getHeight(), this.getPosX() + this.getWidth(), this.getPosY() + this.getHeight() + 2);
        this.genRectangleVertices(this.getPosX(), this.getPosY() + this.tensorGUI.titleBoxHeight + 43, this.getPosX() + this.getWidth(), this.getPosY() + this.tensorGUI.titleBoxHeight + 45);
        this.genRectangleVertices(this.getPosX() + 55, this.getPosY() + this.tensorGUI.titleBoxHeight + 2, this.getPosX() + 57, this.getPosY() + this.tensorGUI.titleBoxHeight + 43);

        this.swapRectangleColor(this.hoveredOnBack ? selectedFillColor.getRGB() : fillColor.getRGB());
        this.genRectangleVertices(this.getPosX() + this.getWidth() - 100, this.getPosY(), this.getPosX() + this.getWidth(), this.getPosY() + this.tensorGUI.titleBoxHeight);

        this.swapRectangleColor(this.hoveredOnMainBox ? selectedFillColor.getRGB() : fillColor.getRGB());
        this.genRectangleVertices(this.getPosX() + 57, this.getPosY() + this.tensorGUI.titleBoxHeight + 2, this.getPosX() + this.getWidth(), this.getPosY() + this.tensorGUI.titleBoxHeight + 43);

        this.swapRectangleColor(this.hoveredOnAdd ? new Color(0, 255, 0, fillColor.getAlpha()).darker().getRGB() : new Color(0, 255, 0, fillColor.getAlpha()).getRGB());
        this.genRectangleVertices(this.getPosX(), this.getPosY() + this.tensorGUI.titleBoxHeight + 2, this.getPosX() + 55, this.getPosY() + this.tensorGUI.titleBoxHeight + 43);

        this.finishRectangle();
        this.finishFlatGL(false);

        this.drawCenteredString(this.hackElement.hack.NAME + " : " + this.buttonSetting.getName(), this.getPosX() + ((this.getWidth() - 102) / 2), this.getPosY() + 2 + ((this.tensorGUI.titleBoxHeight - 12) / 2), Color.WHITE.getRGB());
        this.drawCenteredString(">> Go Back <<", this.getPosX() + this.getWidth() - (100 / 2), this.getPosY() + 2 + ((this.tensorGUI.titleBoxHeight - 12) / 2), Color.YELLOW.getRGB());
        this.drawCenteredString(">> Add <<", this.getPosX() + 50 - (this.getFontRenderer().getStringWidth(">> Add <<") / 2), this.getPosY() + this.tensorGUI.titleBoxHeight + 18, Color.YELLOW.getRGB());
        this.drawString("Username : " + ChatColor.LIGHT_GRAY.text() + (this.searchingUsername ? this.usernameValue.isEmpty() ? "..." : this.usernameValue : this.usernameValue.isEmpty() ? "New Account" : this.usernameValue), this.getPosX() + 60, this.getPosY() + this.tensorGUI.titleBoxHeight + 5, Color.WHITE.getRGB());
        this.drawString("Server : " + ChatColor.LIGHT_GRAY.text() + this.getWaypointServerName(), this.getPosX() + 60, this.getPosY() + this.tensorGUI.titleBoxHeight + 17, Color.WHITE.getRGB());
        this.drawString("Password : " + ChatColor.LIGHT_GRAY.text() + (this.searchingPassword ? this.passwordValue.isEmpty() ? "..." : this.passwordValue : this.passwordValue.isEmpty() ? Account.DEFAULT_PASSWORD : this.passwordValue), this.getPosX() + 60, this.getPosY() + this.tensorGUI.titleBoxHeight + 29, Color.WHITE.getRGB());

        GL11.glEnable(GL11.GL_SCISSOR_TEST);

        int posX = this.getPosX();
        int posY = this.getPosY() + this.tensorGUI.titleBoxHeight + 45;
        int width1 = this.getWidth();
        int height1 = this.getHeight() - this.tensorGUI.titleBoxHeight - 45;

        double scaleX = (double) this.getMinecraft().displayWidth / (double) this.getMinecraft().currentScreen.width;
        double scaleY = (double) this.getMinecraft().displayHeight / (double) this.getMinecraft().currentScreen.height;

        GL11.glScissor((int) Math.ceil(posX * scaleX), this.getMinecraft().displayHeight - (int) Math.ceil((posY + height1) * scaleY), (int) Math.ceil(width1 * scaleX), (int) Math.ceil(height1 * scaleY));

        this.accountObjects.forEach(accountObject -> accountObject.render(tensorGUI, fillColor, selectedFillColor, outLineColor));

        int accountHeight = this.getAccountListHeight();

        if (accountHeight < this.getHeight() - -this.tensorGUI.titleBoxHeight - 45) {
            this.drawRectangle(this.getPosX(), this.getPosY() + this.tensorGUI.titleBoxHeight + 45 + accountHeight, this.getPosX() + this.getWidth(), this.getPosY() + this.getHeight(), fillColor.getRGB());
        }

        GL11.glDisable(GL11.GL_SCISSOR_TEST);
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        if (this.isHovered(this.getPosX() + this.getWidth() - 100, this.getPosY(), this.getPosX() + this.getWidth(), this.getPosY() + this.tensorGUI.titleBoxHeight, mouseX, mouseY)) {
            this.close();
        }

        if (this.isHovered(this.getPosX(), this.getPosY() + this.tensorGUI.titleBoxHeight + 2, this.getPosX() + this.getWidth(), this.getPosY() + this.tensorGUI.titleBoxHeight + 43, mouseX, mouseY)) {
            if (this.isHovered(this.getPosX(), this.getPosY() + this.tensorGUI.titleBoxHeight + 2, this.getPosX() + 58, this.getPosY() + this.tensorGUI.titleBoxHeight + 43, mouseX, mouseY)) {
                this.addNewAccount();
            }

            if (this.isHovered(this.getPosX() + 60, this.getPosY() + this.tensorGUI.titleBoxHeight + 4, this.getPosX() + 60 + this.getFontRenderer().getStringWidth("Username : " + (this.searchingUsername ? this.usernameValue.isEmpty() ? "..." : this.usernameValue : this.usernameValue.isEmpty() ? "New Account" : this.usernameValue)), this.getPosY() + this.tensorGUI.titleBoxHeight + 12, mouseX, mouseY)) {
                this.searchingUsername = !this.searchingUsername;
                this.searchingPassword = false;
                this.setBackSearchValues();
            } else if (this.isHovered(this.getPosX() + 60, this.getPosY() + this.tensorGUI.titleBoxHeight + 28, this.getPosX() + 60 + this.getFontRenderer().getStringWidth("Password : " + (this.searchingPassword ? this.passwordValue.isEmpty() ? "..." : this.passwordValue : this.passwordValue.isEmpty() ? Account.DEFAULT_PASSWORD : this.passwordValue)), this.getPosY() + this.tensorGUI.titleBoxHeight + 36, mouseX, mouseY)) {
                this.searchingUsername = false;
                this.searchingPassword = !this.searchingPassword;
                this.setBackSearchValues();

            } else {
                this.clearSearching();
                this.setBackSearchValues();
            }

        } else if (this.isHovered(this.getPosX(), this.getPosY() + this.tensorGUI.titleBoxHeight + 45, this.getPosX() + this.getWidth(), this.getPosY() + this.getHeight(), mouseX, mouseY)) {

            AtomicBoolean hasDispatched = new AtomicBoolean(false);

            this.accountObjects.forEach(accountObject -> {

                if (!hasDispatched.get() && this.isHovered(accountObject.getPosX(), accountObject.getPosY(), accountObject.getPosX() + accountObject.getWidth(), accountObject.getPosY() + accountObject.getHeight() + accountObject.getPasswordHeight(), mouseX, mouseY)) {
                    hasDispatched.set(true);
                    accountObject.onClick(mouseX, mouseY);
                }
            });

            if (this.accountToDelete != null) {
                this.removeAccount(this.accountToDelete);
            }

            this.clearSearching();

        } else {
            this.clearSearching();
        }
    }

    @Override
    public boolean isSearching() {
        if (this.searchingUsername) {
            return true;
        } else return this.searchingPassword;
    }

    @Override
    public void setSearching(boolean searching) {
        if (!searching) {
            this.clearSearching();
        }
    }

    public void clearSearching() {
        this.searchingUsername = false;
        this.searchingPassword = false;
    }

    @Override
    public void onKeyPressed(char letter, int key) {
        if (key == Keyboard.KEY_DELETE) {

            if (this.searchingUsername) {
                this.usernameValue = "";
            } else if (this.searchingPassword) {
                this.passwordValue = "";
            }

            this.setSearching(false);
            return;
        }

        if (key == Keyboard.KEY_RETURN) {
            this.setSearching(false);
            return;
        }

        if (key == Keyboard.KEY_BACK) {

            if (this.searchingUsername && !this.usernameValue.isEmpty()) {
                this.usernameValue = this.usernameValue.substring(0, this.usernameValue.length() - 1);
            } else if (this.searchingPassword && !this.passwordValue.isEmpty()) {
                this.passwordValue = this.passwordValue.substring(0, this.passwordValue.length() - 1);
            }

            return;
        }

        if (this.searchingUsername) {

            if (Arrays.stream(this.badKeys()).noneMatch(value -> value == key) && ChatAllowedCharacters.allowedCharacters.contains(String.valueOf(letter))) {
                String newValue = this.usernameValue + letter;

                if (newValue.length() <= Account.MAX_USERNAME_LENGTH) {
                    this.usernameValue = newValue;
                }
            }

            return;
        }

        if (this.searchingPassword) {

            if (Arrays.stream(this.badKeys()).noneMatch(value -> value == key) && ChatAllowedCharacters.allowedCharacters.contains(String.valueOf(letter))) {
                String newValue = this.passwordValue + letter;

                if (newValue.length() <= Account.MAX_PASSWORD_LENGTH) {
                    this.passwordValue = newValue;
                }
            }
        }
    }

    @Override
    public boolean isActive() {
        return this.active;
    }

    @Override
    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public void close() {
        this.setActive(false);
        this.setSearching(false);
        this.resetSearchingValues();
        this.hoveredOnBack = false;
        this.hoveredOnAdd = false;
        this.hoveredOnMainBox = false;
        this.tensorGUI.closeButton();
    }

    @Override
    public void onHovered(int mouseX, int mouseY) {
        this.hoveredOnBack = this.isHovered(this.getPosX() + this.getWidth() - 100, this.getPosY(), this.getPosX() + this.getWidth(), this.getPosY() + this.tensorGUI.titleBoxHeight, mouseX, mouseY);

        this.hoveredOnAdd = this.isHovered(this.getPosX(), this.getPosY() + this.tensorGUI.titleBoxHeight + 2, this.getPosX() + 55, this.getPosY() + this.tensorGUI.titleBoxHeight + 43, mouseX, mouseY);

        this.hoveredOnMainBox = this.isHovered(this.getPosX() + 57, this.getPosY() + this.tensorGUI.titleBoxHeight + 2, this.getPosX() + this.getWidth(), this.getPosY() + this.tensorGUI.titleBoxHeight + 43, mouseX, mouseY);

        if (!this.hoveredOnBack && !this.hoveredOnAdd && !this.hoveredOnMainBox && this.isHovered(this.getPosX(), this.getPosY() + this.tensorGUI.titleBoxHeight + 45, this.getPosX() + this.getWidth(), this.getPosY() + this.getHeight(), mouseX, mouseY)) {
            this.accountObjects.forEach(object -> object.onHovered(mouseX, mouseY));
        } else {
            this.accountObjects.forEach(object -> object.setHovered(false));
        }
    }

    @Override
    public void handleScroll(int scroll) {
        if (scroll == 0) {
            return;
        }

        int scrollAdjust = scroll;

        if (scrollY.getValue() > -this.tensorGUI.getScrollSpeed() && scroll > 0) {

            scrollAdjust = -scrollY.getValue();
        }

        int maxHeight = this.getHeight() - this.tensorGUI.titleBoxHeight - 45;

        if (this.getAccountListHeight() <= maxHeight) {
            return;
        }

        if (this.getAccountListHeight() + (scroll < 0 ? this.scrollY.getValue() - this.tensorGUI.getScrollSpeed() : 0) <= maxHeight) {
            int test = ((this.getAccountListHeight() + (scroll < 0 ? this.scrollY.getValue() : 0)) - maxHeight);

            if (test >= scrollAdjust) {
                scrollAdjust = -test;
            } else {
                return;
            }
        }

        this.scrollY.setValue(this.scrollY.getValue() + scrollAdjust);
    }

    private int getAccountListHeight() {
        AtomicInteger totalHeight = new AtomicInteger(0);

        this.accountObjects.forEach(accountObject -> {
            totalHeight.getAndAdd(accountObject.getHeight());
            totalHeight.getAndAdd(accountObject.getPasswordHeight());
        });

        return totalHeight.get();
    }

    public int getAccountObjectOffsetY(int number) {
        AtomicInteger offsetY = new AtomicInteger(0);

        if (number == 0) {
            return 0;
        }

        for (int i = 0; i < number; i++) {
            AccountObject object = this.accountObjects.get(i);

            offsetY.getAndAdd(object.getHeight());
            offsetY.getAndAdd(object.getPasswordHeight());
        }

        return offsetY.get();
    }

    private void setBackSearchValues() {
        if (this.searchingUsername && !this.usernameValue.equalsIgnoreCase("")) {
            this.prevUsername = this.usernameValue;
            this.usernameValue = "";
        }

        if (!this.searchingUsername && !this.prevUsername.equalsIgnoreCase("prev")) {
            this.usernameValue = this.prevUsername;
            this.prevUsername = "prev";
        }

        if (this.searchingPassword && !this.passwordValue.equalsIgnoreCase("")) {
            this.prevPassword = this.passwordValue;
            this.passwordValue = "";
        }

        if (!this.searchingPassword && !this.prevPassword.equalsIgnoreCase("prev")) {
            this.passwordValue = this.prevPassword;
            this.prevPassword = "prev";
        }
    }

    private void resetSearchingValues() {
        this.usernameValue = "";
        this.passwordValue = "";
        this.prevUsername = "prev";
        this.prevPassword = "prev";
    }

    private int[] badKeys() {
        return new int[]{
                0x00, 0x01, 0x0E, 0x0F, 0x1C, 0x1D, 0x2A, 0x36, 0x37,
                0x38, 0x39, 0x3A, 0x3B, 0x3C, 0x3D, 0x3E, 0x3F, 0x40,
                0x41, 0x42, 0x43, 0x44, 0x45, 0x46, 0x53, 0x57, 0x58,
                0x64, 0x65, 0x66, 0x67, 0x68, 0x69, 0x70, 0x71, 0x79,
                0x7B, 0x7D, 0x8D, 0x90, 0x91, 0x93, 0x94, 0x95, 0x96,
                0x97, 0x9C, 0x9D, 0xA7, 0xB7, 0xB8, 0xC4, 0xC5, 0xC7,
                0xC9, 0xCF, 0xD1, 0xD2, 0xD3, 0xDA, 0xDB, 0xDC, 0xDD,
                0xDE, 0xDF
        };
    }

    private void addNewAccount() {
        String username = this.usernameValue;
        String server = this.getWaypointServerName();
        String password = this.passwordValue;

        this.clearSearching();
        this.resetSearchingValues();

        if (username.isEmpty()) {
            return;
        }

        if (username.length() > Account.MAX_USERNAME_LENGTH) {
            return;
        }

        if (this.getAccountManager().hasAccount(username)) {

            if (password.isEmpty()) {
                return;
            }

            Account account = this.getAccountManager().getAccount(username);

            AccountObject object = this.accountObjects.stream().filter(accountObject -> accountObject.account == account).findFirst().orElse(null);

            if (account != null) {
                account.addPassword(server, password);
            }

            if (object != null) {
                object.resetPasswords();
            }

            return;
        }

        Account account = new Account(username);

        if (!password.isEmpty()) {
            account.addPassword(server, password);
        }

        this.getAccountManager().addAccount(account);
        this.addAccountToList(account);
    }

    private void addAccountToList(Account account) {
        int offsetY = this.accountObjects.size();

        AccountObject object = new AccountObject(account, this, this.clickGUI.x, this.clickGUI.y, this.scrollY, 0, this.tensorGUI.titleBoxHeight + 45, offsetY);
        this.accountObjects.add(object);
    }

    private void removeAccount(Account account) {
        this.accountToDelete = null;

        if (account.isSessionAccount()) {
            return;
        }

        AccountObject object = this.accountObjects.stream().filter(accountObject -> accountObject.account.equals(account)).findFirst().orElse(null);

        if (object != null) {
            this.accountObjects.remove(object);

            if (this.getAccountManager().isActiveAccount(account)) {
                this.buttonSetting.onChange();
            }

            this.getAccountManager().deleteAccount(account);

            for (int i = 0; i < this.accountObjects.size(); i++) {

                this.accountObjects.get(i).number = i;
            }

            if (this.scrollY.getValue() < 0) {

                int value = this.scrollY.getValue() + object.getHeight() + object.getPasswordHeight();

                if (value < 0) {
                    this.scrollY.setValue(value);
                } else {
                    this.scrollY.setValue(0);
                }
            }
        }
    }

    public void queueToRemoveAccount(Account account) {
        if (this.accountToDelete != null) {
            this.removeAccount(account);
        }

        this.accountToDelete = account;
    }

    public void resetHover(AccountObject accountObject) {

        this.hoveredOnBack = false;

        this.accountObjects.forEach(accountObject1 -> {

            if (accountObject1 != accountObject) {
                accountObject1.setHovered(false);
            }
        });
    }

    public void resetAccountObjects() {
        this.accountObjects.clear();
        this.scrollY.setValue(this.scrollY.getDefaultValue());

        AtomicInteger offsetY = new AtomicInteger(0);

        this.getAccountManager().getAccounts().forEach(account -> {

            AccountObject object = new AccountObject(account, this, this.clickGUI.x, this.clickGUI.y, this.scrollY, 0, this.tensorGUI.titleBoxHeight + 45, offsetY.getAndAdd(1));
            this.accountObjects.add(object);
        });
    }
}
