package dev.satyrn.papermc.api.configuration.v1;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a configuration node with a boolean value.
 *
 * @author Isabel Maskrey
 * @since 1.0.0
 */
@SuppressWarnings("unused")
public class BooleanNode extends ConfigurationNode<Boolean> {
    /**
     * Creates a new configuration node with a boolean value.
     *
     * @param parent The parent container.
     * @param name   The name of the configuration node.
     *
     * @since 1.0.0
     */
    public BooleanNode(final @NotNull ConfigurationNode<?> parent, final @NotNull String name) {
        super(parent, name);
    }

    /**
     * Returns the boolean value of the node.
     *
     * @return The boolean value.
     *
     * @since 1.0.0
     */
    @Override
    public final @NotNull Boolean value() {
        return this.getConfig().getBoolean(this.getValuePath(), this.defaultValue());
    }

    /**
     * Gets the default value of the node.
     * <p>
     * Defaults to {@code false}.
     *
     * @return The default value.
     *
     * @since 1.3.0
     */
    @Override
    public @NotNull Boolean defaultValue() {
        return false;
    }

    /**
     * Gets the name of the "value" node, which will be used if this node contains children and a value.
     * <p>
     * Defaults to "enabled" for boolean nodes.
     *
     * @return The name of the value node.
     *
     * @since 1.9.0
     */
    @Override
    public @NotNull String getValueNodeName() {
        return "enabled";
    }

    /**
     * Sets the value of the node in the configuration file.
     *
     * @param value The value to set.
     *
     * @since 1.9.0
     */
    @Override
    public void setValue(@Nullable Boolean value) {

    }
}
