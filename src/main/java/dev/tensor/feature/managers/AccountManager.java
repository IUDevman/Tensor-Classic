package dev.tensor.feature.managers;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import dev.tensor.misc.imp.Manager;
import dev.tensor.misc.imp.account.Account;
import org.slf4j.Logger;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Hoosiers
 * @since 05-19-2026
 */

@SuppressWarnings("unused")
public final class AccountManager implements Manager {

    @Override
    public String getName() {
        return "AccountManager";
    }

    private final String configPath = this.getConfigManager().getConfigPath() + "Accounts.json";

    private final ArrayList<Account> accounts = new ArrayList<>();

    private Account activeAccount = null;

    private boolean updated = false;

    @Override
    public void load(Logger LOGGER) {
        LOGGER.info("Loaded: " + this.getName());

        Thread toLoad = new Thread(() -> {

            while (this.getMinecraft() == null || this.getMinecraft().session == null || this.getMinecraft().session.username == null) {
                try {
                    Thread.sleep(20);
                } catch (InterruptedException ignored) {
                    this.getTensorLogger().info("Error with " + this.getName() + " loading thread!");
                }
            }

            try {
                this.loadSavedAccounts();

            } catch (Exception ignored) {
                this.getTensorLogger().info("Failed to load accounts!");
            }

            Account sessionAccount = this.accounts.stream().filter(account -> account.getUsername().equalsIgnoreCase(this.getMinecraft().session.username)).findFirst().orElse(null);

            if (sessionAccount != null) {
                sessionAccount.setSessionAccount(true);
                this.activeAccount = sessionAccount;
                return;
            }

            this.addSessionAccount();
        });

        toLoad.start();
    }

    private void loadSavedAccounts() throws Exception {
        Path path = Paths.get(this.configPath);

        if (!Files.exists(path)) {
            this.getTensorLogger().info("Failed to load accounts!");
            return;
        }

        JsonArray jsonArray = this.getConfigManager().getJsonArrayFromPath(path);

        if (jsonArray == null || jsonArray.isEmpty()) {
            return;
        }

        jsonArray.forEach(jsonElement -> {

            JsonObject jsonObject = jsonElement.getAsJsonObject();

            if (jsonObject != null) {
                String username = jsonObject.get("Username").getAsString();

                if (username != null && !username.isEmpty()) {
                    Account account = new Account(username);
                    this.addAccount(account);

                    HashMap<String, String> encryptedPasswordsServerMap = new HashMap<>();

                    JsonArray encryptedPasswordEntries = jsonObject.get("Passwords").getAsJsonArray();

                    if (encryptedPasswordEntries != null && !encryptedPasswordEntries.isEmpty()) {
                        encryptedPasswordEntries.forEach(jsonElement1 -> {

                            String combinedPassword = jsonElement1.getAsString();

                            if (combinedPassword != null && !combinedPassword.isEmpty()) {
                                String[] splitServerPassword = combinedPassword.split("§");

                                if (splitServerPassword.length == 2) {
                                    String server = splitServerPassword[0];
                                    String encryptedPassword = splitServerPassword[1];

                                    encryptedPasswordsServerMap.put(server, encryptedPassword);
                                }
                            }
                        });
                    }

                    if (!encryptedPasswordsServerMap.isEmpty()) {
                        encryptedPasswordsServerMap.forEach(account::addEncryptedPassword);
                    }
                }
            }
        });
    }

    @Override
    public void save(Logger LOGGER) {
        LOGGER.info("Saved: " + this.getName());

        try {
            this.saveAccounts();

        } catch (Exception ignored) {
            this.getTensorLogger().info("Failed to save accounts!");
        }
    }

    private void saveAccounts() throws Exception {
        Path path = Paths.get(this.configPath);
        this.getConfigManager().registerFile(path);

        JsonArray jsonArray = new JsonArray();

        this.accounts.forEach(account -> {

            JsonObject jsonObject = new JsonObject();

            jsonObject.add("Username", new JsonPrimitive(account.getUsername()));

            JsonArray encryptedPasswords = new JsonArray();

            HashMap<String, String> encryptedServerPasswordsMap = account.getEncryptedServerPasswordsMap();

            if (!encryptedServerPasswordsMap.isEmpty()) {
                encryptedServerPasswordsMap.forEach((s, s2) -> {
                    String combined = s + "§" + s2;
                    encryptedPasswords.add(combined);
                });
            }

            jsonObject.add("Passwords", encryptedPasswords);

            jsonArray.add(jsonObject);
        });

        this.getConfigManager().createAndWrite(path, jsonArray);
    }

    public ArrayList<Account> getAccounts() {
        return this.accounts;
    }

