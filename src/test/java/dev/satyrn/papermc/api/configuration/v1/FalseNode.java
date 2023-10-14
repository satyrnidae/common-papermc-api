package dev.satyrn.papermc.api.configuration.v1;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A boolean node which always contains a false value.
 */
public class FalseNode extends ConfigurationNode<Boolean> {
    /**
     * Initializes a new Configuration node.
     *
     * @param parent The parent node.
     * @param name   The node name.
     *
     * @since 1.0.0
     */
    protected FalseNode(@NotNull ConfigurationNode<?> parent, @Nullable String name) {
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
    public @NotNull Boolean value() {
        return false;
    }

    /**
     * Gets the default value of the node.
     *
     * @return The default value.
     *
     * @since 1.3.0
     */
    @Override
    public @NotNull Boolean defaultValue() {
        return false;
    }
}
