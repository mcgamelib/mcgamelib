package net.silthus.mcgamelib;

import net.silthus.mcgamelib.features.MaxHealthFeature;
import net.silthus.mcgamelib.modes.TestPhase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.Mockito.mock;

class SimpleGameSessionTest extends TestBase {

    @Test
    @DisplayName("should create an instance of the configured phase")
    void shouldCreateInstancesOfPhases() {

        GameManager gameManager = new GameManager(mock(MCGameLib.class));
        gameManager.load();

        gameManager.phases().register(TestPhase.class);
        gameManager.features().register(MaxHealthFeature.class);

        GameSession session = new SimpleGame(gameManager,
                GameDefinition.builder().name("test")
                        .phase(TestPhase.class)
                        .build(),
                new GameConfig()
        ).createSession();


        assertThatCode(session::initialize)
                .doesNotThrowAnyException();

        assertThat(session.phases())
                .hasSize(1)
                .hasOnlyElementsOfTypes(TestPhase.class);
    }
}