package net.silthus.mcgamelib.features;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import net.silthus.configmapper.ConfigOption;
import net.silthus.mcgamelib.AbstractFeature;
import net.silthus.mcgamelib.GameSession;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Data
@Accessors(fluent = true)
@EqualsAndHashCode(callSuper = true)
public class MaxHealthFeature extends AbstractFeature {

    @ConfigOption(
            required = true,
            description = "The maximum health that is set when this feature is activated."
    )
    private double maxHealth = 20d;

    private final Map<UUID, Double> healthMap = new HashMap<>();

    public MaxHealthFeature(GameSession session) {
        super(session);
    }

    @Override
    public void enable() {

        onPlayerJoin(this::addMaxHealth);
        onPlayerSpectate(this::removeMaxHealth);
    }

    @Override
    public void disable() {

        onPlayerQuit(this::removeMaxHealth);
    }

    private void addMaxHealth(Player player) {

        AttributeInstance attribute = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        if (attribute == null) return;
        healthMap.put(player.getUniqueId(), attribute.getBaseValue());
        attribute.setBaseValue(maxHealth);
    }

    private void removeMaxHealth(Player player) {

        Double oldHealth = healthMap.remove(player.getUniqueId());
        if (oldHealth != null) {
            AttributeInstance attribute = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
            if (attribute != null) {
                attribute.setBaseValue(oldHealth);
            }
        }
    }
}
