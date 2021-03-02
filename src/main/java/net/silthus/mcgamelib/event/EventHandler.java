package net.silthus.mcgamelib.event;

import lombok.NonNull;
import net.silthus.mcgamelib.Game;
import net.silthus.mcgamelib.MCGameLib;
import net.silthus.mcgamelib.User;
import net.silthus.mcgamelib.UserHandler;
import net.silthus.mcgamelib.events.player.UserEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.plugin.EventExecutor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EventHandler implements Listener {

    private static final Logger log = Logger.getLogger(EventHandler.class.getName());

    private final MCGameLib plugin;
    private final UserHandler userHandler;

    private final EventExecutor eventExecutor = (listener, event) -> callEvent(event);

    private final Map<Class<? extends Event>, List<RegisteredListener>> activeEvents = new HashMap<>();
    private final Map<UUID, List<RegisteredListener>> activeListeners = new HashMap<>();

    private final Map<Class<? extends Event>, Method> reflectionCachePlayer = new HashMap<>();
    private final Map<Class<? extends Event>, Method> reflectionCacheUser = new HashMap<>();
    private final Map<Class<? extends Event>, Method> reflectionCacheEntity = new HashMap<>();

    public EventHandler(MCGameLib plugin, UserHandler userHandler) {
        this.plugin = plugin;
        this.userHandler = userHandler;
    }

    public void registerEvents(@NonNull Listener listener, @NonNull Game game) {

        Set<RegisteredListener> newEvents = new HashSet<>();
        Arrays.stream(listener.getClass().getMethods()).filter((method -> method.isAnnotationPresent(GameEvent.class))).forEach(
                method -> {
                    if (method.getParameterCount() != 1 && method.getParameterCount() != 2) {
                        log.warning("Invalid parameters for " + listener.getClass().getName() + " " + method.toString());
                        return;
                    }

                    if (Event.class.isAssignableFrom(method.getParameterTypes()[0])) {
                        //noinspection unchecked
                        Class<Event> eventClass = (Class<Event>) method.getParameterTypes()[0];

                        RegisteredListener registeredListener = new RegisteredListener(listener,
                                game,
                                eventClass,
                                method,
                                method.getAnnotation(GameEvent.class)
                        );

                        activeListeners.computeIfAbsent(game.id(), (key) -> new CopyOnWriteArrayList<>()).add(registeredListener);

                        activeEvents.computeIfAbsent(eventClass, (key) -> {
                            newEvents.add(registeredListener);
                            return new CopyOnWriteArrayList<>();
                        }).add(registeredListener);
                    } else {
                        log.warning("Invalid parameter for " + listener.getClass().getName() + " " + method.toString());
                    }
                }
        );

        // check if we need to register a new event
        newEvents.forEach(registeredListener ->
                Bukkit.getServer().getPluginManager().registerEvent(registeredListener.eventClass(),
                        this,
                        registeredListener.annotation().priority(),
                        eventExecutor,
                        plugin,
                        registeredListener.annotation().ignoreCancelled()
                ));

        // register normal events
        Bukkit.getServer().getPluginManager().registerEvents(listener, plugin);
    }

    public void unregister(@NonNull Listener listener, @NonNull Game game) {
        //noinspection unchecked
        Arrays.stream(listener.getClass().getMethods())
                .filter((method -> method.isAnnotationPresent(GameEvent.class)))
                .filter(method -> Event.class.isAssignableFrom(method.getParameterTypes()[0]))
                .map(method -> (Class<Event>) method.getParameterTypes()[0]).forEach(
                eventClass -> activeEvents.get(eventClass).removeIf(registeredListener -> registeredListener.listener().equals(listener)));

        if (activeListeners.containsKey(game.id())) {
            activeListeners.get(game.id()).removeIf(registeredListener -> registeredListener.listener().equals(listener));
            if (activeListeners.get(game.id()).size() == 0) {
                activeListeners.remove(game.id());
            }
        }

        HandlerList.unregisterAll(listener);
    }

    @SuppressWarnings("unchecked")
    public <T extends Event> void callEvent(@NonNull T event) {

        Class<Event> eventClass = (Class<Event>) event.getClass();
        while (!eventClass.equals(Object.class)) {
            if (activeEvents.containsKey(eventClass)) {
                activeEvents.get(eventClass).forEach(registeredListener -> {
                    Optional<User> user = Optional.empty();
                    boolean tried = false;
                    for (EventFilter filter : registeredListener.filters()) {
                        if (user.isEmpty() && !tried) {
                            user = figureOutUser(event);
                            tried = true;
                        }
                        if (!filter.filter(event, registeredListener, user)) {
                            return;
                        }
                    }

                    try {
                        if (registeredListener.method().getParameterCount() == 2) {
                            registeredListener.method().invoke(registeredListener.listener(), event, user.orElse(null));
                        } else {
                            registeredListener.method().invoke(registeredListener.listener(), event);
                        }
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        log.log(Level.SEVERE, "Error while calling eventhandler!", e);
                    }
                });
                break;
            } else {
                eventClass = (Class<Event>) eventClass.getSuperclass();
            }
        }
    }

    private <T extends Event> Optional<User> figureOutUser(@NonNull T event) {

        if (event instanceof PlayerEvent) {
            return userHandler.user(((PlayerEvent) event).getPlayer().getUniqueId());
        } else if (event instanceof UserEvent) {
            return Optional.of(((UserEvent) event).getUser());
        }

        // search for method to get player
        if (!reflectionCachePlayer.containsKey(event.getClass()) && !reflectionCacheUser.containsKey(event.getClass()) && !reflectionCacheEntity.containsKey(event.getClass())) {
            Method entityMethod = null;
            boolean found = false;
            for (Method m : event.getClass().getMethods()) {
                if (m.getReturnType().equals(User.class)) {
                    reflectionCacheUser.put(event.getClass(), m);
                    found = true;
                    break;
                } else if (m.getReturnType().equals(Player.class)) {
                    reflectionCachePlayer.put(event.getClass(), m);
                    found = true;
                    break;
                } else if (Entity.class.isAssignableFrom(m.getReturnType())) {
                    entityMethod = m;
                }
            }

            // entity should be fallback, if there is something better don't use it
            if (!found && entityMethod != null) {
                reflectionCacheEntity.put(event.getClass(), entityMethod);
            }
        }

        // check cache to find user
        if (reflectionCacheUser.containsKey(event.getClass())) {
            Method method = reflectionCacheUser.get(event.getClass());
            try {
                return Optional.of((User) method.invoke(event));
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
                return Optional.empty();
            }
        } else if (reflectionCachePlayer.containsKey(event.getClass())) {
            Method method = reflectionCachePlayer.get(event.getClass());
            try {
                return userHandler.user(((Player) method.invoke(event)).getUniqueId());
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
                return Optional.empty();
            }
        } else if (reflectionCacheEntity.containsKey(event.getClass())) {
            Method method = reflectionCacheEntity.get(event.getClass());
            try {
                Entity entity = (Entity) method.invoke(event);
                if (entity instanceof Player) {
                    return userHandler.user(entity.getUniqueId());
                } else {
                    return Optional.empty();
                }
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
                return Optional.empty();
            }
        } else {
            log.warning("Could not find a way to get the user from " + event.getEventName() + "!");
            return Optional.empty();
        }
    }
}
