package net.silthus.mcgamelib;

import lombok.Getter;
import lombok.experimental.Accessors;
import net.silthus.mcgamelib.event.EventHandler;

@Getter
@Accessors(fluent = true)
public final class GameManager {

    private final MCGameLib plugin;

    private FeatureRegistry features;
    private PhaseRegistry phases;

    private EventHandler eventHandler;
    private UserHandler userHandler;

    GameManager(MCGameLib plugin) {
        this.plugin = plugin;
    }

    public void load() {

        features = new FeatureRegistry();
        phases = new PhaseRegistry();

        userHandler = new UserHandler();
        eventHandler = new EventHandler(plugin, userHandler);
    }
}
