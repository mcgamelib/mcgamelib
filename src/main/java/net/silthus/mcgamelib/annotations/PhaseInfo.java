package net.silthus.mcgamelib.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Every {@link net.silthus.mcgamelib.Phase} must be annotated with {@code @PhaseInfo}.
 * <p>The annotation provides additional metadata like dependencies and a name.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface PhaseInfo {

    /**
     * Every phase requires a unique identifier used to reference it in configs.
     *
     * @return the unique identifier of the phase
     */
    String value();

    /**
     * Provide additional details about the phase that will be displayed to game creators.
     *
     * @return the description of this phase
     */
    String[] description() default {};
}
