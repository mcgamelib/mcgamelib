package net.silthus.mcgamelib;

import lombok.Getter;
import lombok.experimental.Accessors;
import net.silthus.mcgamelib.event.EventHandler;

@Getter
@Accessors(fluent = true)
public final class GameManager {

    private final MCGameLib plugin;

    private final FeatureRegistry features = new FeatureRegistry();
    private final PhaseRegistry phases = new PhaseRegistry();

    private final EventHandler eventHandler;
    private final UserHandler userHandler;

    GameManager(MCGameLib plugin) {

        this.plugin = plugin;
        userHandler = new UserHandler();
        eventHandler = new EventHandler(plugin, userHandler);
    }

    public void initialize() {

    }
}
