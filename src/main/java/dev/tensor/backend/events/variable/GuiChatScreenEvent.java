package dev.tensor.backend.events.variable;

import dev.tensor.misc.event.imp.modifiers.EventVariable;

/**
 * @author Hoosiers
 * @since 03-15-2026
 */

public final class GuiChatScreenEvent extends EventVariable<GuiChatScreenEvent> {

    public String message;
    public int minX;
    public int maxX;
    public int minY;
    public int maxY;

    @Override
    public GuiChatScreenEvent withVariables(Object... args) {
        this.message = (String) args[0];
        this.minX = (int) args[1];
        this.maxX = (int) args[2];
        this.minY = (int) args[3];
        this.maxY = (int) args[4];


        return super.withVariables(args);
    }
}
