package net.silthus.mcgamelib;

import java.util.Collection;
import java.util.Optional;

public interface Phase {

    GameSession session();

    default Game game() {
        return session().game();
    }

    void start();

    void end();

    Collection<Feature> features();

    <TFeature extends Feature> TFeature feature(Class<TFeature> featureClass);

    <TFeature extends Feature> Optional<TFeature> optionalFeature(Class<TFeature> featureClass);



}
