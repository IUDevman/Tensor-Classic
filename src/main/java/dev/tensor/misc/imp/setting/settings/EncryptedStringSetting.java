package dev.tensor.misc.imp.setting.settings;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import dev.tensor.misc.helpers.TensorLogger;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;

/**
 * @author Hoosiers
 * @since 03-24-2026
 */

public class EncryptedStringSetting extends StringSetting {

    public EncryptedStringSetting(String NAME, String VALUE) {
        super(NAME, VALUE);
    }

    @Override
    public void load(JsonElement jsonElement) {
        try {
            this.setValueFromString(this.decryptValue(jsonElement.getAsString()));
        } catch (Exception ignored) {
            this.getTensorLogger().log("Failed to load EncryptedStringSetting: " + this.getName() + " for hack: " + this.getHackNameForSetting() + "!", TensorLogger.Level.ERROR);
        }
    }

    @Override
    public void save(JsonObject jsonObject) {

        String encryptedValue = this.encryptValue(this.getValue());

        jsonObject.add(this.getName(), new JsonPrimitive(encryptedValue));
    }

    private String decryptValue(String encryptedValue) {
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();

        String seed = System.getProperties().getProperty("user.name") + System.getProperties().getProperty("java.version");
        encryptor.setPassword(seed);

        return encryptor.decrypt(encryptedValue);
    }

    private String encryptValue(String value) {
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();

        String seed = System.getProperties().getProperty("user.name") + System.getProperties().getProperty("java.version");
        encryptor.setPassword(seed);

        return encryptor.encrypt(value);
    }
}
