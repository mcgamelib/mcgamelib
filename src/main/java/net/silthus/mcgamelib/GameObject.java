package net.silthus.mcgamelib;

import net.silthus.mcgamelib.events.player.JoinGameEvent;
import net.silthus.mcgamelib.events.player.QuitGameEvent;
import net.silthus.mcgamelib.events.player.SpectateGameEvent;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.UUID;
import java.util.function.Consumer;

/**
 * The game object is part of a game session and can access the game
 * that created it and the game session it is used in.
 * <p>Examples for game objects are {@link Phase}s and {@link Feature}s.
 */
public interface GameObject {

    /**
     * @return the unique identifier of this game object
     */
    UUID id();

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

    /**
     * Use the onJoin hook as an alternative to the {@link JoinGameEvent}.
     * <p>The difference to the event is that the provided callback will
     * also be called for every player currently in the game after the
     * {@link Phase} or {@link Feature} has been activated.
     *
     * @param consumer the consumer that is called when a user joined or the game object was activated
     */
    default void onJoin(Consumer<User> consumer) {

        session().onJoin(this, consumer);
    }

    /**
     * Use the onJoin hook as an alternative to the {@link JoinGameEvent}.
     * <p>The difference to the event is that the provided callback will
     * also be called for every player currently in the game after the
     * {@link Phase} or {@link Feature} has been activated.
     * <p>The difference to the {@link #onJoin(Consumer)} hook is that
     * this method already resolved the {@link Player} and is only called if it exists.
     *
     * @param consumer the consumer that is called when a player joined or the game object was activated
     */
    default void onPlayerJoin(Consumer<Player> consumer) {

        session().onPlayerJoin(this, consumer);
    }

    /**
     * Use the onQuit hook as an alternative to the {@link QuitGameEvent}.
     * <p>The difference to the event is that the provided callback will
     * also be called for every player currently in the game after the
     * {@link Phase} or {@link Feature} has been deactivated.
     *
     * @param consumer the consumer that is called when a user quits or the game object was activated
     */
    default void onQuit(Consumer<User> consumer) {

        session().onQuit(this, consumer);
    }

    /**
     * Use the onQuit hook as an alternative to the {@link QuitGameEvent}.
     * <p>The difference to the event is that the provided callback will
     * also be called for every player currently in the game after the
     * {@link Phase} or {@link Feature} has been deactivated.
     * <p>The difference to the {@link #onJoin(Consumer)} hook is that
     * this method already resolved the {@link Player} and is only called if it exists.
     *
     * @param consumer the consumer that is called when a player quits or the game object was activated
     */
    default void onPlayerQuit(Consumer<Player> consumer) {

        session().onPlayerQuit(this, consumer);
    }

    /**
     * Use the onSpectate hook as an alternative to the {@link SpectateGameEvent}.
     * <p>The difference to the event is that the provided callback will
     * also be called for every player currently spectating the game after the
     * {@link Phase} or {@link Feature} has been activated.
     *
     * @param consumer the consumer that is called when a user starts spectating or the game object was activated
     */
    default void onSpectate(Consumer<User> consumer) {

        session().onSpectate(this, consumer);
    }

    /**
     * Use the onSpectate hook as an alternative to the {@link SpectateGameEvent}.
     * <p>The difference to the event is that the provided callback will
     * also be called for every player currently spectating the game after the
     * {@link Phase} or {@link Feature} has been activated.
     * <p>The difference to the {@link #onSpectate(Consumer)} hook is that
     * this method already resolved the {@link Player} and is only called if it exists.
     *
     * @param consumer the consumer that is called when a player starts spectating or the game object was activated
     */
    default void onPlayerSpectate(Consumer<Player> consumer) {

        session().onPlayerSpectate(this, consumer);
    }
}
