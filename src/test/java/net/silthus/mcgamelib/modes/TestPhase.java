package net.silthus.mcgamelib.modes;

import net.silthus.mcgamelib.AbstractPhase;
import net.silthus.mcgamelib.GameSession;
import net.silthus.mcgamelib.features.MaxHealthFeature;

public class TestPhase extends AbstractPhase {

    public TestPhase(GameSession session) {
        super(session);
    }

    @Override
    public void configure() {

        addFeature(MaxHealthFeature.class, maxHealthFeature -> maxHealthFeature.maxHealth(30));
    }
}
