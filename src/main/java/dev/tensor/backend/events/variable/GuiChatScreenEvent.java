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
    public GuiChatScreenEvent withVariables(Object... variables) {
        this.message = (String) variables[0];
        this.minX = (int) variables[1];
        this.maxX = (int) variables[2];
        this.minY = (int) variables[3];
        this.maxY = (int) variables[4];

        return super.withVariables(variables);
    }
}
