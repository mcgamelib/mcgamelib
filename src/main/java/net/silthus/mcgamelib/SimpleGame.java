package net.silthus.mcgamelib;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
@Accessors(fluent = true)
@EqualsAndHashCode(of = "id")
class SimpleGame implements Game {

    private final UUID id;
    private final GameManager gameManager;
    private final GameDefinition definition;
    private final GameConfig config;

    private final Set<GameSession> sessions = new HashSet<>();

    SimpleGame(UUID id, GameManager gameManager, GameDefinition definition, GameConfig config) {
        this.id = id;
        this.gameManager = gameManager;
        this.definition = definition;
        this.config = config;
    }

    SimpleGame(GameManager gameManager, GameDefinition definition, GameConfig config) {
        this(UUID.randomUUID(), gameManager, definition, config);
    }

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
    public GameSession createSession() {

        GameSession session = new SimpleGameSession(this).initialize();
        sessions.add(session);

        return session;
    }
}
