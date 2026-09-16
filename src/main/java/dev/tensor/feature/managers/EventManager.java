package dev.tensor.feature.managers;

import dev.tensor.misc.event.EventAction;
import dev.tensor.misc.event.imp.Event;
import dev.tensor.misc.helpers.TensorLogger;
import dev.tensor.misc.imp.Manager;

import java.util.Comparator;
import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArrayList;

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

    private final HashMap<Class<? extends Event>, CopyOnWriteArrayList<EventAction>> ACTION_MAP = new HashMap<>();

    @Override
    public void load(TensorLogger LOGGER) {
        LOGGER.log("Loaded: " + this.getName(), TensorLogger.Level.INFO);
    }

    @Override
    public void save(TensorLogger LOGGER) {
        LOGGER.log("Saved: " + this.getName(), TensorLogger.Level.INFO);
    }

    public void addAction(EventAction eventAction) {
        Class<? extends Event> eventClass = eventAction.getEventClass();
        boolean hasClass = this.ACTION_MAP.containsKey(eventClass);

        if (hasClass) {
            CopyOnWriteArrayList<EventAction> actions = this.ACTION_MAP.get(eventClass);

            if (!actions.contains(eventAction)) {
                actions.add(eventAction);
            }

            actions.sort(Comparator.comparing(EventAction::getPriority));
            return;
        }

        CopyOnWriteArrayList<EventAction> actions = new CopyOnWriteArrayList<>();
        actions.add(eventAction);

        this.ACTION_MAP.put(eventClass, actions);
    }

    public void removeAction(EventAction eventAction) {
        Class<? extends Event> eventClass = eventAction.getEventClass();
        boolean hasClass = this.ACTION_MAP.containsKey(eventClass);

        if (hasClass) {
            CopyOnWriteArrayList<EventAction> actions = this.ACTION_MAP.get(eventClass);
            actions.remove(eventAction);
        }
    }

    /**
     * For events that are called many times, we want to avoid going through the
     * process of getting them from the cache, resetting variables, etc. just
     * for it to not do anything when called (no target actions).
     * So, we can do the checks beforehand using shouldCall() to ensure that we
     * are not wasting time.
     * But, we don't want to run the checks twice if an event has target actions,
     * so we can set ignoreChecks to true to skip them.
     */

    public void callEvent(Event event, boolean ignoreChecks) {
        Class<? extends Event> eventClass = event.getClass();
        CopyOnWriteArrayList<EventAction> actions = this.ACTION_MAP.get(eventClass);

        if (ignoreChecks || (actions != null && !actions.isEmpty())) {
            actions.forEach(action -> {

                try {
                    action.getAction().accept(event);

                } catch (Exception ignored) {
                    this.getTensorLogger().log("Failed to call event '" + event.getClass().getSimpleName() + "' for action in class '" + action.getDeclaringClass() + "'!", TensorLogger.Level.WARN);
                }
            });
        }
    }

    public boolean shouldCall(Class<? extends Event> eventClass) {
        CopyOnWriteArrayList<EventAction> actions = this.ACTION_MAP.get(eventClass);

        return actions != null && !actions.isEmpty();
    }
}
