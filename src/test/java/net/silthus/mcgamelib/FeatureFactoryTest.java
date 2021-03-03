package net.silthus.mcgamelib;

import net.silthus.mcgamelib.features.MaxHealthFeature;
import org.bukkit.configuration.MemoryConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class FeatureFactoryTest extends TestBase {

    private FeatureFactory<MaxHealthFeature> factory;

    @BeforeEach
    void setUp() {

        factory = new FeatureFactory<>(MaxHealthFeature.class, MaxHealthFeature::new);
    }

    @Test
    @DisplayName("should create feature with default values and name")
    void shouldCreateFeatureWithDefaultValues() {

        assertThat(factory.create(mockGameSession()))
                .isNotNull()
                .extracting(AbstractFeature::name, MaxHealthFeature::maxHealth)
                .contains("max-health", 20d);
    }

    @Test
    @DisplayName("should use values from game config")
    void shouldUseValuesFromConfig() {

        GameSession session = mockGameSession();
        MemoryConfiguration config = new MemoryConfiguration();
        config.set("features.max-health.max_health", 10d);
        when(session.game().config()).thenReturn(new GameConfig(config));

        assertThat(factory.create(session))
                .isNotNull()
                .extracting(AbstractFeature::name, MaxHealthFeature::maxHealth)
                .contains("max-health", 10d);
    }

    @Test
    @DisplayName("should use values from phase config")
    void shouldUseValuesFromPhaseConfig() {

        Phase test = mockPhase("test");
        MemoryConfiguration config = new MemoryConfiguration();
        config.set("phases.test.features.max-health.max_health", 10d);
        when(test.session().game().config()).thenReturn(new GameConfig(config));

        assertThat(factory.create(test))
                .isNotNull()
                .extracting(AbstractFeature::name, MaxHealthFeature::maxHealth)
                .contains("max-health", 10d);
    }
}