package net.silthus.mcgamelib;

import net.silthus.mcgamelib.annotations.FeatureInfo;
import net.silthus.mcgamelib.event.EventFilter;
import net.silthus.mcgamelib.event.GameEvent;

import java.util.function.Consumer;

/**
 * A feature is the smallest part of a game and handles a single action or state.
 * <p>Every feature must be annotated with @{@link FeatureInfo} to provide additional metadata.
 * <p>The feature must also be added to a {@link Game} or {@link Phase} to be activated.
 * <p>You can also implement {@link org.bukkit.event.Listener} and subscribe to any bukkit
 * events your feature needs. Tag them with @{@link GameEvent} instead of
 * {@code EventHandler} to allow the usage of custom {@link EventFilter} classes.
 * <p>By default this will filter out all players that are spectating or not in the game the feature is activated for.
 * <pre>{@code
 *     @GameEvent
 *     public void onInteract(PlayerInteractEvent event) {
 *          // this event is only fired for players in the current game session
 *     }
 * }</pre>
 */
public interface Feature extends GameObject {

    /**
     * @return the name of the phase used in config paths and for reference
     */
    String name();

    /**
     * The load method is called after the config options have been
     * injected into the feature and before {@link #enable()} is called.
     * <p>Use it to load data and prepare your feature.
     */
    default void load() {}

    /**
     * The enable method is called when the phase or game is activated.
     * <p>The feature will also be registered as an event listener after enable has been called.
     * <p>Use the {@link #onPlayerJoin(Consumer)} and {@link #onPlayerQuit(Consumer)} methods of the {@link GameObject}
     * to get notified when a player joins and quits the game. These methods are preferred over the standard bukkit events.
     */
    void enable();

    /**
     * The disable method is called when the phase or game ends.
     * <p>The feature will also be unregistered as an event listener after disable has been called.
     * <p>Use the {@link #onPlayerJoin(Consumer)} and {@link #onPlayerQuit(Consumer)} methods of the {@link GameObject}
     * to get notified when a player joins and quits the game. These methods are preferred over the standard bukkit events.
     */
    void disable();
}
