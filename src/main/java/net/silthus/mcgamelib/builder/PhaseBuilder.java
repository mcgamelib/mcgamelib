package net.silthus.mcgamelib.builder;

import net.silthus.mcgamelib.Feature;
import net.silthus.mcgamelib.Phase;

import java.util.function.Consumer;

public interface PhaseBuilder<TPhase extends Phase> extends GameModeBuilder {

    <TFeature extends Feature> FeatureBuilder<TFeature, TPhase> with(Class<TFeature> featureClass);

    <TFeature extends Feature> FeatureBuilder<TFeature, TPhase> with(Class<TFeature> featureClass, Consumer<TFeature> config);
}
