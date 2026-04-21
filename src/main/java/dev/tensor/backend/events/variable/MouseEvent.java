package dev.tensor.backend.events.variable;

import dev.tensor.misc.event.imp.modifiers.EventVariable;

/**
 * @author Hoosiers
 * @since 04-12-2026
 */

public final class MouseEvent implements EventVariable {

    public int dWheel;
    public boolean eventButtonState;
    public int eventButton;

    @Override
    public <T extends EventVariable> T withVariables(Object... args) {
        this.dWheel = (int) args[0];
        this.eventButtonState = (boolean) args[1];
        this.eventButton = (int) args[2];


        return EventVariable.super.withVariables(args);
    }
}
