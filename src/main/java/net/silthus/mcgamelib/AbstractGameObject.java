package net.silthus.mcgamelib;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.UUID;

@Data
@Accessors(fluent = true)
@EqualsAndHashCode(of = "id")
public abstract class AbstractGameObject implements GameObject {

    private final UUID id = UUID.randomUUID();
    private final GameSession session;
}
