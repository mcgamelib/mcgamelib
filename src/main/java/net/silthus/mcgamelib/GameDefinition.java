package net.silthus.mcgamelib;

import lombok.Builder;
import lombok.Singular;
import lombok.Value;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@Value
@Builder
@Accessors(fluent = true)
public class GameDefinition {

    String name;
    @Builder.Default
    String description = "N/A";
    @Builder.Default
    String author = "N/A";
    @Singular(ignoreNullCollections = true)
    List<Map.Entry<Class<? extends Phase>, Consumer<? extends Phase>>> phases;

    public static class GameDefinitionBuilder {

        public <TPhase extends Phase> GameDefinitionBuilder addPhase(Class<TPhase> phaseClass) {

            return addPhase(phaseClass, phase -> {});
        }

        public <TPhase extends Phase> GameDefinitionBuilder addPhase(Class<TPhase> phaseClass, Consumer<TPhase> consumer) {

            if (phases == null) phases = new ArrayList<>();

            phases.add(Map.entry(phaseClass, consumer));

            return this;
        }
    }
}
