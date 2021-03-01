package net.silthus.mcgamelib;

import org.bukkit.entity.Player;

import java.util.Collection;

/**
 * The game object is part of a game session and can access the game
 * that created it and the game session it is used in.
 * <p>Examples for game objects are {@link Phase}s and {@link Feature}s.
 */
public interface GameObject {

    /**
     * @return the game session that this phase belongs to
     */
    GameSession session();

    /**
     * @return the game that created the game session and belongs to this phase
     */
    default Game game() {
        return session().game();
    }

    /**
     * Use the game manager to access the game registry,
     * e.g. to create a new instance of a feature.
     *
     * @return the game manager of this game object
     */
    default GameManager gameManager() {

        return game().gameManager();
    }

    /**
     * Gets a list of active users in this game session.
     * <p>The list will only contain users that are playing
     * and exclude spectators.
     * Use the {@link #session()} to get all users including spectators.
     *
     * @return a list of users that are in this game session
     */
    default Collection<User> users() {

        return session().users();
    }

    /**
     * Gets a list of players that are actively playing the game.
     * <p>The list excludes any players that are only spectating.
     * Use the {@link #session()} to get all players including spectators.
     *
     * @return a list of players playing the game
     */
    default Collection<Player> players() {

        return session().players();
    }
}
