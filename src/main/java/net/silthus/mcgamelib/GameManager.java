package net.silthus.mcgamelib;

import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
public final class GameManager {

    private final MCGameLib plugin;
    private final FeatureRegistry features = new FeatureRegistry();

    GameManager(MCGameLib plugin) {
        this.plugin = plugin;
    }
}
