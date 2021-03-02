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
 * The event is called when a {@link User} starts spectating the {@link Game}.
 * <p>A user may join a game after he has been spectating it. In this case the
 * {@link JoinGameEvent} will be fired.
 * <p>Use the {@link GameObject#onSpectate(Consumer)} method inside
 * {@link Feature}s to safely get all spectators of the game when the game starts
 * or your feature activates.
 */
@Data
@Accessors(fluent = true)
@EqualsAndHashCode(callSuper = true)
public class SpectateGameEvent extends UserEvent implements Cancellable {

    @Getter
    private static final HandlerList handlerList = new HandlerList();

    private final GameSession session;
    private boolean cancelled;

    public SpectateGameEvent(GameSession session, User user) {
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
