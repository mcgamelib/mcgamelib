package net.silthus.mcgamelib.events.game;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.silthus.mcgamelib.Game;
import net.silthus.mcgamelib.events.MCGameLibEvent;

/**
 * Parent class for game events
 */
@Data
@EqualsAndHashCode(callSuper = true)
public abstract class GameEvent extends MCGameLibEvent {

    private final Game game;
}
