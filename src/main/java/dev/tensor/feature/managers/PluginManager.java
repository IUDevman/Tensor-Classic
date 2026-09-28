package dev.tensor.feature.managers;

import dev.tensor.TensorClassic;
import dev.tensor.misc.helpers.TensorLogger;
import dev.tensor.misc.imp.manager.Manager;
import dev.tensor.misc.imp.plugin.Plugin;
import net.fabricmc.loader.api.FabricLoader;

import java.util.ArrayList;
import java.util.Locale;

/**
 * @author Hoosiers
 * @since 05-10-2026
 */

public final class PluginManager implements Manager {

    @Override
    public String getName() {
        return "PluginManager";
    }

    private final ArrayList<Plugin> plugins = new ArrayList<>();

    @Override
    public void onLoad(TensorLogger LOGGER) {
        this.plugins.addAll(FabricLoader.getInstance().getEntrypoints(TensorClassic.MOD_NAME_SHORT.toLowerCase(Locale.ROOT), Plugin.class));

        if (this.plugins.isEmpty()) {
            return;
        }

        this.plugins.forEach(plugin -> plugin.load(LOGGER));

        LOGGER.log("Loaded: " + this.plugins.size() + " Plugins", TensorLogger.Level.INFO);
    }

    @Override
    public void onLoadPost(TensorLogger LOGGER) {
        if (this.plugins.isEmpty()) {
            return;
        }

        this.plugins.forEach(plugin -> plugin.loadPost(LOGGER));
    }

    @Override
    public void onSave(TensorLogger LOGGER) {
        if (this.plugins.isEmpty()) {
            return;
        }

        this.plugins.forEach(plugin -> plugin.save(LOGGER));

        LOGGER.log("Saved: " + this.plugins.size() + " Plugins", TensorLogger.Level.INFO);
    }
}
