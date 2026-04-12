package dev.tensor.misc.gui;

import dev.tensor.TensorClassic;
import dev.tensor.feature.hacks.AutoReconnect;
import dev.tensor.misc.imp.Globals;
import net.minecraft.src.GuiConnectFailed;
import net.minecraft.src.GuiConnecting;

import java.text.DecimalFormat;

/**
 * @author Hoosiers
 * @since 04-11-2026
 */

public final class TensorGuiConnectFailed extends GuiConnectFailed implements Globals {

    private final AutoReconnect autoReconnect;
    private final boolean restart;
    private final long startTime;

    private double secondsRemaining;

    private final DecimalFormat timeFormat = new DecimalFormat("#.0");

    public TensorGuiConnectFailed(String string, String string2, Object... objects) {
        super(string, string2, objects);

        this.autoReconnect = this.getHackManager().getHack(AutoReconnect.class);

        this.restart = string.toLowerCase().contains("restart") || string2.toLowerCase().contains("restart");

        this.startTime = System.currentTimeMillis();

        this.secondsRemaining = (double) this.getDelayTime() / 1000;
    }

    @Override
    public void updateScreen() {
        long secondsDiff = System.currentTimeMillis() - startTime;

        if (secondsDiff >= this.getDelayTime()) {
            this.getMinecraft().displayGuiScreen(new GuiConnecting(this.getMinecraft(), this.autoReconnect.getServerLastName(), this.autoReconnect.getServerLastPort()));
        } else {
            this.secondsRemaining = (double) (this.getDelayTime() - secondsDiff) / 1000;
        }

        super.updateScreen();
    }

    @Override
    public void drawScreen(int i, int j, float f) {
        String timeRemaining = this.timeFormat.format(this.secondsRemaining);

        this.drawCenteredString(TensorClassic.MOD_NAME + ": Time until restart is [" + timeRemaining + "s]!", this.width / 2, this.height / 2 + 100, this.autoReconnect.textColor.getValue().getRGB());

        super.drawScreen(i, j, f);
    }

    private int getDelayTime() {
        return this.restart ? this.autoReconnect.restartSeconds.getValue() * 1000 : this.autoReconnect.delaySeconds.getValue() * 1000;
    }
}
