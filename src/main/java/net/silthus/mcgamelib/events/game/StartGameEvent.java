package net.silthus.mcgamelib.events.game;

import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.experimental.Accessors;
import net.silthus.mcgamelib.GameSession;

/**
 * The StartGameEvent is called when a new game starts.
 */
@Value
@Accessors(fluent = true)
@EqualsAndHashCode(callSuper = true)
public class StartGameEvent extends GameEvent {

    GameSession session;

    public StartGameEvent(GameSession session) {
        super(session.game());
        this.session = session;
    }
}
