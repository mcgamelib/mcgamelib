package net.silthus.mcgamelib;

import java.util.Collection;
import java.util.Optional;
import java.util.function.Consumer;

public interface Phase extends GameObject {

    /**
     * Gets all features that are associated with this phase.
     *
     * @return an immutable list of features for this phase
     */
    Collection<Feature> features();

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
     * Configures this phase adding all built-in features and loading config values.
     * <p>The configure method is always called before the start of the phase.
     */
    void configure();

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
