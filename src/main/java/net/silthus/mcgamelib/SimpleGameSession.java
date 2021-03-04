package net.silthus.mcgamelib;

import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.java.Log;
import net.silthus.configmapper.ConfigurationException;
import net.silthus.mcgamelib.events.player.JoinGameEvent;
import net.silthus.mcgamelib.events.player.QuitGameEvent;
import net.silthus.mcgamelib.events.player.SpectateGameEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Data
@Log(topic = "MCGameLib")
@Accessors(fluent = true)
class SimpleGameSession implements GameSession {

    private final UUID id = UUID.randomUUID();
    private final Game game;
    private GameState state;

    private final Set<User> users = new HashSet<>();
    private final Set<User> spectators = new HashSet<>();

    private final List<Phase> phases = new ArrayList<>();

    private final Map<GameListenerType, Map<UUID, Set<Consumer<User>>>> userListeners = new HashMap<>();
    private final Map<GameListenerType, Map<UUID, Set<Consumer<Player>>>> playerListeners = new HashMap<>();

    private boolean initialized = false;

    public Collection<Phase> phases() {

        return List.copyOf(phases);
    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public GameSession initialize() {

        if (initialized()) return this;

        for (Map.Entry<Class<? extends Phase>, Consumer<? extends Phase>> entry : game().definition().phases()) {
            phases.add(createPhase((Class) entry.getKey(), entry.getValue()));
        }

        initialized(true);

        return this;
    }

    @Override
    public GameSession start() {
        // TODO: implement
        return this;
    }

    @Override
    public GameSession abort() {
        // TODO: implement
        return this;
    }

    @Override
    public GameSession end() {
        // TODO: implement
        return this;
    }

    @Override
    public JoinResult join(User user) {

        JoinGameEvent event = new JoinGameEvent(this, user);
        Bukkit.getPluginManager().callEvent(event);

        if (event.isCancelled()) return JoinResult.CANCELLED;

        informListeners(GameListenerType.JOIN, user);

        return JoinResult.SUCCESS;
    }

    @Override
    public JoinResult spectate(User user) {

        SpectateGameEvent event = new SpectateGameEvent(this, user);
        Bukkit.getPluginManager().callEvent(event);

        if (event.isCancelled()) return JoinResult.CANCELLED;

        informListeners(GameListenerType.SPECTATE, user);

        return JoinResult.SUCCESS;
    }

    @Override
    public boolean quit(User user) {

        Bukkit.getPluginManager().callEvent(new QuitGameEvent(this, user));
        informListeners(GameListenerType.QUIT, user);

        return users.remove(user) || spectators.remove(user);
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

        addUserListener(GameListenerType.JOIN, gameObject, consumer);
    }

    @Override
    public void onPlayerJoin(GameObject gameObject, Consumer<Player> consumer) {

        addPlayerListener(GameListenerType.JOIN, gameObject, consumer);
    }

    @Override
    public void onQuit(GameObject gameObject, Consumer<User> consumer) {

        addUserListener(GameListenerType.QUIT, gameObject, consumer);
    }

    @Override
    public void onPlayerQuit(GameObject gameObject, Consumer<Player> consumer) {

        addPlayerListener(GameListenerType.QUIT, gameObject, consumer);
    }

    @Override
    public void onSpectate(GameObject gameObject, Consumer<User> consumer) {

        addUserListener(GameListenerType.SPECTATE, gameObject, consumer);
    }

    @Override
    public void onPlayerSpectate(GameObject gameObject, Consumer<Player> consumer) {

        addPlayerListener(GameListenerType.SPECTATE, gameObject, consumer);
    }

    private void informListeners(GameListenerType type, User user) {

        userListeners(type).forEach(consumer -> consumer.accept(user));
        user.player().ifPresent(player -> playerListeners(type).forEach(playerConsumer -> playerConsumer.accept(player)));
    }

    private Set<Consumer<User>> userListeners(GameListenerType type) {

        return userListeners().computeIfAbsent(type, t -> new HashMap<>())
                .values().stream().flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }

    private Set<Consumer<Player>> playerListeners(GameListenerType type) {

        return playerListeners.computeIfAbsent(type, t -> new HashMap<>())
                .values().stream().flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }

    private void addUserListener(GameListenerType type, GameObject gameObject, Consumer<User> consumer) {

        userListeners.computeIfAbsent(type, t -> new HashMap<>())
                .computeIfAbsent(gameObject.id(), uuid -> new HashSet<>())
                .add(consumer);
    }

    private void addPlayerListener(GameListenerType type, GameObject gameObject, Consumer<Player> consumer) {

        playerListeners.computeIfAbsent(type, t -> new HashMap<>())
                .computeIfAbsent(gameObject.id(), uuid -> new HashSet<>())
                .add(consumer);
    }

    private <TPhase extends Phase> TPhase createPhase(Class<TPhase> phaseClass, Consumer<TPhase> consumer) throws ConfigurationException {

        return game().gameManager().phases().get(phaseClass).map(phaseFactory -> {
            TPhase phase = phaseFactory.create(this);
            consumer.accept(phase);
            return phase;
        }).orElseThrow(() -> new ConfigurationException("failed to find a valid phase registration for " + phaseClass.getCanonicalName()));
    }


    private enum GameListenerType {
        JOIN,
        QUIT,
        SPECTATE
    }
}
