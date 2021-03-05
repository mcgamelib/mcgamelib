package net.silthus.mcgamelib;

import net.silthus.mcgamelib.modes.TestPhase;
import org.bukkit.configuration.MemoryConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

class PhaseFactoryTest extends TestBase {

    private PhaseFactory<TestPhase> factory;

    @BeforeEach
    void setUp() {

        super.setUp();
        factory = new PhaseFactory<>(TestPhase.class, TestPhase::new);
    }

    @Test
    @DisplayName("should create phase with default values and name")
    void shouldCreateFeatureWithDefaultValues() {

        assertThat(factory.create(gameSession()))
                .isNotNull()
                .extracting(Phase::name)
                .isEqualTo("test");
    }

    @Test
    @DisplayName("should use values from game config")
    void shouldUseValuesFromConfig() {

        MemoryConfiguration config = new MemoryConfiguration();
        config.set("phases.test.duration", "PT10m");
        GameSession session = gameSession(config);

        assertThat(factory.create(session))
                .isNotNull()
                .extracting(Phase::name, Phase::configuredDuration)
                .contains("test", Duration.ofMinutes(10));
    }
}