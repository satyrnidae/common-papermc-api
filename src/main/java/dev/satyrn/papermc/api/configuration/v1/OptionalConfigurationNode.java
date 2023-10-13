package dev.satyrn.papermc.api.configuration.v1;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

/**
 * A configuration node
 * @param <T>
 */
@
public abstract class OptionalConfigurationNode<T> extends ConfigurationNode<Optional<T>>{
    /**
     * Initializes a new Configuration node.
     *
     * @param parent The parent node.
     * @param name   The node name.
     *
     * @since 1.9.1
     */
    protected OptionalConfigurationNode(@NotNull ConfigurationNode<?> parent, @Nullable String name) {
        super(parent, name);
    }

    /**
     * Gets the value of the node.
     *
     * @return The value.
     *
     * @since 1.9.1
     */
    @Override
    public abstract @NotNull Optional<T> value();

    /**
     * Gets the default value of the node.
     *
     * @return The default value.
     *
     * @since 1.9.1
     */
    @Override
    public abstract @NotNull Optional<T> defaultValue();
}
