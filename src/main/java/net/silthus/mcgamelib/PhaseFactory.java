package net.silthus.mcgamelib;

import lombok.extern.java.Log;
import net.silthus.configmapper.ConfigMap;
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
     * Creates a new phase and initializes it in the scope of the game session.
     * <p>This will also scan the phase for all config properties and injects
     * the provided config section from the {@link GameConfig}.
     *
     * @param session the game session that creates the phase
     * @return the phase that was created
     * @throws InitializationException if the creation of the config map for the given feature fails.
     *                                this could be the case if the feature is missing required configuration parameters.
     */
    public TPhase create(GameSession session) throws InitializationException {

        return create(session, true);
    }

    /**
     * Creates a new phase in the scope of the game session.
     * <p>This will also scan the phase for all config properties and injects
     * the provided config section from the {@link GameConfig}.
     *
     * @param session the game session that creates the phase
     * @param init set to true to initialize the phase
     * @return the phase that was created
     * @throws InitializationException if the creation of the config map for the given feature fails.
     *                                this could be the case if the feature is missing required configuration parameters.
     */
    public TPhase create(GameSession session, boolean init) throws InitializationException {

        final TPhase phase;
        try {
            phase = supplier.apply(session);

            gameSessionConfigMaps.computeIfAbsent(session.id(),
                    uuid -> BukkitConfigMap.of(phase).with(
                            session.game().config().getPhaseConfig(phase)
                    )
            ).applyTo(phase);
        } catch (Exception e) {
            throw new InitializationException("failed to create instance of phase " + phaseClass.getCanonicalName(), e);
        }

        if (init) {
            try {
                phase.initialize();
            } catch (Exception e) {
                throw new InitializationException("failed to initialize phase " + phase.getClass().getCanonicalName(), e);
            }
        }

        return phase;
    }
}
