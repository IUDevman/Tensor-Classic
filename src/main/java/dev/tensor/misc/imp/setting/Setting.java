package dev.tensor.misc.imp.setting;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.tensor.feature.managers.HackManager;
import dev.tensor.misc.helpers.TensorLogger;
import dev.tensor.misc.imp.Globals;

import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Hoosiers
 * @since 04-12-2021
 */

public interface Setting<T> extends Globals {

    String getValueTypeName();

    String getName();

    T getDefaultValue();

    T getValue();

    String getValueString(boolean color);

    void setValue(T VALUE);

    void setValueFromString(String string) throws Exception;

    void load(JsonElement jsonElement);

    void save(JsonObject jsonObject);

    default void reset() {
        this.setValue(this.getDefaultValue());
    }

    default boolean hasCustomConfig() {
        return false;
    }

    default String getHackNameForSetting() {
        try {
            HackManager hackManager = this.getHackManager();

            if (hackManager == null) {
                return "Error";
            }

            AtomicReference<String> value = new AtomicReference<>("Unknown");

            hackManager.getHacks().forEach(hack -> {
                if (!value.get().equalsIgnoreCase("Unknown")) {
                    return;
                }

                if (hack.getSettings().contains(this)) {
                    value.set(hack.NAME);
                }
            });

            return value.get();

        } catch (Exception ignored) {
            this.getTensorLogger().log("Failed to get hack owner for setting: " + this.getName(), TensorLogger.Level.WARN);
            return "Error";
        }
    }
}
