package dev.tensor.feature.commands;

import dev.tensor.misc.helpers.TensorLogger;
import dev.tensor.misc.imp.command.SimpleCallableCommand;

/**
 * @author Hoosiers
 * @since 06-14-2026
 */

public final class FloodLogger extends SimpleCallableCommand {

    @Override
    public String getName() {
        return "FloodLogger";
    }

    @Override
    public String[] getAcceptedNames() {
        return new String[]{
                this.getName()
        };
    }

    @Override
    public String getDescription() {
        return "Fills the logger with messages; is useful for debugging.";
    }

    @Override
    public void onCommandTriggered() {
        for (int i = 0; i < 10; i++) {
            this.getTensorLogger().log("FloodLogger Active (" + i + ")!", TensorLogger.Level.INFO);
        }
    }
}
