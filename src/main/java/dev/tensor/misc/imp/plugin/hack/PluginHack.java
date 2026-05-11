package dev.tensor.misc.imp.plugin.hack;

import dev.tensor.misc.helpers.rotation.Rotation;
import dev.tensor.misc.helpers.rotation.RotationPriority;
import dev.tensor.misc.imp.plugin.PluginGlobal;

/**
 * @author Hoosiers
 * @since 05-10-2026
 */

@SuppressWarnings("unused")
public interface PluginHack extends PluginGlobal {

    default int getRotationPriority() {
        return RotationPriority.PLUGIN_PRIORITY_DEFAULT;
    }

    default void addRotationToQueue(Rotation rotation) {
        this.addPluginRotationToQueue(rotation, this.getRotationPriority());
    }
}
