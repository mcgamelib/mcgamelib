package net.silthus.mcgamelib;

import net.silthus.mcgamelib.modes.TestPhase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SimpleGameSessionTest extends TestBase {

    @Override
    @BeforeEach
    void setUp() {
        super.setUp();
        gameManager().phases().register(TestPhase.class);
    }

    @Test
    @DisplayName("should create and initialize the game session")
    void shouldCreateAndInitializeTheGameSession() {

        GameSession session = game().createSession();

        assertThat(session.initialized()).isTrue();
    }

    @Test
    @DisplayName("should create and initialize the phases")
    void shouldCreateInstancesOfPhases() {

        GameSession session = new SimpleGame(gameManager(),
                GameDefinition.builder().name("test")
                        .addPhase(TestPhase.class)
                        .build(),
                new GameConfig()
        ).createSession();

        assertThat(session.phases())
                .hasSize(1)
                .hasOnlyElementsOfTypes(TestPhase.class)
                .allMatch(Phase::initialized);
    }

    @Test
    @DisplayName("should cache new game sessions in the game session list")
    void shouldStoreTheNewGameSessionInTheGameList() {

        Game game = game();
        GameSession session = game.createSession();

        assertThat(game.allSessions()).contains(session);
    }
}