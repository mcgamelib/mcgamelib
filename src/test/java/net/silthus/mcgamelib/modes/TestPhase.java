package net.silthus.mcgamelib.modes;

import net.silthus.mcgamelib.AbstractPhase;
import net.silthus.mcgamelib.Feature;
import net.silthus.mcgamelib.GameSession;

import java.util.Collection;
import java.util.Optional;

public class TestPhase extends AbstractPhase {

    public TestPhase(GameSession session) {
        super(session);
    }

    @Override
    public void start() {

    }

    @Override
    public void end() {

    }

    @Override
    public Collection<Feature> features() {
        return null;
    }

    @Override
    public <TFeature extends Feature> TFeature feature(Class<TFeature> featureClass) {
        return null;
    }

    @Override
    public <TFeature extends Feature> Optional<TFeature> optionalFeature(Class<TFeature> featureClass) {
        return Optional.empty();
    }
}
