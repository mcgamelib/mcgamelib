package net.silthus.mcgamelib;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public final class UserHandler {

    private final Map<UUID, User> userCache = new HashMap<>();

    public Optional<User> user(UUID uuid) {

        // TODO: implement
        return Optional.ofNullable(userCache.computeIfAbsent(uuid, id -> null));
    }
}
