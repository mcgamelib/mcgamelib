package net.silthus.mcgamelib;

import java.util.UUID;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestBase {

    public GameSession mockGameSession() {

        GameSession session = mock(GameSession.class);
        UUID randomUUID = UUID.randomUUID();
        when(session.id()).thenReturn(randomUUID);
        Game game = mockGame();
        when(session.game()).thenReturn(game);
        return session;
    }

    public Game mockGame() {

        Game game = mock(Game.class);
        GameConfig gameConfig = new GameConfig();
        when(game.config()).thenReturn(gameConfig);
        return game;
    }

    public Phase mockPhase(String name) {

        Phase phase = mock(Phase.class);
        when(phase.name()).thenReturn(name);
        UUID randomUUID = UUID.randomUUID();
        when(phase.id()).thenReturn(randomUUID);
        GameSession gameSession = mockGameSession();
        when(phase.session()).thenReturn(gameSession);

        return phase;
    }
}
