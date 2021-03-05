package net.silthus.mcgamelib;

import net.silthus.mcgamelib.modes.TestPhase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@SuppressWarnings("ALL")
class PhaseRegistryTest extends TestBase {

    private PhaseRegistry registry;

    @BeforeEach
    void setUp() {

        super.setUp();
        registry = new PhaseRegistry();
    }

    @Test
    @DisplayName("should successfully register phase")
    void shouldRegisterFeature() {

        registry.register(TestPhase.class);

        assertThat(registry.get(TestPhase.class))
                .isPresent()
                .get()
                .extracting(factory -> factory.create(gameSession()))
                .isNotNull();
    }

    @Test
    @DisplayName("should allow creation with custom supplier")
    void shouldOverwriteConfiguration() {

        registry.register(TestPhase.class, session -> (TestPhase) new TestPhase(session).duration(Duration.ofMinutes(1)));

        assertThat(registry.get(TestPhase.class))
                .isPresent()
                .get()
                .extracting(factory -> factory.create(gameSession()))
                .extracting(TestPhase::configuredDuration)
                .isEqualTo(Duration.ofMinutes(1));
    }

    @Test
    @DisplayName("should throw if phase is registered")
    void shouldThrowIfFeatureIsAlreadyRegistered() {

        registry.register(TestPhase.class);

        assertThatExceptionOfType(UnsupportedOperationException.class)
                .isThrownBy(() -> registry.register(TestPhase.class));
    }

    @Test
    @DisplayName("should return a list of all registered phases")
    void shouldReturnAListOfAllRegisteredFeatures() {

        registry.register(TestPhase.class);

        assertThat(registry.allTypes())
                .hasSize(1)
                .containsExactly(TestPhase.class);
    }

    @Test
    @DisplayName("should remove an invalid phase registration")
    void shouldThrowExceptionOnRegistrationIfClassIsInvalid() {

        registry.register(InvalidPhase.class);

        assertThatExceptionOfType(InitializationException.class)
                .isThrownBy(() -> registry.get(InvalidPhase.class).get().create(gameSession()));

        assertThat(registry.allTypes()).isEmpty();
    }

    public static class InvalidPhase extends AbstractPhase {

        public InvalidPhase(GameSession session, Object test) {
            super(session);
        }

        @Override
        protected void configure() {

        }
    }
}