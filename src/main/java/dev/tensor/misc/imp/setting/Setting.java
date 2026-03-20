package dev.tensor.misc.imp.setting;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.tensor.misc.imp.Globals;

/**
 * @author Hoosiers
 * @since 04-12-2021
 */

public interface Setting<T> extends Globals {

    String getName();

    T getDefaultValue();

    T getValue();

    void setValue(T VALUE);

    void load(JsonElement jsonElement);

    void save(JsonObject jsonObject);

    default void reset() {
        this.setValue(this.getDefaultValue());
    }
}
