package dev.tensor.feature.hacks;

import dev.tensor.misc.imp.hack.Category;
import dev.tensor.misc.imp.hack.Hack;
import dev.tensor.misc.imp.setting.settings.ColorNoAlphaSetting;
import dev.tensor.misc.imp.setting.settings.IntegerSetting;

import java.awt.*;

/**
 * @author Hoosiers
 * @since 04-11-2026
 */

@Hack.Info(name = "AutoReconnect", category = Category.Misc, description = "Automatically logs into the server when you get kicked. Note: Is somewhat broken.")
public final class AutoReconnect extends Hack {

    public final IntegerSetting delaySeconds = new IntegerSetting("Delay Seconds", 5, 1, 60);
    public final IntegerSetting restartSeconds = new IntegerSetting("Restart Seconds", 60, 1, 180);
    public final ColorNoAlphaSetting textColor = new ColorNoAlphaSetting("Text Color", Color.YELLOW, "Sets the color of the message in the disconnect screen.");

    private String lastName;
    private int lastPort;

    public String getServerLastName() {
        return this.lastName;
    }

    public void setServerLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getServerLastPort() {
        return this.lastPort;
    }

    public void setServerLastPort(int lastPort) {
        this.lastPort = lastPort;
    }
}
