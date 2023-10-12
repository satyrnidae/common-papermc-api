package dev.satyrn.papermc.api.configuration.v1;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a configuration node with a string value.
 *
 * @author Isabel Maskrey
 * @since 1.0.0
 */
@SuppressWarnings("unused")
public class StringNode extends ConfigurationNode<String> {
    /**
     * Creates a new configuration node with a string value.
     *
     * @param parent The parent container.
     * @param name   The node's name.
     *
     * @since 1.0.0
     */
    public StringNode(final @NotNull ConfigurationNode<?> parent, final @NotNull String name) {
        super(parent, name);
    }

    /**
     * Returns the string value of the node.
     *
     * @return The string value.
     *
     * @since 1.0.0
     */
    @Override
    public @Nullable String value() {
        return this.getConfig().getString(this.getValuePath(), this.defaultValue());
    }

    /**
     * Gets the default value of the node.
     * <p>
     * Defaults to {@code null}.
     *
     * @return The default value.
     *
     * @since 1.3.0
     */
    @Override
    public @Nullable String defaultValue() {
        return null;
    }
}
