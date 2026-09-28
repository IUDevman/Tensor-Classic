package dev.tensor.misc.imp.plugin;

import dev.tensor.misc.helpers.TensorLogger;
import dev.tensor.misc.helpers.rotation.Rotation;
import dev.tensor.misc.imp.Globals;
import dev.tensor.misc.imp.manager.Manager;
import dev.tensor.misc.imp.command.Command;
import dev.tensor.misc.imp.hack.Hack;

/**
 * @author Hoosiers
 * @since 05-10-2026
 */

@SuppressWarnings("unused")
public interface PluginGlobal extends Globals {

    default void addAndLoadManager(Manager manager) {
        this.addAndLoadManager(this.getTensorLogger(), manager);
    }

    default void addAndLoadManager(TensorLogger LOGGER, Manager manager) {
        this.getTensor().MANAGERS.put(manager.getClass(), manager);

        manager.load(LOGGER);
    }

    default void addHack(Hack hack) {
        this.getHackManager().addHack(hack);
        this.getHackManager().postSortHacks();
    }

    default void addCommand(Command command) {
        this.getCommandManager().addCommand(command);
        this.getCommandManager().postSortCommands();
    }

    default void addPluginRotationToQueue(Rotation rotation, int priority) {
        this.getRotationManager().addToRotationQueue(rotation, priority);
    }
}
