package net.silthus.mcgamelib;

import lombok.NonNull;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemoryConfiguration;
import org.jetbrains.annotations.Nullable;

public final class GameConfig {

    private final ConfigurationSection config;

    public GameConfig(ConfigurationSection config) {
        this.config = config;
    }

    public GameConfig() {
        this.config = new MemoryConfiguration();
    }

    /**
     * Gets the configuration section that configures the given phase.
     * <p>An empty configuration section will be returned if no section is present.
     *
     * @param phase the phase to get a config for
     * @return the configuration section of the phase
     */
    public ConfigurationSection getPhaseConfig(@NonNull Phase phase) {

        ConfigurationSection phases = config.getConfigurationSection("phases");
        if (phases == null) phases = config.createSection("phases");
        ConfigurationSection phaseSection = phases.getConfigurationSection(phase.name());
        if (phaseSection == null) phaseSection = phases.createSection(phase.name());

        return phaseSection;
    }

    /**
     * Gets the relevant configuration section of the feature inside the game config.
     *
     * @param phase the phase of the feature.
     *              can be null if the feature is created in a game scope and not a phase.
     * @param feature the feature to get the config for
     * @return the config section of the feature
     */
    public ConfigurationSection getFeatureConfig(@Nullable Phase phase, @NonNull Feature feature) {

        ConfigurationSection section;
        if (phase == null) {
            section = config;
        } else {
            section = getPhaseConfig(phase);
        }

        ConfigurationSection features = section.getConfigurationSection("features");
        if (features == null) features = section.createSection("features");

        ConfigurationSection featureSection = features.getConfigurationSection(feature.name());
        if (featureSection == null) featureSection = features.createSection(feature.name());

        return featureSection;
    }
}
