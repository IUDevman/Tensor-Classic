package dev.tensor;

import dev.tensor.feature.managers.*;
import dev.tensor.misc.imp.Globals;
import dev.tensor.misc.imp.Manager;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

/**
 * @author Hoosiers
 * @since 03-18-2026
 */

public final class TensorClassic implements ModInitializer, Globals {

    public static final String MOD_NAME = "Tensor Classic";

    public static final String MOD_NAME_SHORT = "Tensor";

    public static final String MOD_VERSION = "r1.0.0";

    private static TensorClassic INSTANCE;

    private static Logger LOGGER;

    public final HashMap<Class<? extends Manager>, Manager> MANAGERS = new HashMap<>();

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

    @Override
    public void onInitialize() {
        long bootStartTime = System.currentTimeMillis();

        this.loadManagers();

        Runtime.getRuntime().addShutdownHook(new Thread(this::saveManagers));

        long bootEndTime = System.currentTimeMillis();

        LOGGER.info("Initialized " + MOD_NAME + " Version " + MOD_VERSION + " in " + (bootEndTime - bootStartTime) + " ms!");
    }

    private void loadManagers() {
        this.addManager(this.returnLoadedManager(new EventManager()));
        this.addManager(this.returnLoadedManager(new ConfigManager()));
        this.addManager(this.returnLoadedManager(new FriendManager()));
        this.addManager(this.returnLoadedManager(new CapeManager()));
        this.addManager(this.returnLoadedManager(new DiscordManager()));
        this.addManager(this.returnLoadedManager(new RotationManager()));
        this.addManager(this.returnLoadedManager(new EntityListManager()));
        this.addManager(this.returnLoadedManager(new AccountManager()));
        this.addManager(this.returnLoadedManager(new HackManager()));
        this.addManager(this.returnLoadedManager(new CommandManager()));
        this.addManager(this.returnLoadedManager(new PluginManager()));

        this.MANAGERS.values().forEach(manager -> manager.loadPost(LOGGER));
    }

    private <T extends Manager> void addManager(T manager) {
        this.MANAGERS.put(manager.getClass(), manager);
    }

    private <T extends Manager> T returnLoadedManager(T manager) {
        manager.load(LOGGER);

        return manager;
    }

    public void saveManagers() {
        long saveStartTime = System.currentTimeMillis();

        this.MANAGERS.values().forEach(manager -> manager.save(LOGGER));

        long saveEndTime = System.currentTimeMillis();

        LOGGER.info("Saved " + MOD_NAME + " Version " + MOD_VERSION + " in " + (saveEndTime - saveStartTime) + " ms!");
    }

    public void restartManagers(boolean saveOld) {
        long resetStartTime = System.currentTimeMillis();

        if (saveOld) {
            this.saveManagers();
        }

        this.loadManagers();

        long resetEndTime = System.currentTimeMillis();

        LOGGER.info("Reset " + MOD_NAME + " Version " + MOD_VERSION + " in " + (resetEndTime - resetStartTime) + " ms!");
    }
}
