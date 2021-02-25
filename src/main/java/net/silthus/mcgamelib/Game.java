package net.silthus.mcgamelib;

import java.util.UUID;

/**
 * The game is the core part of this API. It defines the logic that happens during a game.
 * <p>
 * It is composed of at least one {@link Phase} and may be created multiple times for each play of the game.
 */
public interface Game {

    /**
     * @return the unique identifier of this game
     */
    UUID id();

    /**
     * @return true if the game started and a {@link #gameSession()} exists
     */
    boolean started();

    /**
     * @return true if the game is running and {@link #gameSession()} is not null
     */
    boolean running();

    /**
     * @return true if the game already ended
     */
    boolean ended();

    /**
     * The game session only exists after {@link #start()} has been called.
     *
     * @return the game session of this game.
     *         null if the game is not {@link #started()}
     */
    GameSession gameSession();

    /**
     * Starts this game and creates a new {@link GameSession} for it.
     * <p>Starting a game will load the {@link Map} and enable the first {@link Phase}.
     * <p>A game can only be started once. Any subsequent start attempts will throw an exception.
     * Make sure to check {@link #started()} before calling this if you are unsure about the state of the game.
     *
     * @return the game session created for this game
     * @throws UnsupportedOperationException if this method was already called and the game {@link #started()}
     */
    GameSession start();

    /**
     * Requests the given user to join this game.
     *
     * @param user the user that wants to join the game
     * @return the result of the join attempt
     */
    JoinResult join(User user);

    /**
     * Attempts to spectate this game.
     *
     * @param user the user that wants to spectate the game
     * @return the result of the spectate attempt
     */
    JoinResult spectate(User user);

    /**
     * Signals the game that the given user wants to stop spectating or playing.
     *
     * @param user the user that leaves the game
     * @return true if the user was in the game.
     *         false if the user was neither spectating nor playing the game.
     */
    boolean leave(User user);

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
         * The map is loading and the game cannot be joined at this moment.
         */
        MAP_LOADING;

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
