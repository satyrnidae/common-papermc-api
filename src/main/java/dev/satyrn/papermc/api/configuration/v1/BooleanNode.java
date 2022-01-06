package dev.satyrn.papermc.api.configuration.v1;

import org.jetbrains.annotations.NotNull;

/**
 * Represents a configuration node with a boolean value.
 */
@SuppressWarnings("unused")
public final class BooleanNode extends ConfigurationNode<Boolean> {
    /**
     * Creates a new configuration node with a boolean value.
     *
     * @param parent The parent container.
     * @param name   The name of the configuration node.
     */
    public BooleanNode(final @NotNull ConfigurationContainer parent, final @NotNull String name) {
        super(parent, name, parent.getConfig());
    }

    /**
     * Returns the boolean value of the node.
     *
     * @return The boolean value.
     */
    @Override
    public @NotNull Boolean value() {
        return this.getConfig().getBoolean(this.getPath());
    }
}
