package dev.satyrn.papermc.api.configuration.v1;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A node which always contains a null value.
 */
public class NullNode extends ConfigurationNode<Object> {
    /**
     * Initializes a new Configuration node.
     *
     * @param parent The parent node.
     * @param name   The node name.
     *
     * @since 1.0.0
     */
    protected NullNode(@NotNull ConfigurationNode<?> parent, @Nullable String name) {
        super(parent, name);
    }

    /**
     * Gets the value of the node.
     *
     * @return The value.
     *
     * @since 1.0.0
     */
    @Override
    public @Nullable Object value() {
        return null;
    }

    /**
     * Gets the default value of the node.
     *
     * @return The default value.
     *
     * @since 1.3.0
     */
    @Override
    public @Nullable Object defaultValue() {
        return null;
    }
}
