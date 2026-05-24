package dev.tensor.misc.imp.account;

import dev.tensor.misc.imp.Globals;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;

import java.util.HashMap;

/**
 * @author Hoosiers
 * @since 05-19-2026
 */

@SuppressWarnings("unused")
public final class Account implements Globals {

    public static final String DEFAULT_PASSWORD = "Set your password here. Config is encrypted!";

    public static final int MAX_USERNAME_LENGTH = 16;
    public static final int MAX_PASSWORD_LENGTH = 32;

    private boolean sessionAccount;

    private final String username;
    private final HashMap<String, String> serverPasswordsMap = new HashMap<>();

    public Account(boolean sessionAccount, String username) {
        this.sessionAccount = sessionAccount;

        this.username = username;
    }

    public Account(String username) {
        this.sessionAccount = false;

        this.username = username;
    }

    public boolean isSessionAccount() {
        return this.sessionAccount;
    }

    public void setSessionAccount(boolean isInitial) {
        this.sessionAccount = isInitial;
    }

    public String getUsername() {
        return this.username;
    }

    public HashMap<String, String> getServerPasswordsMap() {
        return this.serverPasswordsMap;
    }

    public String getPasswordForServer(String server) {
        String password = this.serverPasswordsMap.get(server);

        if (password == null || password.isEmpty()) {
            return DEFAULT_PASSWORD;
        }

        return password;
    }

    public String getPasswordForCurrentServer() {
        return this.getPasswordForServer(this.getWaypointServerName());
    }

    public void addPassword(String server, String password) {
        if (password.length() > MAX_PASSWORD_LENGTH) {
            return;
        }

        this.serverPasswordsMap.put(server, password);
    }

    public void addPasswordForCurrentServer(String password) {
        if (password.length() > MAX_PASSWORD_LENGTH) {
            return;
        }

        this.serverPasswordsMap.put(this.getWaypointServerName(), password);
    }

    public void removePasswordForServer(String server) {
        this.serverPasswordsMap.remove(server);
    }

    public void removePasswordForCurrentServer() {
        this.removePasswordForServer(this.getWaypointServerName());
    }

    public void addEncryptedPassword(String server, String encryptedPassword) {
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();

        String seed = System.getProperties().getProperty("user.name") + System.getProperties().getProperty("java.version");
        encryptor.setPassword(seed);

        String decryptedPassword = encryptor.decrypt(encryptedPassword);

        this.serverPasswordsMap.put(server, decryptedPassword);
    }

    public HashMap<String, String> getEncryptedServerPasswordsMap() {
        HashMap<String, String> encryptedServerPasswordsMap = new HashMap<>();

        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();

        String seed = System.getProperties().getProperty("user.name") + System.getProperties().getProperty("java.version");
        encryptor.setPassword(seed);

        this.serverPasswordsMap.forEach((s, s2) -> encryptedServerPasswordsMap.put(s, encryptor.encrypt(s2)));

        return encryptedServerPasswordsMap;
    }
}
