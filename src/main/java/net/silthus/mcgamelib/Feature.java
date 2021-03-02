package net.silthus.mcgamelib;

import net.silthus.mcgamelib.annotations.FeatureInfo;

/**
 * A feature is the smallest part of a game and handles a single action or state.
 * <p>Every feature must be annotated with @{@link FeatureInfo} to provide additional metadata.
 * <p>The feature must also be added to a {@link Game} or {@link Phase} to be activated.
 */
public interface Feature extends GameObject {

    /**
     * The load method is called after the config options have been
     * injected into the feature and before {@link #enable()} is called.
     * <p>Use it to load data and prepare your feature.
     */
    default void load() {}

    /**
     * The enable method is called when the phase or game is activated.
     * <p>
     */
    void enable();

    void disable();
}
