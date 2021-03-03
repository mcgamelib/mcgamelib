package net.silthus.mcgamelib;

import lombok.NonNull;
import lombok.extern.java.Log;
import net.silthus.configmapper.ConfigMap;
import net.silthus.configmapper.ConfigurationException;
import net.silthus.configmapper.bukkit.BukkitConfigMap;
import net.silthus.mcgamelib.annotations.FeatureInfo;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

@Log(topic = "MCGameLib")
public final class FeatureFactory<TFeature extends Feature> {

    private final Class<TFeature> featureClass;
    private final FeatureInfo featureInfo;
    private final Function<GameSession, TFeature> supplier;
    private final Map<UUID, ConfigMap> phaseConfigMaps = new HashMap<>();
    private final Map<UUID, ConfigMap> gameSessionConfigMaps = new HashMap<>();

    FeatureFactory(Class<TFeature> featureClass, Function<GameSession, TFeature> supplier) {

        this.featureClass = featureClass;
        this.featureInfo = featureClass.getAnnotation(FeatureInfo.class);
        this.supplier = supplier;
    }

    /**
     * Creates a new feature in the scope of the game session.
     * <p>This should only be used if the feature is not part of a phase.
     * Use the {@link #create(Phase)} method otherwise.
     *
     * @param session the game session that creates the feature
     * @return the feature that was created
     * @throws ConfigurationException if the creation of the config map for the given feature fails.
     *                                this could be the case if the feature is missing required configuration parameters.
     */
    public TFeature create(GameSession session) throws ConfigurationException {

        return create(null, session, session.id(), gameSessionConfigMaps);
    }

    /**
     * Creates a new feature using the config path of the given phase.
     *
     * @param phase the phase that is requesting the creation of this feature
     * @return the created feature with all configs applied to it
     * @throws ConfigurationException if the creation of the config map for the given feature fails.
     *                                this could be the case if the feature is missing required configuration parameters.
     */
    public TFeature create(Phase phase) throws ConfigurationException {

        return create(phase, phase.session(), phase.id(), phaseConfigMaps);
    }

    private TFeature create(@Nullable Phase phase, @NonNull GameSession session, @NonNull UUID id, @NonNull Map<UUID, ConfigMap> cache) {

        final TFeature feature = supplier.apply(session);

        if (feature == null)
            throw new ConfigurationException("failed to create instance of feature " + featureClass.getCanonicalName());

        return cache.computeIfAbsent(id,
                uuid -> {
                    try {
                        return BukkitConfigMap.of(feature).with(
                                session.game().config().getFeatureConfig(phase, feature)
                        );
                    } catch (ConfigurationException e) {
                        log.severe("failed to create config map of feature " + featureClass.getCanonicalName() + " (" + featureInfo.value() + "): " + e.getMessage());
                        throw e;
                    }
                }
        ).applyTo(feature);
    }
}
