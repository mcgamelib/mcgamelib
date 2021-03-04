package net.silthus.mcgamelib;

import org.jetbrains.annotations.Nullable;

import java.time.Duration;
import java.time.Instant;
import java.util.Collection;
import java.util.Optional;
import java.util.function.Consumer;

public interface Phase extends GameObject {

    /**
     * @return the name of the phase used in config paths and for reference
     */
    String name();

    /**
     * The actual duration of the phase where it was active.
     * <p>The duration may still be changing if the phase is {@link #active()}.
     * Only after the phase {@link #ended()} the duration will be a constant value.
     *
     * @return the duration of the phase
     */
    Duration duration();

    /**
     * Sets the default duration of this phase.
     * <p>The duration can always be overwritten by the config.
     * <p>This does not affect the time that has already passed and only
     * represents the duration this phase should have.
     * <p>Set it to {@link Duration#ZERO} to disable the automatic ending.
     * <p>Updating the duration when a phase is {@link #active()} may result
     * in an immediate ending of the phase if the new duration is short
     * than the passed {@link #duration()}.
     *
     * @param duration the default duration to set for this phase
     * @return this phase
     */
    Phase duration(Duration duration);

    /**
     * The duration of the phase determines the time period
     * after which the {@link #end()} method is automatically called.
     * <p>A phase may be ended before the configured duration is over.
     * <p>A configured duration that is equals to {@link Duration#ZERO}
     * represents a phase that has no duration and must be ended manually.
     *
     * @return the configured duration of the phase
     */
    Duration configuredDuration();

    /**
     * The remaining duration determines the time until the phase
     * automatically ends.
     * <p>The phase can always be ended before the end of its duration
     * by simply calling the {@link #end()} method.
     *
     * @return the remaining duration until the phase ends
     */
    default Duration remainingDuration() {

        return configuredDuration().minus(duration());
    }

    /**
     * @return the time the phase was started.
     *         may be null if the phase has not started yet.
     */
    @Nullable Instant startTime();

    /**
     * @return the time the phase has ended.
     *         may be null if the phase has not ended yet.
     */
    @Nullable Instant endTime();

    /**
     * @return true if the phase started and is active.
     *         false if it has not started or ended.
     */
    default boolean active() {

        return startTime() != null && endTime() == null;
    }

    /**
     * @return true if the phase ended.
     *         false if the phase did not start or end yet.
     */
    default boolean ended() {

        return startTime() != null && endTime() != null;
    }

    /**
     * Gets all features that are associated with this phase.
     *
     * @return an immutable list of features for this phase
     */
    Collection<Feature> features();

    /**
     * Gets the feature of the given type in this phase.
     * <p>Returns null if the feature does not exist.
     * You can also use the {@link #optionalFeature(Class)} method to safely
     * get a feature in this phase using an {@link Optional}.
     *
     * @param featureClass the class of the feature to get
     * @param <TFeature> the type of the feature
     * @return the feature or null if it does not exist
     */
    <TFeature extends Feature> TFeature feature(Class<TFeature> featureClass);

    /**
     * Tries to get a feature of the given type in this phase.
     * <p>Returns an empty optional if no feature of the given type exists.
     * Use the {@link #feature(Class)} method to get a nullable feature.
     *
     * @param featureClass the class of the feature to get
     * @param <TFeature> the type of the feature
     * @return the feature or an empty optional if it does not exist
     */
    <TFeature extends Feature> Optional<TFeature> optionalFeature(Class<TFeature> featureClass);

    /**
         * Adds the given feature to this phase without an explicit configuration.
         *
         * @param feature the class of the feature that is added to this phase
         * @param <TFeature> the type of the feature
         * @return this phase instance
         */
    <TFeature extends Feature> Phase addFeature(Class<TFeature> feature);

    /**
         * Adds the given feature to this phase with a custom configuration.
         *
         * @param feature the class of the feature that is added to this phase
         * @param <TFeature> the type of the feature
         * @param config the configuration callback to configure the feature
         * @return this phase instance
         */
    <TFeature extends Feature> Phase addFeature(Class<TFeature> feature, Consumer<TFeature> config);

    /**
     * Initializes the phase adding all built-in features and loading config values.
     * <p>The init method is always called before the start of the phase.
     * <p>Use it after creating a new instance of the phase to load the configuration.
     */
    void init();

    /**
     * Starts this phase and activates all features in it.
     * <p>Any active phase will be ended before starting a this new phase.
     */
    void start();

    /**
     * Ends this phase deactivating all features in it.
     */
    void end();
}
