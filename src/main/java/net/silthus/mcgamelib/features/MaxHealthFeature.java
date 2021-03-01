package net.silthus.mcgamelib.features;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.silthus.configmapper.ConfigOption;
import net.silthus.mcgamelib.AbstractFeature;
import net.silthus.mcgamelib.Phase;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@Accessors(fluent = true)
@EqualsAndHashCode(callSuper = true)
public class MaxHealthFeature extends AbstractFeature {

    @ConfigOption(
            required = true,
            description = "The maximum health that is set when this feature is activated."
    )
    private double maxHealth = 20d;

    private final Map<UUID, Double> healthMap = new HashMap<>();

    public MaxHealthFeature(Phase phase) {
        super(phase);
    }

    @Override
    public void enable() {

        for (Player player : players()) {
            AttributeInstance attribute = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
            if (attribute == null) continue;
            healthMap.put(player.getUniqueId(), attribute.getBaseValue());
            attribute.setBaseValue(maxHealth);
        }
    }

    @Override
    public void disable() {

        for (Player player : players()) {
            Double oldHealth = healthMap.remove(player.getUniqueId());
            if (oldHealth != null) {
                AttributeInstance attribute = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
                if (attribute != null) {
                    attribute.setBaseValue(oldHealth);
                }
            }
        }
    }
}
