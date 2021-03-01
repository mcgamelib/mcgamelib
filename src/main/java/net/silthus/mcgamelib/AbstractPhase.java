package net.silthus.mcgamelib;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

@Data
@Accessors(fluent = true)
@EqualsAndHashCode(callSuper = true)
public abstract class AbstractPhase extends AbstractGameObject implements Phase {

    private final HashMap<Class<?>, Feature> features = new HashMap<>();

    public AbstractPhase(GameSession session) {
        super(session);
    }

    @Override
    public Collection<Feature> features() {

        return List.copyOf(features.values());
    }

    @Override
    public <TFeature extends Feature> TFeature feature(Class<TFeature> featureClass) {
        return null;
    }

    @Override
    public <TFeature extends Feature> Optional<TFeature> optionalFeature(Class<TFeature> featureClass) {
        return Optional.empty();
    }

    @Override
    public <TFeature extends Feature> Phase addFeature(Class<TFeature> feature) {

        return addFeature(feature, tFeature -> {});
    }

    @Override
    public <TFeature extends Feature> Phase addFeature(Class<TFeature> feature, Consumer<TFeature> config) {

        gameManager().features().get(session(), feature).ifPresent(tFeature -> {
            config.accept(tFeature);
            features.put(feature, tFeature);
        });

        return this;
    }

    @Override
    public void start() {

    }

    @Override
    public void end() {

    }
}
