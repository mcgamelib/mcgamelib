package net.silthus.mcgamelib;

import org.bukkit.entity.Player;

import java.util.Collection;

public interface Feature {

    Phase phase();

    default GameSession session() {
        return phase().session();
    }

    default Game game() {
        return session().game();
    }

    default Collection<Player> players() {

        return session().players();
    }

    void enable();

    void disable();
}
