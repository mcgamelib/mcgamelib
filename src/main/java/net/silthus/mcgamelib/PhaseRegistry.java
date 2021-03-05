package net.silthus.mcgamelib;

import lombok.extern.java.Log;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

@Log(topic = "MCGameLib")
public final class PhaseRegistry {

    private final Map<Class<? extends Phase>, PhaseFactory<? extends Phase>> phases = new HashMap<>();

    /**
     * @return an immutable set of all registered feature types
     */
    public Set<Class<? extends Phase>> allTypes() {

        return Set.copyOf(phases.keySet());
    }

    /**
     * Registers the phase using a public constructor that accepts a {@link GameSession} as its only argument.
     * <p>Use the {@link #register(Class, Function)} to supply your custom function that creates new instance of the phase.
     *
     * @param phaseClass the class of the phase
     * @param <TPhase> the type of the phase
     * @return this phase registry
     * @throws UnsupportedOperationException if phase feature class is already registered
     * @throws InitializationException inside the supplier function if the creation of the phase fails
     */
    public <TPhase extends Phase> PhaseRegistry register(Class<TPhase> phaseClass) {

        return register(phaseClass, phase -> {
            try {
                final Constructor<TPhase> constructor = phaseClass.getConstructor(GameSession.class);
                constructor.setAccessible(true);
                return constructor.newInstance(phase);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                phases.remove(phaseClass);
                throw new InitializationException("failed to create instance of phase: " + phaseClass.getCanonicalName(), e);
            }
        });
    }

    /**
     * Registers a new phase using the given supplier.
     * <p>Use the {@link #register(Class)} method to automatically detect a valid constructor.
     *
     * @param phaseClass the class of the phase that is registered
     * @param supplier the supplier for the phase
     * @param <TPhase> the type of the phase
     * @return this phase registry
     * @throws UnsupportedOperationException if the phase class is already registered
     */
    public <TPhase extends Phase> PhaseRegistry register(Class<TPhase> phaseClass, Function<GameSession, TPhase> supplier) {

        if (phases.containsKey(phaseClass)) {
            throw new UnsupportedOperationException("The phase is already registered: " + phaseClass.getCanonicalName());
        }

        phases.put(phaseClass, new PhaseFactory<>(phaseClass, supplier));
        log.fine("registered phase: " + phaseClass.getCanonicalName());

        return this;
    }

    /**
     * Tries to find a registered phase for the given class.
     * <p>Only the exact match may return the given phase instance.
     *
     * @param phaseClass the class of the phase
     * @param <TPhase> the type of the phase
     * @return the factory of the phase if it exists
     */
    @SuppressWarnings("unchecked")
    public <TPhase extends Phase> Optional<PhaseFactory<TPhase>> get(Class<TPhase> phaseClass) {

        return Optional.ofNullable((PhaseFactory<TPhase>) phases.get(phaseClass));
    }
}
