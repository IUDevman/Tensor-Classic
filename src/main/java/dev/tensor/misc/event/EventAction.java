package dev.tensor.misc.event;

import dev.tensor.misc.event.imp.Event;
import dev.tensor.misc.event.imp.EventPriority;
import dev.tensor.misc.imp.Globals;

import java.util.function.Consumer;

/**
 * @author Hoosiers
 * @since 05-05-2026
 */

public final class EventAction<T extends Event<T>> implements Globals {

    private final Class<T> eventClass;
    private final EventPriority priority;
    private final Consumer<? super T> action;

    private String declaringClass = "Unknown_Class";

    public EventAction(Class<T> eventClass, Consumer<? super T> action) {
        this.eventClass = eventClass;
        this.priority = EventPriority.Medium;
        this.action = action;
    }

    public EventAction(Class<T> eventClass, EventPriority priority, Consumer<? super T> action) {
        this.eventClass = eventClass;
        this.priority = priority;
        this.action = action;
    }

    public Class<T> getEventClass() {
        return this.eventClass;
    }

    public byte getPriority() {
        return this.priority.value();
    }

    public Consumer<? super T> getAction() {
        return this.action;
    }

    public String getDeclaringClass() {
        return this.declaringClass;
    }

    public void setDeclaringClass(Class<?> declaringClass) {
        if (declaringClass == null) {
            return;
        }

        this.declaringClass = declaringClass.getSimpleName();
    }

    public void setDeclaringClass(String declaringClass) {
        this.declaringClass = declaringClass;
    }
}
