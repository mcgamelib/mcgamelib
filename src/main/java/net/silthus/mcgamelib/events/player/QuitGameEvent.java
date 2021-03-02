package net.silthus.mcgamelib.events.player;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Value;
import lombok.experimental.Accessors;
import net.silthus.mcgamelib.*;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

/**
 * The event is called when a {@link User} quits a {@link GameSession}.
 * <p>The {@link Game} may or not have been ended or started at that moment.
 * <p>Use the {@link GameObject#onQuit(Consumer)} method inside
 * {@link Feature}s to safely get all users of the game when the game ends
 * or your feature is deactivated.
 */
@Value
@Accessors(fluent = true)
@EqualsAndHashCode(callSuper = true)
public class QuitGameEvent extends UserEvent {

    @Getter
    private static final HandlerList handlerList = new HandlerList();

    GameSession session;

    public QuitGameEvent(GameSession session, User user) {
        super(user);
        this.session = session;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlerList;
    }
}
