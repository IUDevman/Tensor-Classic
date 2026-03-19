package dev.tensor.misc.imp.hack;

import dev.tensor.misc.imp.Globals;
import dev.tensor.misc.imp.setting.Setting;
import org.lwjgl.input.Keyboard;

import java.lang.annotation.*;
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

    public void launchStartUp() {
        this.setEnabled(this.ENABLED);
    }

    private final ArrayList<Setting<?>> SETTINGS = new ArrayList<>();

    public Hack() {
        Arrays.stream(this.getClass().getDeclaredFields()).forEach(field -> {

            if (Setting.class.isAssignableFrom(field.getType())) {

                if (!field.isAccessible()) {
                    field.setAccessible(true);
                }

                try {

                    this.SETTINGS.add((Setting<?>) field.get(this));

                } catch (Exception ignored) {
                    this.getTensorLogger().info("Failed to get setting for hack " + this.NAME + "!");
                }
            }
        });
    }

    public ArrayList<Setting<?>> getSettings() {
        return this.SETTINGS;
    }

    public void reset() {
        this.BIND = this.getInfo().bind();

        this.DRAWN = this.getInfo().drawn();

        this.MESSAGES = this.getInfo().messages();

        this.ENABLED = this.getInfo().enabled();

        this.getSettings().forEach(Setting::reset);
    }

    protected void enable() {
        this.ENABLED = true;

        this.getEventManager().register(this);

        this.onEnable(this.failsNullCheck());
        this.sendToggleMessage(true);
    }

    private void disable() {
        this.ENABLED = false;

        this.getEventManager().register(this);

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
            MESSAGE += this.getColor(ChatColor.GREEN) + "Enabled!";
        } else {
            MESSAGE += this.getColor(ChatColor.RED) + "Disabled!";
        }

        this.sendClientMessageWithPrefix(MESSAGE);
    }

    protected void onEnable(boolean isNull) {

    }

    protected void onDisable(boolean isNull) {

    }

    public void onClientTick() {

    }

    public void onRender2D() {

    }

    public void onRender3D() {

    }
}
