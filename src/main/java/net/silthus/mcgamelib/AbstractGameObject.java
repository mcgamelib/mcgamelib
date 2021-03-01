package net.silthus.mcgamelib;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent = true)
public abstract class AbstractGameObject implements GameObject {

    private final GameSession session;
}
