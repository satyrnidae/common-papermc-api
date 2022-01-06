package dev.satyrn.papermc.api.configuration.v1;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a configuration node with a String value.
 */
@SuppressWarnings("unused")
public final class StringNode extends ConfigurationNode<String> {
    /**
     * Creates a new configuration node with a String value.
     *
     * @param parent The parent container.
     * @param name   The node's name.
     */
    public StringNode(final @NotNull ConfigurationContainer parent, final @NotNull String name) {
        super(parent, name, parent.getConfig());
    }

    /**
     * Returns the String value of the node.
     *
     * @return The String value.
     */
    @Override
    public @Nullable String value() {
        return this.getConfig().getString(this.getPath());
    }
}
