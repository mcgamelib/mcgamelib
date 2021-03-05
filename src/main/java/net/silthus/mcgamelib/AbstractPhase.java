package net.silthus.mcgamelib;

import com.google.common.base.Strings;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.extern.java.Log;
import net.silthus.configmapper.ConfigOption;
import net.silthus.configmapper.ConfigurationException;
import net.silthus.mcgamelib.annotations.PhaseInfo;
import org.bukkit.event.Listener;

import java.time.Duration;
import java.time.Instant;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

@Getter
@ToString(of = "info")
@Accessors(fluent = true)
@EqualsAndHashCode(callSuper = true)
@Log(topic = "MCGameLib")
public abstract class AbstractPhase extends AbstractGameObject implements Phase {

    private final PhaseInfo info;
    private final HashMap<Class<?>, Feature> features = new HashMap<>();

    @ConfigOption(description = {
            "The duration of the phase in ISO-8601 format.",
            "The time format must start with a PT and then be followed by a duration of D H M S.",
            "For example: PT5d4h3m2s.001 is parsed as a duration of 5 days 4 hours 3 minutes 2.001 seconds"
    })
    private String duration = null;
    private Duration configuredDuration = Duration.ZERO;
    private Instant startTime;
    private Instant endTime;
    private boolean initialized = false;

    public AbstractPhase(GameSession session) {
        super(session);
        this.info = getClass().getAnnotation(PhaseInfo.class);
    }

    @Override
    public String name() {

        return info.value();
    }

    @Override
    public Duration duration() {

        Instant startTime = startTime();
        Instant endTime = endTime();
        if (startTime == null) return Duration.ZERO;
        if (endTime == null) return Duration.between(startTime, Instant.now());

        return Duration.between(startTime, endTime);
    }

    @Override
    public Phase duration(Duration duration) {

        this.configuredDuration = duration;
        return this;
    }

    @Override
    public final Collection<Feature> features() {

        return List.copyOf(features.values());
    }

    @Override
    public final <TFeature extends Feature> TFeature feature(@NonNull Class<TFeature> featureClass) {

        return featureClass.cast(features.get(featureClass));
    }

    @Override
    public final <TFeature extends Feature> Optional<TFeature> optionalFeature(@NonNull Class<TFeature> featureClass) {

        return Optional.ofNullable(feature(featureClass));
    }

    @Override
    public final <TFeature extends Feature> Phase addFeature(Class<TFeature> feature) {

        return addFeature(feature, tFeature -> {});
    }

    @Override
    public final <TFeature extends Feature> Phase addFeature(Class<TFeature> feature, Consumer<TFeature> config) {

        gameManager().features().get(feature)
                .ifPresent(factory -> {
                    try {
                        TFeature tFeature = factory.create(this);
                        config.accept(tFeature);
                        features.put(feature, tFeature);
                    } catch (ConfigurationException e) {
                        log.severe("failed to configure feature " + feature.getCanonicalName()
                                + " in phase " + getClass().getCanonicalName() + ": " + e.getMessage());
                        e.printStackTrace();
                    }
                });

        return this;
    }

    @Override
    public final void initialize() throws InitializationException {

        if (initialized()) return;

        try {
            configure();
        } catch (Exception e) {
            throw new InitializationException("an error occurred when trying to configure phase: " + e.getMessage(), e);
        }

        if (!Strings.isNullOrEmpty(duration)) {
            try {
                Duration duration = Duration.parse(this.duration);
                if (!duration.isZero() && !duration.isNegative()) {
                    configuredDuration = duration;
                }
            } catch (Exception e) {
                throw new InitializationException("failed to parse duration \"" + this.duration + "\": " + e.getMessage(), e);
            }
        }

        this.initialized = true;
    }

    /**
     * Configures the phase adding all built-in features and loading config values.
     * <p>The init method is always called before the start of the phase.
     * <p>Overwrite it to add the features of this phase, configure the duration and so on.
     * <p>You can safely throw any exception to indicate a failed configured.
     */
    protected abstract void configure();

    @Override
    public final void start() {

        startTime = Instant.now();

        for (Feature feature : features.values()) {
            feature.enable();
            if (feature instanceof Listener) {
                gameManager().eventHandler().registerEvents((Listener) feature, session());
            }
        }
        onStart();
    }

    /**
     * onStart is called after all features in this phase have been enabled.
     * <p>Overwrite it to do phase specific actions that do not fit into a feature.
     */
    protected void onStart() {}

    @Override
    public final void end() {

        endTime = Instant.now();

        onEnd();
        for (Feature feature : features.values()) {
            if (feature instanceof Listener) {
                gameManager().eventHandler().unregister((Listener) feature, session());
            }
            feature.disable();
        }
    }

    /**
     * onEnd is called right before the features of this phase are disabled.
     * <p>Overwrite it to do phase specific cleanup tasks that do not fit into a feature.
     */
    protected void onEnd() {}
}
