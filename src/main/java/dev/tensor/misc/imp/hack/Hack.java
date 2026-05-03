package dev.tensor.misc.imp.hack;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import dev.tensor.misc.helpers.ChatColor;
import dev.tensor.misc.imp.Globals;
import dev.tensor.misc.imp.setting.Setting;
import org.lwjgl.input.Keyboard;

import java.lang.annotation.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author Hoosiers
 * @since 04-12-2021
 */

public abstract class Hack implements Globals {

    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    public @interface Info {
        String name();

        Category category();

        int bind() default Keyboard.KEY_NONE;

        boolean drawn() default true;

        boolean messages() default true;

        boolean enabled() default false;
    }

    private Info getInfo() {
        return this.getClass().getAnnotation(Info.class);
    }

    public final String NAME = this.getInfo().name();

    public final Category CATEGORY = this.getInfo().category();

    public int BIND = this.getInfo().bind();

    public boolean DRAWN = this.getInfo().drawn();

    public boolean MESSAGES = this.getInfo().messages();

    private boolean ENABLED = this.getInfo().enabled();

    public ArrayList<Setting<?>> getSettings() {
        ArrayList<Setting<?>> SETTINGS = new ArrayList<>();

        Arrays.stream(this.getClass().getDeclaredFields()).forEach(field -> {

            if (Setting.class.isAssignableFrom(field.getType())) {

                if (!field.isAccessible()) {
                    field.setAccessible(true);
                }

                try {

                    SETTINGS.add((Setting<?>) field.get(this));

                } catch (Exception ignored) {
                    this.getTensorLogger().info("Failed to get setting for hack " + this.NAME + "!");
                }
            }
        });

        return SETTINGS;
    }

    public boolean isEnabled() {
        return this.ENABLED;
    }

    public void setEnabled(boolean ENABLED) {
        if (ENABLED) {
            this.enable();
            return;
        }

        this.disable();
    }

    public void toggle() {
        this.setEnabled(!this.isEnabled());
    }

    public void reset() {
        this.BIND = this.getInfo().bind();

        this.DRAWN = this.getInfo().drawn();

        this.MESSAGES = this.getInfo().messages();

        this.setEnabled(this.getInfo().enabled());

        this.getSettings().forEach(Setting::reset);
    }

    protected void enable() {
        this.ENABLED = true;

        this.getEventManager().register(this);
        this.getHackManager().onHackToggled(this, true);
        this.onEnable(this.failsNullCheck());
        this.sendToggleMessage(true);
    }

    private void disable() {
        this.ENABLED = false;

        this.getEventManager().unregister(this);
        this.getHackManager().onHackToggled(this, false);
        this.onDisable(this.failsNullCheck());
        this.sendToggleMessage(false);
    }

    private void sendToggleMessage(boolean ENABLED) {

        if (this.failsNullCheck()) {
            return;
        }

        if (!this.getHackManager().shouldSendMessagesGlobal() || !this.MESSAGES) {
            return;
        }

        String MESSAGE = this.NAME + " ";

        if (ENABLED) {
            MESSAGE += ChatColor.GREEN.text() + "Enabled!";
        } else {
            MESSAGE += ChatColor.RED.text() + "Disabled!";
        }

        this.sendClientMessageWithHeader(MESSAGE);
    }

    protected void onEnable(boolean isNull) {

    }

    protected void onDisable(boolean isNull) {

    }

    public void onClientTick() {

    }

    public void load(String modulePath) throws Exception {
        Path path = Paths.get(modulePath + this.NAME + ".json");

        if (!Files.exists(path)) {

            if (this.ENABLED) {
                this.setEnabled(true);
            }

            return;
        }

        JsonObject jsonObject = this.getConfigManager().getJsonObjectFromPath(path);

        this.MESSAGES = jsonObject.get("Messages").getAsBoolean();
        this.DRAWN = jsonObject.get("Drawn").getAsBoolean();
        this.BIND = jsonObject.get("Bind").getAsInt();

        JsonObject settingObject = jsonObject.getAsJsonObject("Settings");

        this.getSettings().forEach(objectSetting -> {
            JsonElement jsonElement = settingObject.get(objectSetting.getName());

            if (jsonElement != null && !objectSetting.shouldExcludeFromHackConfig()) {
                objectSetting.load(jsonElement);
            }
        });

        this.setEnabled(jsonObject.get("Enabled").getAsBoolean());

        this.getSettings().forEach(objectSetting -> {
            if (objectSetting.shouldExcludeFromHackConfig()) {
                objectSetting.load(null);
            }
        });
    }

    public void save(String modulePath) throws Exception {
        Path path = Paths.get(modulePath + this.NAME + ".json");
        this.getConfigManager().registerFile(path);

        JsonObject jsonObject = new JsonObject();

        jsonObject.add("Enabled", new JsonPrimitive(this.isEnabled()));
        jsonObject.add("Messages", new JsonPrimitive(this.MESSAGES));
        jsonObject.add("Drawn", new JsonPrimitive(this.DRAWN));
        jsonObject.add("Bind", new JsonPrimitive(this.BIND));

        JsonObject settingObject = new JsonObject();

        this.getSettings().forEach(objectSetting -> {

            if (!objectSetting.shouldExcludeFromHackConfig()) {
                objectSetting.save(settingObject);
            }
        });

        jsonObject.add("Settings", settingObject);

        this.getConfigManager().createAndWrite(path, jsonObject);

        this.getSettings().forEach(objectSetting -> {

            if (objectSetting.shouldExcludeFromHackConfig()) {
                objectSetting.save(null);
            }
        });
    }

    public boolean shouldDisableOnDisconnect() {
        return false;
    }
}
