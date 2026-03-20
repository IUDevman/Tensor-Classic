package dev.tensor.misc.imp.command;

/**
 * @author Hoosiers
 * @since 03-19-2026
 */

public abstract class SimpleCallableCommand implements Command {

    @Override
    public String getRegex() {
        return "{name}";
    }

    @Override
    public void onCommand(String[] ARGS) {
        this.onCommandTriggered();
    }

    public void onCommandTriggered() {

    }
}
