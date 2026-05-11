package dev.tensor.feature.managers;

import dev.tensor.TensorClassic;
import dev.tensor.misc.imp.Manager;
import dev.tensor.misc.imp.plugin.Plugin;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;

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
    public void load(Logger LOGGER) {
        LOGGER.info("Loaded: " + this.getName());

        this.plugins.addAll(FabricLoader.getInstance().getEntrypoints(TensorClassic.MOD_NAME_SHORT.toLowerCase(Locale.ROOT), Plugin.class));

        if (this.plugins.isEmpty()) {
            return;
        }

        this.plugins.forEach(plugin -> plugin.load(LOGGER));

        LOGGER.info("Loaded: " + this.plugins.size() + " Plugins");
    }

    @Override
    public void loadPost(Logger LOGGER) {
        if (this.plugins.isEmpty()) {
            return;
        }

        this.plugins.forEach(plugin -> plugin.loadPost(LOGGER));
    }

    @Override
    public void save(Logger LOGGER) {
        LOGGER.info("Saved: " + this.getName());

        if (this.plugins.isEmpty()) {
            return;
        }

        this.plugins.forEach(plugin -> plugin.save(LOGGER));

        LOGGER.info("Saved: " + this.plugins.size() + " Plugins");
    }
}
