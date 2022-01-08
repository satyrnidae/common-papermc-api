package dev.satyrn.papermc.api.configuration.v1;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a configuration node with a string value.
 *
 * @author Isabel Maskrey
 * @since 1.0-SNAPSHOT
 */
@SuppressWarnings("unused")
public class StringNode extends ConfigurationNode<String> {
    /**
     * Creates a new configuration node with a string value.
     *
     * @param parent The parent container.
     * @param name   The node's name.
     * @since 1.0-SNAPSHOT
     */
    public StringNode(final @NotNull ConfigurationContainer parent, final @NotNull String name) {
        super(parent, name, parent.getConfig());
    }

    /**
     * Returns the string value of the node.
     *
     * @return The string value.
     * @since 1.0-SNAPSHOT
     */
    @Override
    public @Nullable String value() {
        return this.getConfig().getString(this.getPath(), this.defaultValue());
    }

    /**
     * Gets the default value of the node.
     *
     * @return The value.
     * @since 1.3-SNAPSHOT
     */
    @Override
    public @Nullable String defaultValue() {
        return null;
    }
}
