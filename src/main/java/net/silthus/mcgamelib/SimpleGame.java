package net.silthus.mcgamelib;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
@Accessors(fluent = true)
public class SimpleGame implements Game {

    private final UUID id = UUID.randomUUID();
    private final GameManager gameManager;
    private final GameConfig config;

    private final Set<GameSession> sessions = new HashSet<>();

    @Override
    public final Collection<GameSession> sessions() {

        return sessions.stream()
                .filter(GameSession::active)
                .collect(Collectors.toUnmodifiableSet());
    }

    @Override
    public final Collection<GameSession> allSessions() {

        return Set.copyOf(sessions);
    }

    @Override
    public GameSession startNewGameSession() {
        return null;
    }
}
