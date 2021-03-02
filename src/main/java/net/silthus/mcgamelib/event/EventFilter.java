package net.silthus.mcgamelib.event;

import lombok.NonNull;
import net.silthus.mcgamelib.User;
import org.bukkit.event.Event;

import java.util.Optional;

@FunctionalInterface
public interface EventFilter {

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    boolean filter(@NonNull Event event, @NonNull RegisteredListener registeredListener, @NonNull Optional<User> user);
}
