package net.silthus.mcgamelib.event.filters;

import lombok.NonNull;
import net.silthus.mcgamelib.User;
import net.silthus.mcgamelib.event.EventFilter;
import net.silthus.mcgamelib.event.RegisteredListener;
import org.bukkit.event.Event;

import java.util.Optional;

public class PlayerFilter implements EventFilter {
    @Override
    public boolean filter(@NonNull Event event, @NonNull RegisteredListener registeredListener, @NonNull Optional<User> user) {
        return false;
    }
}
