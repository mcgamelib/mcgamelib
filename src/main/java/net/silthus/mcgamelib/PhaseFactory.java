package net.silthus.mcgamelib;

import lombok.extern.java.Log;
import net.silthus.configmapper.ConfigMap;
import net.silthus.configmapper.ConfigurationException;
import net.silthus.configmapper.bukkit.BukkitConfigMap;
import net.silthus.mcgamelib.annotations.PhaseInfo;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

@Log(topic = "MCGameLib")
public final class PhaseFactory<TPhase extends Phase> {

    private final Class<TPhase> phaseClass;
    private final PhaseInfo phaseInfo;
    private final Function<GameSession, TPhase> supplier;
    private final Map<UUID, ConfigMap> gameSessionConfigMaps = new HashMap<>();

    PhaseFactory(Class<TPhase> phaseClass, Function<GameSession, TPhase> supplier) {

        this.phaseClass = phaseClass;
        this.phaseInfo = phaseClass.getAnnotation(PhaseInfo.class);
        this.supplier = supplier;
    }

    /**
     * Creates a new phase in the scope of the game session.
     * <p>This will also scan the phase for all config properties and injects
     * the provided config section from the {@link GameConfig}.
     *
     * @param session the game session that creates the phase
     * @return the phase that was created
     * @throws ConfigurationException if the creation of the config map for the given feature fails.
     *                                this could be the case if the feature is missing required configuration parameters.
     */
    public TPhase create(GameSession session) throws ConfigurationException {

        final TPhase phase = supplier.apply(session);

        if (phase == null)
            throw new ConfigurationException("failed to create instance of phase " + phaseClass.getCanonicalName());

        return gameSessionConfigMaps.computeIfAbsent(session.id(),
                uuid -> {
                    try {
                        return BukkitConfigMap.of(phase).with(
                                session.game().config().getPhaseConfig(phase)
                        );
                    } catch (ConfigurationException e) {
                        log.severe("failed to create config map of phase " + phaseClass.getCanonicalName() + " (" + phaseInfo.value() + "): " + e.getMessage());
                        throw e;
                    }
                }
        ).applyTo(phase);
    }
}
