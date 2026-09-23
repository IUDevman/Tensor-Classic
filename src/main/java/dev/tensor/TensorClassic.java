package dev.tensor;

import dev.tensor.feature.managers.*;
import dev.tensor.misc.helpers.TensorLogger;
import dev.tensor.misc.imp.Globals;
import dev.tensor.misc.imp.Manager;
import net.fabricmc.api.ModInitializer;

import java.util.HashMap;

/**
 * @author Hoosiers
 * @since 03-18-2026
 */

public final class TensorClassic implements ModInitializer, Globals {

    public static final String MOD_NAME = "Tensor Classic";

    public static final String MOD_NAME_SHORT = "Tensor";

    public static final String MOD_VERSION = "r1.1.0";

    private static TensorClassic INSTANCE;

    private static TensorLogger LOGGER;

    public final HashMap<Class<? extends Manager>, Manager> MANAGERS = new HashMap<>();

    public TensorClassic() {
        if (INSTANCE == null) {
            INSTANCE = this;
        }

        LOGGER = TensorLogger.getInstance();
    }

    public static TensorClassic getInstance() {
        if (INSTANCE == null) {
            new TensorClassic();
        }

        return INSTANCE;
    }

    public static TensorLogger getLogger() {
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

        this.getTensorLogger().log("Initialized " + MOD_NAME + " Version " + MOD_VERSION + " in " + (bootEndTime - bootStartTime) + " ms!", TensorLogger.Level.INFO);
    }

    private void loadManagers() {
        this.loadAndAddManager(new EventManager());
        this.loadAndAddManager(new ConfigManager());
        this.loadAndAddManager(new FriendManager());
        this.loadAndAddManager(new CapeManager());
        this.loadAndAddManager(new DiscordManager());
        this.loadAndAddManager(new RotationManager());
        this.loadAndAddManager(new EntityListManager());
        this.loadAndAddManager(new AccountManager());
        this.loadAndAddManager(new HackManager());
        this.loadAndAddManager(new CommandManager());
        this.loadAndAddManager(new PluginManager());
        this.loadAndAddManager(new DeepLManager());

        this.MANAGERS.values().forEach(manager -> manager.onLoadPost(LOGGER));
    }

    private <T extends Manager> void loadAndAddManager(T manager) {
        manager.load(LOGGER);

        this.MANAGERS.put(manager.getClass(), manager);
    }

    public void saveManagers() {
        long saveStartTime = System.currentTimeMillis();

        this.MANAGERS.values().forEach(manager -> manager.save(LOGGER));

        long saveEndTime = System.currentTimeMillis();

        this.getTensorLogger().log("Saved " + MOD_NAME + " Version " + MOD_VERSION + " in " + (saveEndTime - saveStartTime) + " ms!", TensorLogger.Level.INFO);

        this.getTensorLogger().generateLogReport(this.getConfigManager(), false);
    }

    public void restartManagers(boolean saveOld) {
        long resetStartTime = System.currentTimeMillis();

        if (saveOld) {
            this.saveManagers();
        }

        this.loadManagers();

        long resetEndTime = System.currentTimeMillis();

        this.getTensorLogger().log("Reset " + MOD_NAME + " Version " + MOD_VERSION + " in " + (resetEndTime - resetStartTime) + " ms!", TensorLogger.Level.INFO);
    }
}
