package net.silthus.mcgamelib;

import net.silthus.configmapper.ConfigurationException;
import net.silthus.mcgamelib.features.MaxHealthFeature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.net.ConnectException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.mock;

@SuppressWarnings("ALL")
class FeatureRegistryTest extends TestBase {

    private FeatureRegistry registry;

    @BeforeEach
    void setUp() {

        registry = new FeatureRegistry();
    }

    @Test
    @DisplayName("should successfully register feature")
    void shouldRegisterFeature() {

        registry.register(MaxHealthFeature.class);

        assertThat(registry.get(MaxHealthFeature.class))
                .isPresent()
                .get()
                .extracting(factory -> factory.create(mockGameSession()))
                .extracting(MaxHealthFeature::maxHealth)
                .isEqualTo(20d);
    }

    @Test
    @DisplayName("should allow creation with custom supplier")
    void shouldOverwriteConfiguration() {

        registry.register(MaxHealthFeature.class, session -> new MaxHealthFeature(session).maxHealth(30));

        assertThat(registry.get(MaxHealthFeature.class))
                .isPresent()
                .get()
                .extracting(factory -> factory.create(mockGameSession()))
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
    @DisplayName("should remove an invalid feature registration")
    void shouldThrowExceptionOnRegistrationIfClassIsInvalid() {

        registry.register(InvalidFeature.class);

        assertThatExceptionOfType(ConfigurationException.class)
                .isThrownBy(() -> registry.get(InvalidFeature.class).get().create(mockGameSession()));

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