package dev.tensor.feature.managers;

import dev.tensor.misc.event.EventAction;
import dev.tensor.misc.event.imp.Event;
import dev.tensor.misc.imp.Manager;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

/**
 * @author Hoosiers
 * @since 05-05-2026
 */

@SuppressWarnings({"rawtypes", "unchecked"})
public final class EventManager implements Manager {

    @Override
    public String getName() {
        return "EventManager";
    }

    private final HashMap<Class<? extends Event>, ArrayList<EventAction>> ACTION_MAP = new HashMap<>();

    @Override
    public void load(Logger LOGGER) {
        LOGGER.info("Loaded: " + this.getName());
    }

    @Override
    public void save(Logger LOGGER) {
        LOGGER.info("Saved: " + this.getName());
    }

    public void addAction(EventAction eventAction) {
        Class<? extends Event> eventClass = eventAction.getEventClass();
        boolean hasClass = this.ACTION_MAP.containsKey(eventClass);

        if (hasClass) {
            ArrayList<EventAction> actions = this.ACTION_MAP.get(eventClass);

            if (!actions.contains(eventAction)) {
                actions.add(eventAction);
            }

            actions.sort(Comparator.comparing(EventAction::getPriority));
            return;
        }

        ArrayList<EventAction> actions = new ArrayList<>();
        actions.add(eventAction);

        this.ACTION_MAP.put(eventClass, actions);
    }

    public void removeAction(EventAction eventAction) {
        Class<? extends Event> eventClass = eventAction.getEventClass();
        boolean hasClass = this.ACTION_MAP.containsKey(eventClass);

        if (hasClass) {
            ArrayList<EventAction> actions = this.ACTION_MAP.get(eventClass);
            actions.remove(eventAction);
        }
    }

    public void callEvent(Event event) {
        Class<? extends Event> eventClass = event.getClass();
        boolean hasEvent = this.ACTION_MAP.containsKey(eventClass);

        if (hasEvent) {
            try {
                ArrayList<EventAction> actions = this.ACTION_MAP.get(eventClass);

                if (actions.isEmpty()) {
                    return;
                }

                actions.forEach(action -> action.getAction().accept(event));

            } catch (Exception ignored) {
                this.getTensorLogger().info("D:< Failed to call event '" + event.getClass().getSimpleName() + "'!");
            }
        }
    }
}
