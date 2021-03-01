package net.silthus.mcgamelib;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent = true)
public abstract class AbstractFeature implements Feature {

    private final Phase phase;
}
