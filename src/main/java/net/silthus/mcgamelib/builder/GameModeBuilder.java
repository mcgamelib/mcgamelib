package net.silthus.mcgamelib.builder;

import net.silthus.mcgamelib.GameMode;
import net.silthus.mcgamelib.Phase;

import java.util.function.Consumer;

public interface GameModeBuilder {

    <TPhase extends Phase> PhaseBuilder<TPhase> add(Class<TPhase> phaseClass);

    <TPhase extends Phase> PhaseBuilder<TPhase> add(Class<TPhase> phaseClass, Consumer<TPhase> config);

    GameMode build();
}
