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
     * A game can only be created from a definition that defines the various phases and settings.
     * <p>The game takes this {@link GameDefinition} and customizes it using the {@link GameConfig}.
     * The result is a {@link Game} that can be played multiple times by creating individual {@link GameSession}s.
     *
     * @return the game definition this game is based off
     */
    GameDefinition definition();

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
     * Creates a new game session for this game and initializes it.
     * <p>Use {@link GameSession#start()} to start the first phase of the game.
     * <p>A game can be created as often as needed but care should be taken to fill existing
     * {@link #sessions()} before creating another game.
     *
     * @return the game session created for this game
     * @throws InitializationException if the game session fails to initialize.
     *         this may happen if a phase or feature has missing config values
     *         or if an error occurred when initializing the game.
     */
    GameSession createSession() throws InitializationException;
}
