package net.silthus.mcgamelib;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.apache.commons.lang.RandomStringUtils;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemoryConfiguration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.util.UUID;

import static org.mockito.Mockito.*;

@Accessors(fluent = true)
public class TestBase {

    private ServerMock server;
    private MCGameLib plugin;
    @Getter
    private GameManager gameManager;

    @BeforeEach
    void setUp() {

//        server = MockBukkit.mock();
//        plugin = MockBukkit.load(MCGameLib.class);
        gameManager = new GameManager(mock(MCGameLib.class));
        gameManager.initialize();
    }

    @AfterEach
    void tearDown() {

        MockBukkit.unmock();
    }

    public GameSession gameSession(ConfigurationSection config) {

        return spy(new SimpleGameSession(game(config)));
    }

    public GameSession gameSession() {

        return gameSession(new MemoryConfiguration());
    }

    public Game game() {

        return game(new MemoryConfiguration());
    }

    public Game game(ConfigurationSection config) {

        GameDefinition definition = GameDefinition.builder().name(RandomStringUtils.randomAlphanumeric(20)).build();
        return new SimpleGame(
                gameManager,
                definition,
                new GameConfig(config)
        );
    }

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
