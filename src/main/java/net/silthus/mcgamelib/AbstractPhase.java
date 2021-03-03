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
import java.util.*;
import java.util.function.Consumer;

@Getter
@ToString(of = "info")
@Accessors(fluent = true)
@EqualsAndHashCode(callSuper = true)
@Log(topic = "MCGameLib")
public abstract class AbstractPhase extends AbstractGameObject implements Phase {

    private final UUID id = UUID.randomUUID();
    private final PhaseInfo info;
    private final HashMap<Class<?>, Feature> features = new HashMap<>();

    @ConfigOption(description = {
            "The duration of the phase in ISO-8601 format.",
            "The time format must start with a P and then be followed by a duration of D H M S.",
            "For example: P5d4h3m2s.001 is parsed as a duration of 5 days 4 hours 3 minutes 2.001 seconds"
    })
    private final String duration = null;
    private Duration configuredDuration = Duration.ZERO;
    private Instant startTime;
    private Instant endTime;

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

    /**
     * Sets the default duration of this phase.
     * <p>The duration can always be overwritten by the config.
     *
     * @param duration the default duration to set for this phase
     * @return this phase
     */
    protected AbstractPhase duration(Duration duration) {

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

    /**
         * Adds the given feature to this phase without an explicit configuration.
         *
         * @param feature the class of the feature that is added to this phase
         * @param <TFeature> the type of the feature
         * @return this phase instance
         */
    protected final <TFeature extends Feature> Phase addFeature(Class<TFeature> feature) {

        return addFeature(feature, tFeature -> {});
    }

    /**
         * Adds the given feature to this phase with a custom configuration.
         *
         * @param feature the class of the feature that is added to this phase
         * @param <TFeature> the type of the feature
         * @param config the configuration callback to configure the feature
         * @return this phase instance
         */
    protected final <TFeature extends Feature> Phase addFeature(Class<TFeature> feature, Consumer<TFeature> config) {

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
    public final void init() {

        configure();

        if (!Strings.isNullOrEmpty(duration)) {
            Duration duration = Duration.parse(this.duration);
            if (!duration().isZero() && !duration.isNegative()) {
                configuredDuration = duration;
            }
        }
    }

    /**
     * Configures the phase adding all built-in features and loading config values.
     * <p>The init method is always called before the start of the phase.
     * <p>Overwrite it to add the features of this phase, configure the duration and so on.
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
