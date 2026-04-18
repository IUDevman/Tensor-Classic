package dev.tensor.feature.hacks;

import dev.tensor.misc.imp.hack.Category;
import dev.tensor.misc.imp.hack.Hack;
import dev.tensor.misc.imp.setting.settings.BooleanSetting;
import dev.tensor.misc.imp.setting.settings.IntegerSetting;
import net.minecraft.src.Packet3Chat;

import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * @author Hoosiers
 * @since 4-18-2026
 */

@Hack.Info(name = "PhysicsSpammer", category = Category.Misc)
public final class PhysicsSpammer extends Hack {

    public final BooleanSetting greenText = new BooleanSetting("GreenText", true);
    public final IntegerSetting tickDelay = new IntegerSetting("Tick Delay", 60, 1, 100);

    private final ArrayList<String> cachedPhysicsSpam = new ArrayList<>();

    private int delay = 0;
    private int index = 0;

    @Override
    public void onEnable(boolean isNull) {
        if (this.cachedPhysicsSpam.isEmpty()) {
            this.runChache.start();
        }

        this.delay = 0;
        this.index = 0;
    }

    @Override
    public void onDisable(boolean isNull) {
        if (!this.runChache.isInterrupted()) {
            this.runChache.interrupt();
        }
    }

    @Override
    public void onClientTick() {
        if (this.cachedPhysicsSpam.size() > 1000 && !this.runChache.isInterrupted()) {
            this.runChache.interrupt();
        }

        if (this.delay < this.tickDelay.getValue()) {
            this.delay++;
            return;

        } else {
            this.delay = 0;
        }

        if (this.index > this.cachedPhysicsSpam.size() - 1) {
            this.index = 0;
        }

        String message = this.cachedPhysicsSpam.get(index);
        this.index += 1;

        if (this.greenText.getValue()) {
            message = ">" + message;
        }

        this.sendPacket(new Packet3Chat(message));
    }

    private final Thread runChache = new Thread(() -> {
        while (this.cachedPhysicsSpam.size() < 1000) {
            try {
                Scanner scanner = new Scanner(new URL("https://snarxiv.org/").openStream());

                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();

                    if (line.startsWith("<p>") && !line.startsWith("<p><a href=") && !line.startsWith("<p>Links to:") && line.length() >= 4) {
                        this.cutAndAddMessage(line);
                    }
                }

            } catch (Exception ignored) {
            }
        }
    });

    private void cutAndAddMessage(String message)  {
        if (message.length() > 99) {
            String cut = message.substring(0, 98);
            this.cachedPhysicsSpam.add(cut);

            String otherEnd = message.substring(99);

            if (otherEnd.length() > 1) {
                this.cutAndAddMessage(message.substring(99));
            }

            return;
        }

        this.cachedPhysicsSpam.add(message);
    }
}
