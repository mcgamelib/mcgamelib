package net.silthus.mcgamelib;

/**
 * Every {@link GameSession} has a GameState based
 * on the current state of the game.
 */
public enum GameState {

    NOT_INITIALIZED,
    NOT_STARTED,
    LOADING,
    ACTIVE,
    ENDED
}
