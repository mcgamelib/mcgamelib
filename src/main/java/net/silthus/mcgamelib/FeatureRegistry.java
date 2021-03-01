package net.silthus.mcgamelib;

import lombok.extern.java.Log;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

@Log(topic = "MCGameLib")
public final class FeatureRegistry {

    private final Map<Class<? extends Feature>, Function<GameSession, ? extends Feature>> features = new HashMap<>();

    /**
     * @return an immutable set of all registered feature types
     */
    public Set<Class<? extends Feature>> allTypes() {

        return Set.copyOf(features.keySet());
    }

    /**
     * Registers the feature using a public constructor that accepts a {@link GameSession} as its only argument.
     * <p>Use the {@link #register(Class, Function)} to supply your custom function that creates new instances of the feature.
     *
     * @param featureClass the class of the feature
     * @param <TFeature> the type of the feature
     * @return this feature registry
     * @throws UnsupportedOperationException if the feature class is already registered
     * @throws RuntimeException when a new instance of the feature is created and the creation fails
     */
    public <TFeature extends Feature> FeatureRegistry register(Class<TFeature> featureClass) {

        return register(featureClass, phase -> {
            try {
                final Constructor<TFeature> constructor = featureClass.getConstructor(GameSession.class);
                constructor.setAccessible(true);
                return constructor.newInstance(phase);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                log.severe("failed to create instance of feature: " + featureClass.getCanonicalName());
                log.severe("removing it from the registry...");
                features.remove(featureClass);
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Registers a new feature using the given supplier.
     * <p>Use the {@link #register(Class)} method to automatically detect a valid constructor.
     *
     * @param featureClass the class of the feature that is registered
     * @param supplier the supplier for the feature
     * @param <TFeature> the type of the feature
     * @return this feature registry
     * @throws UnsupportedOperationException if the feature class is already registered
     */
    public <TFeature extends Feature> FeatureRegistry register(Class<TFeature> featureClass, Function<GameSession, TFeature> supplier) {

        if (features.containsKey(featureClass)) {
            throw new UnsupportedOperationException("The feature is already registered: " + featureClass.getCanonicalName());
        }

        features.put(featureClass, supplier);
        log.fine("registered feature: " + featureClass.getCanonicalName());

        return this;
    }

    /**
     * Tries to find a registered feature for the given class.
     * <p>Only the exact match may return the given feature instance.
     *
     * @param session the game session that is creating the feature
     * @param featureClass the class of the feature
     * @param <TFeature> the type of the feature
     * @return a new instance of the feature scope to the given game session
     */
    public <TFeature extends Feature> Optional<TFeature> get(GameSession session, Class<TFeature> featureClass) {

        return Optional.ofNullable(featureClass.cast(features.get(featureClass).apply(session)));
    }
}