    public void addSessionAccount() {
        Account sessionAccount = new Account(true, this.getMinecraft().session.username);

        this.accounts.add(sessionAccount);

        this.activeAccount = sessionAccount;
    }

    public Account getSessionAccount() {
        return this.accounts.stream().filter(Account::isSessionAccount).findFirst().orElse(null);
    }

    public boolean isActiveAccount(Account account) {
        return this.activeAccount == account;
    }

    public void setActiveAccount(String username) {
        Account toBeActiveAccount = this.accounts.stream().filter(account -> account.getUsername().equalsIgnoreCase(username)).findFirst().orElse(null);

        if (toBeActiveAccount == null) {
            return;
        }

        this.setActiveAccount(toBeActiveAccount);
    }

    public void setActiveAccount(Account newActiveAccount) {
        this.activeAccount = newActiveAccount;

        if (!this.getMinecraft().session.username.equalsIgnoreCase(newActiveAccount.getUsername())) {
            this.getTensorLogger().info("Session username changed from '" + this.getMinecraft().session.username + "' to '" + newActiveAccount.getUsername() + "'!");
            this.getMinecraft().session.username = newActiveAccount.getUsername();
        }
    }

    public String getPasswordForActiveAccount(String server) {
        if (this.activeAccount == null) {
            return Account.DEFAULT_PASSWORD;
        }

        return this.activeAccount.getPasswordForServer(server);
    }

    public String getPasswordForActiveAccountOnCurrentServer() {
        if (this.activeAccount == null) {
            return Account.DEFAULT_PASSWORD;
        }

        return this.activeAccount.getPasswordForCurrentServer();
    }

    public HashMap<String, String> getServerPasswordsMapForAccount(String username) {
        Account account = this.accounts.stream().filter(account1 -> account1.getUsername().equalsIgnoreCase(username)).findFirst().orElse(null);

        return this.getServerPasswordsMapForAccount(account);
    }

    public HashMap<String, String> getServerPasswordsMapForAccount(Account account) {
        if (account == null) {
            return new HashMap<>();
        }

        return account.getServerPasswordsMap();
    }

    public void addPasswordToAccount(String username, String server, String password) {
        Account account = this.accounts.stream().filter(account1 -> account1.getUsername().equalsIgnoreCase(username)).findFirst().orElse(null);

        this.addPasswordToAccount(account, server, password);
    }

    public void addPasswordToAccount(Account account, String server, String password) {
        if (account == null) {
            return;
        }

        account.addPassword(server, password);
    }

    public void addPasswordToActiveAccount(String server, String password) {
        this.addPasswordToAccount(this.activeAccount, server, password);
    }

    public void addCurrentServerPasswordToAccount(String username, String password) {
        Account account = this.accounts.stream().filter(account1 -> account1.getUsername().equalsIgnoreCase(username)).findFirst().orElse(null);

        this.addCurrentServerPasswordToAccount(account, password);
    }

    public void addCurrentServerPasswordToAccount(Account account, String password) {
        if (account == null) {
            return;
        }

        account.addPasswordForCurrentServer(password);
    }

    public void addCurrentServerPasswordToActiveAccount(String password) {
        this.addCurrentServerPasswordToAccount(this.activeAccount, password);
    }

    public HashMap<String, String> getServerPasswordsMapForActiveAccount() {
        return this.getServerPasswordsMapForAccount(this.activeAccount);
    }

    public boolean hasAccount(String username) {
        return this.accounts.stream().filter(account1 -> account1.getUsername().equalsIgnoreCase(username)).findFirst().orElse(null) != null;
    }

    public Account getAccount(String username) {
        return this.accounts.stream().filter(account1 -> account1.getUsername().equalsIgnoreCase(username)).findFirst().orElse(null);
    }

    public void addAccount(String username) {
        if (username.length() > Account.MAX_USERNAME_LENGTH) {
            return;
        }

        if (!this.hasAccount(username)) {
            this.addAccount(new Account(username));
        }
    }

    public void addAccount(Account account) {
        if (!this.accounts.contains(account)) {
            this.accounts.add(account);
        }
    }

    public void deleteAccount(String username) {
        Account account = this.accounts.stream().filter(account1 -> account1.getUsername().equalsIgnoreCase(username)).findFirst().orElse(null);

        if (account != null) {
            this.deleteAccount(account);
        }
    }

    public void deleteAccount(Account account) {
        if (account.isSessionAccount()) {
            return;
        }

        if (this.activeAccount == account) {
            this.activeAccount = this.getSessionAccount();
        }

        this.accounts.remove(account);
    }

    public boolean hasUpdated() {
        return this.updated;
    }

    //Must be called with updated = true in order to refresh the accounts list in the ClickGUI panel.
    public void setUpdated(boolean updated) {
        this.updated = updated;
    }
}
