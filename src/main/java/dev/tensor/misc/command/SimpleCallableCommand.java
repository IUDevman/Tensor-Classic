package dev.tensor.misc.command;

/**
 * @author Hoosiers
 * @since 03-19-2026
 */

public abstract class SimpleCallableCommand implements Command {

    @Override
    public void onCommand(String[] ARGS) {
        this.onCommandTriggered();
    }

    public void onCommandTriggered() {

    }
}
