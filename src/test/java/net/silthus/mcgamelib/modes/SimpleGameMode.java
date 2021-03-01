package net.silthus.mcgamelib.modes;

import net.silthus.mcgamelib.GameMode;
import net.silthus.mcgamelib.builder.GameModeBuilder;
import net.silthus.mcgamelib.features.MaxHealthFeature;

public class SimpleGameMode implements GameMode {

    public void configure() {

        GameModeBuilder builder;

        GameMode gameMode = builder
                .add(TestPhase.class)
                    .with(MaxHealthFeature.class)
                    .with(MaxHealthFeature.class, maxHealthFeature -> maxHealthFeature.maxHealth(30))
                .add()
                .build();
    }
}
