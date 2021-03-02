package net.silthus.mcgamelib;

import lombok.extern.java.Log;
import net.silthus.configmapper.ConfigMap;
import net.silthus.configmapper.ConfigurationException;
import net.silthus.configmapper.bukkit.BukkitConfigMap;
import net.silthus.mcgamelib.annotations.FeatureInfo;
import org.bukkit.configuration.MemoryConfiguration;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

@Log(topic = "MCGameLib")
public final class FeatureFactory<TFeature extends Feature> {

    private final Class<TFeature> featureClass;
    private final FeatureInfo featureInfo;
    private final Function<GameSession, TFeature> supplier;
    private final Map<UUID, ConfigMap> gameSessionConfigMaps = new HashMap<>();

    FeatureFactory(Class<TFeature> featureClass, Function<GameSession, TFeature> supplier) {

        this.featureClass = featureClass;
        this.featureInfo = featureClass.getAnnotation(FeatureInfo.class);
        this.supplier = supplier;
    }

    public TFeature create(GameSession gameSession) {

        final TFeature feature = supplier.apply(gameSession);

        return gameSessionConfigMaps.computeIfAbsent(gameSession.id(),
                uuid -> {
                    try {
                        return BukkitConfigMap.of(feature).with(
                                gameSession.game().getFeatureConfig(featureInfo.value())
                                        .orElse(new MemoryConfiguration())
                        );
                    } catch (ConfigurationException e) {
                        log.severe("failed to create config map of feature " + featureClass.getCanonicalName() + " (" + featureInfo.value() + "): " + e.getMessage());
                        throw e;
                    }
                }
        ).applyTo(feature);
    }
}
