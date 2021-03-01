package net.silthus.mcgamelib;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent = true)
@EqualsAndHashCode(callSuper = true)
public abstract class AbstractFeature extends AbstractGameObject implements Feature {

    public AbstractFeature(GameSession session) {
        super(session);
    }
}
