package dev.tensor.backend.events.variable;

import dev.tensor.misc.event.imp.modifiers.EventVariable;

/**
 * @author Hoosiers
 * @since 04-12-2026
 */

public final class MouseEvent extends EventVariable<MouseEvent> {

    public int dWheel;
    public boolean eventButtonState;
    public int eventButton;

    @Override
    public MouseEvent withVariables(Object... variables) {
        this.dWheel = (int) variables[0];
        this.eventButtonState = (boolean) variables[1];
        this.eventButton = (int) variables[2];


        return super.withVariables(variables);
    }
}
