package net.silthus.mcgamelib.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Marker class for events
 */
public abstract class MCGameLibEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}
