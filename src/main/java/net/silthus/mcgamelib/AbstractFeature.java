package net.silthus.mcgamelib;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.Accessors;
import net.silthus.mcgamelib.annotations.FeatureInfo;

import java.util.UUID;

@Getter
@ToString
@Accessors(fluent = true)
@EqualsAndHashCode(callSuper = true)
public abstract class AbstractFeature extends AbstractGameObject implements Feature {

    private final UUID id = UUID.randomUUID();
    private final FeatureInfo info;

    public AbstractFeature(GameSession session) {
        super(session);
        this.info = getClass().getAnnotation(FeatureInfo.class);
    }

    @Override
    public String name() {

        return info.value();
    }
}
