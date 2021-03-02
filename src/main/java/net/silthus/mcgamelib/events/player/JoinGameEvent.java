package net.silthus.mcgamelib.events.player;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.Accessors;
import net.silthus.mcgamelib.*;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

/**
 * The event is called when a {@link User} joins a {@link GameSession}.
 * <p>The {@link Game} may or not have been started at that moment.
 * <p>Use the {@link GameObject#onJoin(Consumer)} method inside
 * {@link Feature}s to safely get all users of the game when the game is started
 * and your feature activated.
 */
@Data
@Accessors(fluent = true)
@EqualsAndHashCode(callSuper = true)
public class JoinGameEvent extends UserEvent implements Cancellable {

    @Getter
    private static final HandlerList handlerList = new HandlerList();

    private final GameSession session;
    private boolean cancelled;

    public JoinGameEvent(GameSession session, User user) {
        super(user);
        this.session = session;
    }

    @Override
    public boolean isCancelled() {

        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {

        this.cancelled = cancel;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlerList;
    }
}
