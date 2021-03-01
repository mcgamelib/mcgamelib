package net.silthus.mcgamelib;

import net.silthus.mcgamelib.features.MaxHealthFeature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.mock;

class FeatureRegistryTest {

    private FeatureRegistry registry;

    @BeforeEach
    void setUp() {

        registry = new FeatureRegistry();
    }

    @Test
    @DisplayName("should successfully register feature")
    void shouldRegisterFeature() {

        registry.register(MaxHealthFeature.class);

        assertThat(registry.get(mock(GameSession.class), MaxHealthFeature.class))
                .isPresent()
                .get()
                .isInstanceOf(MaxHealthFeature.class)
                .extracting(MaxHealthFeature::maxHealth)
                .isEqualTo(20d);
    }

    @Test
    @DisplayName("should allow creation with custom supplier")
    void shouldOverwriteConfiguration() {

        registry.register(MaxHealthFeature.class, session -> new MaxHealthFeature(session).maxHealth(30));

        assertThat(registry.get(mock(GameSession.class), MaxHealthFeature.class))
                .isPresent()
                .get()
                .isInstanceOf(MaxHealthFeature.class)
                .extracting(MaxHealthFeature::maxHealth)
                .isEqualTo(30d);
    }

    @Test
    @DisplayName("should throw if feature is registered")
    void shouldThrowIfFeatureIsAlreadyRegistered() {

        registry.register(MaxHealthFeature.class);

        assertThatExceptionOfType(UnsupportedOperationException.class)
                .isThrownBy(() -> registry.register(MaxHealthFeature.class));
    }

    @Test
    @DisplayName("should return a list of all registered features")
    void shouldReturnAListOfAllRegisteredFeatures() {

        registry.register(MaxHealthFeature.class);

        assertThat(registry.allTypes())
                .hasSize(1)
                .containsExactly(MaxHealthFeature.class);
    }

    @Test
    @DisplayName("should throw an exception when creating an invalid feature")
    void shouldThrowExceptionOnRegistrationIfClassIsInvalid() {

        registry.register(InvalidFeature.class);

        assertThatExceptionOfType(Exception.class)
                .isThrownBy(() -> registry.get(mock(GameSession.class), InvalidFeature.class));
        assertThat(registry.allTypes()).isEmpty();
    }

    public static class InvalidFeature extends AbstractFeature {

        public InvalidFeature(GameSession session, Object test) {
            super(session);
        }

        @Override
        public void enable() {

        }

        @Override
        public void disable() {

        }
    }
}