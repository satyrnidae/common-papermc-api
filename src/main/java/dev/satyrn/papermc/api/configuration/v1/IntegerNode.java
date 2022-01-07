package dev.satyrn.papermc.api.configuration.v1;

import org.jetbrains.annotations.NotNull;

/**
 * Represents a configuration node with an integer value.
 *
 * @author Isabel Maskrey
 * @since 1.0-SNAPSHOT
 */
@SuppressWarnings("unused")
public class IntegerNode extends ConfigurationNode<Integer> {
    /**
     * Creates a new configuration node with an integer value.
     *
     * @param parent The parent container.
     * @param name   The node's name.
     * @since 1.0-SNAPSHOT
     */
    public IntegerNode(@NotNull final ConfigurationContainer parent, @NotNull final String name) {
        super(parent, name, parent.getConfig());
    }

    /**
     * Returns the integer value of the node.
     *
     * @return The integer value.
     * @since 1.0-SNAPSHOT
     */
    @Override
    @NotNull
    public Integer value() {
        return this.getConfig().getInt(this.getPath(), this.defaultValue());
    }

    /**
     * Gets the default value of the node.
     *
     * @return The value.
     * @since 1.3-SNAPSHOT
     */
    @Override
    @NotNull
    public Integer defaultValue() {
        return 0;
    }
}
