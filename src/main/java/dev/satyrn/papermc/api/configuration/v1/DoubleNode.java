package dev.satyrn.papermc.api.configuration.v1;

import org.jetbrains.annotations.NotNull;

/**
 * Represents a configuration node with a double-precision floating point value.
 *
 * @author Isabel Maskrey
 * @since 1.0-SNAPSHOT
 */
@SuppressWarnings("unused")
public final class DoubleNode extends ConfigurationNode<Double> {
    /**
     * Creates a new configuration node with a double-precision floating point value.
     *
     * @param parent The parent container.
     * @param name   The node name.
     * @since 1.0-SNAPSHOT
     */
    public DoubleNode(@NotNull final ConfigurationContainer parent, @NotNull final String name) {
        super(parent, name, parent.getConfig());
    }

    /**
     * Returns the double-precision floating point value of the node.
     *
     * @return The double-precision floating point value.
     * @since 1.0-SNAPSHOT
     */
    @Override
    @NotNull
    public Double value() {
        return this.getConfig().getDouble(this.getPath());
    }
}
