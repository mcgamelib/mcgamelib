package net.silthus.mcgamelib;

import net.silthus.mcgamelib.events.player.JoinGameEvent;
import net.silthus.mcgamelib.events.player.QuitGameEvent;
import net.silthus.mcgamelib.events.player.SpectateGameEvent;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * A game session is created from a game and keeps track of the current game progress.
 * <p>Multiple game sessions may be created per {@link Game} and each get their own
 * instances of the configured {@link Phase}s and {@link Feature}s.
 */
public interface GameSession {

    /**
     * @return the unique identifier of the game session
     */
    UUID id();

    /**
     * @return the game this game session belongs to
     */
    Game game();

    /**
     * @return the state of the current game session
     */
    GameState state();

    /**
     * @return true if the game session is running
     */
    default boolean running() {
        return state() == GameState.RUNNING;
    }

    /**
     * @return true if the game session already ended
     */
    default boolean ended() {
        return state() == GameState.ENDED;
    }

    /**
     * Requests the given user to join this game session.
     *
     * @param user the user that wants to join the game
     * @return the result of the join attempt
     */
    JoinResult join(User user);

    /**
     * Attempts to spectate this game session.
     *
     * @param user the user that wants to spectate the game
     * @return the result of the spectate attempt
     */
    JoinResult spectate(User user);

    /**
     * Signals the game session that the given user wants to stop spectating or playing.
     *
     * @param user the user that leaves the game
     * @return true if the user was in the game.
     *         false if the user was neither spectating nor playing the game.
     */
    boolean leave(User user);

    /**
     * Gets a list of active users in this game session.
     * <p>The list will only contain users that are playing
     * and exclude spectators. Use the {@link #users(boolean)} method
     * to get all users including spectators.
     *
     * @return a list of users that are in this game session
     */
    default Collection<User> users() {

        return users(false);
    }

    /**
     * Gets a list of users that are in the game.
     * <p>Allows filtering for to exclude or include
     * spectators in the result.
     *
     * @param includeSpectators true if spectators should be included in the result
     * @return a list of users in the game
     */
    Collection<User> users(boolean includeSpectators);

    /**
     * Gets a list of players that are actively playing the game.
     * <p>The list excludes any players that are only spectating.
     *
     * @return a list of players playing the game
     */
    default Collection<Player> players() {

        return players(false);
    }

    /**
     * Gets a list of players that are in the game.
     * <p>Allows filtering for to exclude or include
     * spectators in the result.
     *
     * @param includeSpectators true if spectators should be included in the result
     * @return a list of players in the game
     */
    default Collection<Player> players(boolean includeSpectators) {

        return users(includeSpectators).stream()
                .map(User::player)
                .flatMap(Optional::stream)
                .collect(Collectors.toList());
    }

    /**
     * Use the onJoin hook as an alternative to the {@link JoinGameEvent}.
     * <p>The difference to the event is that the provided callback will
     * also be called for every player currently in the game after the
     * {@link Phase} or {@link Feature} has been activated.
     *
     * @param gameObject the game object that registered the listener
     * @param consumer the consumer that is called when a user joined or the game object was activated
     */
    void onJoin(GameObject gameObject, Consumer<User> consumer);

    /**
     * Use the onJoin hook as an alternative to the {@link JoinGameEvent}.
     * <p>The difference to the event is that the provided callback will
     * also be called for every player currently in the game after the
     * {@link Phase} or {@link Feature} has been activated.
     * <p>The difference to the {@link #onJoin(GameObject, Consumer)} hook is that
     * this method already resolved the {@link Player} and is only called if it exists.
     *
     * @param gameObject the game object that registered the listener
     * @param consumer the consumer that is called when a player joined or the game object was activated
     */
    void onPlayerJoin(GameObject gameObject, Consumer<Player> consumer);

    /**
     * Use the onQuit hook as an alternative to the {@link QuitGameEvent}.
     * <p>The difference to the event is that the provided callback will
     * also be called for every player currently in the game after the
     * {@link Phase} or {@link Feature} has been deactivated.
     *
     * @param gameObject the game object that registered the listener
     * @param consumer the consumer that is called when a user quits or the game object was activated
     */
    void onQuit(GameObject gameObject, Consumer<User> consumer);

    /**
     * Use the onQuit hook as an alternative to the {@link QuitGameEvent}.
     * <p>The difference to the event is that the provided callback will
     * also be called for every player currently in the game after the
     * {@link Phase} or {@link Feature} has been deactivated.
     * <p>The difference to the {@link #onJoin(GameObject, Consumer)} hook is that
     * this method already resolved the {@link Player} and is only called if it exists.
     *
     * @param gameObject the game object that registered the listener
     * @param consumer the consumer that is called when a player quits or the game object was activated
     */
    void onPlayerQuit(GameObject gameObject, Consumer<Player> consumer);

    /**
     * Use the onSpectate hook as an alternative to the {@link SpectateGameEvent}.
     * <p>The difference to the event is that the provided callback will
     * also be called for every player currently spectating the game after the
     * {@link Phase} or {@link Feature} has been activated.
     *
     * @param gameObject the game object that registered the listener
     * @param consumer the consumer that is called when a user starts spectating or the game object was activated
     */
    void onSpectate(GameObject gameObject, Consumer<User> consumer);

    /**
     * Use the onSpectate hook as an alternative to the {@link SpectateGameEvent}.
     * <p>The difference to the event is that the provided callback will
     * also be called for every player currently spectating the game after the
     * {@link Phase} or {@link Feature} has been activated.
     * <p>The difference to the {@link #onSpectate(GameObject, Consumer)} hook is that
     * this method already resolved the {@link Player} and is only called if it exists.
     *
     * @param gameObject the game object that registered the listener
     * @param consumer the consumer that is called when a player starts spectating or the game object was activated
     */
    void onPlayerSpectate(GameObject gameObject, Consumer<Player> consumer);

    /**
     * The join result is returned when an user attempts to
     * either {@link #spectate(User)} or {@link #join(User)} a game.
     */
    enum JoinResult {

        /**
         * The user was successfully joined and is now playing or spectating the game.
         */
        SUCCESS,
        /**
         * The game already ended and cannot be spectated or joined anymore.
         */
        GAME_ENDED,
        /**
         * The game is full and cannot be joined.
         * <p>Spectating may be possible.
         */
        GAME_FULL,
        /**
         * The game has not started and cannot be joined or spectated.
         */
        NOT_STARTED,
        /**
         * The game session is loading and the game cannot be joined at this moment.
         */
        LOADING;

        /**
         * @return true if the join was successful
         */
        public boolean success() {

            return this == SUCCESS;
        }

        /**
         * @return true if the join attempt failed
         */
        public boolean failure() {

            return !success();
        }
    }
}
