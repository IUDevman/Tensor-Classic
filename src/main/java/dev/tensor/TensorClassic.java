package dev.tensor;

import dev.tensor.feature.managers.CommandManager;
import dev.tensor.feature.managers.EventManager;
import dev.tensor.feature.managers.HackManager;
import dev.tensor.misc.imp.Globals;
import dev.tensor.misc.imp.Manager;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

/**
 * @author Hoosiers
 * @since 03-18-2026
 */

public final class TensorClassic implements ModInitializer, Globals {

    public static String MOD_NAME = "Tensor Classic";

    public static String MOD_VERSION = "d0.1.0";

    private static TensorClassic INSTANCE;

    private static Logger LOGGER;

    public TensorClassic() {
        if (INSTANCE == null) {
            INSTANCE = this;
        }

        LOGGER = LoggerFactory.getLogger(MOD_NAME);
    }

    public static TensorClassic getInstance() {
        if (INSTANCE == null) {
            new TensorClassic();
        }

        return INSTANCE;
    }

    public static Logger getLogger() {
        if (LOGGER == null) {
            new TensorClassic();
        }

        return LOGGER;
    }

    public ArrayList<Manager> MANAGERS = new ArrayList<>();

    @Override
    public void onInitialize() {
        long bootStartTime = System.currentTimeMillis();

        this.loadManagers();

        long bootEndTime = System.currentTimeMillis();

        LOGGER.info("Initialized " + MOD_NAME + " Version " + MOD_VERSION + " in " + (bootEndTime - bootStartTime) + " ms!");
    }

    private void loadManagers() {

        this.MANAGERS.add(this.returnLoadedManager(new EventManager()));
        this.MANAGERS.add(this.returnLoadedManager(new HackManager()));
        this.MANAGERS.add(this.returnLoadedManager(new CommandManager()));
    }

    private <T extends Manager> T returnLoadedManager(T manager) {

        if (!(manager instanceof EventManager)) {
            this.getEventManager().register(manager);
        }

        manager.load(LOGGER);

        return manager;
    }

    public void saveManagers() {

        long saveStartTime = System.currentTimeMillis();

        this.MANAGERS.forEach(manager -> manager.save(LOGGER));

        long saveEndTime = System.currentTimeMillis();

        LOGGER.info("Saved " + MOD_NAME + " Version " + MOD_VERSION + " in " + (saveEndTime - saveStartTime) + " ms!");
    }

    public void restartManagers(boolean save) {

        long resetStartTime = System.currentTimeMillis();

        if (save) {
            this.saveManagers();
        }

        this.MANAGERS.clear();
        this.loadManagers();

        long resetEndTime = System.currentTimeMillis();

        LOGGER.info("Reset " + MOD_NAME + " Version " + MOD_VERSION + " in " + (resetEndTime - resetStartTime) + " ms!");
    }
}
