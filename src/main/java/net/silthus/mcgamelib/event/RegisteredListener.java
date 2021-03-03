package net.silthus.mcgamelib.event;

import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.java.Log;
import net.silthus.mcgamelib.GameSession;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

@Data
@Accessors(fluent = true)
@Log(topic = "MCGameLib")
public class RegisteredListener {

    private final Listener listener;
    private final GameSession session;
    private final Class<Event> eventClass;
    private final Method method;
    private final GameEvent annotation;
    private final List<EventFilter> filters = new ArrayList<>();

    public RegisteredListener(Listener listener, GameSession session, Class<Event> eventClass, Method method, GameEvent annotation) {

        this.listener = listener;
        this.session = session;
        this.eventClass = eventClass;
        this.method = method;
        this.annotation = annotation;

        for (Class<? extends EventFilter> filter : annotation.filters()) {
            try {
                filters.add(filter.getConstructor().newInstance());
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                log.severe("failed to create EventFilter  " + filter.getCanonicalName() + ": " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public List<EventFilter> filters() {

        return List.copyOf(filters);
    }
}
