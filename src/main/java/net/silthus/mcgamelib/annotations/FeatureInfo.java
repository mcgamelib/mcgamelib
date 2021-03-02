package net.silthus.mcgamelib.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Every {@link net.silthus.mcgamelib.Feature} must be annotated with {@code @FeatureInfo}.
 * <p>The annotation provides additional metadata like dependencies and a name.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface FeatureInfo {

    /**
     * Every feature requires a unique identifier used to reference it in configs.
     *
     * @return the unique identifier of the feature
     */
    String value();

    /**
     * Provide additional details about the feature that will be displayed to game creators.
     *
     * @return the description of this feature
     */
    String[] description() default {};

    /**
     * Provide a list of other features and plugins this feature depends on.
     * <p>Use the feature prefix for features and the plugin prefix for plugins.
     * <pre>
     * depends = {
     *     "feature:my-other-feature",
     *     "plugin:Vault"
     * }
     * </pre>
     *
     * @return the dependencies of this feature
     */
    String[] depends() default {};
}
