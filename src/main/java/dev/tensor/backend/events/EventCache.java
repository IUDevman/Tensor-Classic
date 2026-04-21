package dev.tensor.backend.events;

import dev.tensor.misc.event.imp.Event;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

/**
 * @author Hoosiers
 * @since 04-20-2026
 */

public final class EventCache {

    private static final HashMap<Class<? extends Event>, Event> CACHED_EVENT_MAP = new HashMap<>();

    @SuppressWarnings({"unchecked", "DataFlowIssue"})
    @NotNull /* Yes I know it can return null with exceptions but this never happens suck my dick */
    public static <T extends Event> T getCachedEvent(Class<T> tClass) {
        Event cached = CACHED_EVENT_MAP.get(tClass);

        if (cached != null) {
            cached.reset();
            return (T) cached;
        }

        try {
            T toCache = tClass.getConstructor().newInstance();

            CACHED_EVENT_MAP.put(tClass, toCache);
            return toCache;

        } catch (Exception ignored) {
            return null;
        }
    }
}
