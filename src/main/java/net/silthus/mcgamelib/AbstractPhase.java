package net.silthus.mcgamelib;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent = true)
public abstract class AbstractPhase implements Phase {

    private final GameSession session;
}
