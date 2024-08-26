package dev.satyrn.paperwasp.configuration.node.primitive.v1;

import dev.satyrn.paperwasp.configuration.node.v1.Node;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A node implementation for managing {@code Boolean} values. This node
 * can be used to represent a boolean configuration value, with default
 * values and configuration loading capabilities.
 *
 * @author Isabel Maskrey
 * @since 3.0.0-paper-api.1.21-R0.1-SNAPSHOT
 */
public final class BooleanNode extends Node<Boolean> {
    private boolean value;

    /**
     * Constructs a new {@code BooleanNode} with the specified name and parent node.
     * Initializes the node with a default value of {@code false}.
     *
     * @param name   the name of this node
     * @param parent the parent node of this node
     * @since 3.0.0-paper-api.1.21-R0.1-SNAPSHOT
     */
    public BooleanNode(final @NotNull String name,
                final @NotNull Node<?> parent) {
        super(name, parent);
        this.setDefaultValue(false);
    }

    /**
     * Gets the current boolean value of this node.
     *
     * @return the current boolean value of this node
     * @since 3.0.0-paper-api.1.21-R0.1-SNAPSHOT
     */
    @Override
    public @NotNull Boolean get() {
        return this.value;
    }

    /**
     * Sets the value of this node. If the provided value is {@code null},
     * the value is set to {@code false}.
     *
     * @param value the new value to set, or {@code null} to set to {@code false}
     * @since 3.0.0-paper-api.1.21-R0.1-SNAPSHOT
     */
    @Override
    public void accept(@Nullable Boolean value) {
        this.value = value != null && value;
    }

    /**
     * Retrieves the configured boolean value from the configuration object
     * using the specified path. If the value is not found, the provided
     * default value is returned.
     *
     * @param path         the path in the configuration object to retrieve the value from
     * @param defaultValue the default value to return if the path does not exist
     * @return the configured boolean value from the configuration, or the default value if not found
     * @since 3.0.0-paper-api.1.21-R0.1-SNAPSHOT
     */
    @Override
    protected @NotNull Boolean getConfiguredValue(@NotNull String path, @Nullable Boolean defaultValue) {
        return this.getConfiguration().getBoolean(path, defaultValue != null && defaultValue);
    }
}
