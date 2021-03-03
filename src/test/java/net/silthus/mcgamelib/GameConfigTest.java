package net.silthus.mcgamelib;

import org.bukkit.configuration.MemoryConfiguration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GameConfigTest extends TestBase {

    @Test
    @DisplayName("should get feature config inside phase config")
    void shouldGetNestedFeaturePathOfPhase() {

        MemoryConfiguration config = new MemoryConfiguration();
        config.set("phases.test.features.dummy.test", "foobar");
        GameConfig gameConfig = new GameConfig(config);

        Feature feature = mock(Feature.class);
        when(feature.name()).thenReturn("dummy");
        Phase phase = mockPhase("test");

        assertThat(gameConfig.getFeatureConfig(phase, feature))
                .extracting(configurationSection -> configurationSection.getString("test"))
                .isEqualTo("foobar");
    }

    @Test
    @DisplayName("should extract feature config if phase is null")
    void shouldExtractFeatureConfigIfPHaseIsNull() {

        MemoryConfiguration config = new MemoryConfiguration();
        config.set("features.dummy.test", "foobar");
        GameConfig gameConfig = new GameConfig(config);

        Feature feature = mock(Feature.class);
        when(feature.name()).thenReturn("dummy");

        assertThat(gameConfig.getFeatureConfig(null, feature))
                .extracting(configurationSection -> configurationSection.getString("test"))
                .isEqualTo("foobar");
    }
}