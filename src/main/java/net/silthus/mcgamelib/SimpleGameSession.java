package net.silthus.mcgamelib;

import lombok.Data;
import lombok.experimental.Accessors;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.function.Consumer;

@Data
@Accessors(fluent = true)
public class SimpleGameSession implements GameSession {

    private final UUID id = UUID.randomUUID();
    private final Game game;
    private GameState state;

    private final Set<User> users = new HashSet<>();
    private final Set<User> spectators = new HashSet<>();

    private final Map<UUID, List<Consumer<User>>> userJoinListeners = new HashMap<>();
    private final Map<UUID, List<Consumer<User>>> userQuitListeners = new HashMap<>();
    private final Map<UUID, List<Consumer<User>>> userSpectateListeners = new HashMap<>();

    @Override
    public JoinResult join(User user) {
        return null;
    }

    @Override
    public JoinResult spectate(User user) {
        return null;
    }

    @Override
    public boolean leave(User user) {
        return false;
    }

    @Override
    public Collection<User> users(boolean includeSpectators) {

        Set<User> users = new HashSet<>(users());
        if (includeSpectators) {
            users.addAll(spectators());
        }

        return Set.copyOf(users);
    }

    @Override
    public void onJoin(GameObject gameObject, Consumer<User> consumer) {

    }

    @Override
    public void onPlayerJoin(GameObject gameObject, Consumer<Player> consumer) {

    }

    @Override
    public void onQuit(GameObject gameObject, Consumer<User> consumer) {

    }

    @Override
    public void onPlayerQuit(GameObject gameObject, Consumer<Player> consumer) {

    }

    @Override
    public void onSpectate(GameObject gameObject, Consumer<User> consumer) {

    }

    @Override
    public void onPlayerSpectate(GameObject gameObject, Consumer<Player> consumer) {

    }
}
