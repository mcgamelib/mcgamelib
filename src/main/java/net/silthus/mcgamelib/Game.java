package net.silthus.mcgamelib;

import java.util.Collection;
import java.util.UUID;

/**
 * The game represents a configured instance of a game mode and contains all of the logic.
 * <p>Each game may start multiple sessions during its lifecycle.
 * The {@link GameSession} holds all of the relevant state for a game that is currently running.
 * <p>
 * It is composed of at least one {@link Phase} and may be created multiple times for each play of the game.
 */
public interface Game {

    /**
     * @return the unique identifier of this game
     */
    UUID id();

    /**
     * @return the game manager that creates this game
     */
    GameManager gameManager();

    /**
     * Every game mode is configured with a game config and creates its unique game from that.
     * <p>Use the game config to get the config data of the game, phase or features.
     *
     * @return the config of this game
     */
    GameConfig config();

    /**
     * Gets all active sessions of this game instance.
     * <p>Use the {@link #allSessions()} method to get all sessions
     * including sessions that already ended.
     *
     * @return a list of active (not started or running) game sessions
     */
    Collection<GameSession> sessions();

    /**
     * @return a list of present and past game sessions regardless of their state
     */
    Collection<GameSession> allSessions();

    /**
     * Starts a new {@link GameSession} for this game.
     * <p>Starting a game will load the map and enable the first {@link Phase}.
     * <p>A game can be started as often as needed but care should be taken to fill existing
     * {@link #sessions()} before starting another game.
     *
     * @return the game session created for this game
     */
    GameSession startNewGameSession();
}
