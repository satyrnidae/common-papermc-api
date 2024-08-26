package dev.satyrn.paperwasp.configuration.node.primitive.v1;

import dev.satyrn.paperwasp.configuration.node.v1.Node;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A node implementation for managing {@code Character} values.
 * This node handles character values and provides default handling
 * for characters.
 *
 * @author Isabel Maskrey
 * @since 3.0.0-paper-api.1.21-R0.1-SNAPSHOT
 */
public final class CharacterNode extends Node<Character> {
    private char value;

    /**
     * Constructs a new {@code CharacterNode} with the specified name and parent node.
     * Initializes the node with a default value of {@code Character.MIN_VALUE}.
     *
     * @param name   the name of this node
     * @param parent the parent node of this node
     * @since 3.0.0-paper-api.1.21-R0.1-SNAPSHOT
     */
    public CharacterNode(final @NotNull String name,
                         final @NotNull Node<?> parent) {
        super(name, parent);
        this.setDefaultValue(Character.MIN_VALUE);
    }

    /**
     * Retrieves the current character value held by this node.
     *
     * @return the current character value
     * @since 3.0.0-paper-api.1.21-R0.1-SNAPSHOT
     */
    @Override
    public @NotNull Character get() {
        return this.value;
    }

    /**
     * Accepts a new character value and updates the node's internal value.
     * If the provided value is {@code null}, the node is set to the minimum
     * character value {@code Character.MIN_VALUE}.
     *
     * @param value the new character value to set, or {@code null} to set to {@code Character.MIN_VALUE}
     * @since 3.0.0-paper-api.1.21-R0.1-SNAPSHOT
     */
    @Override
    public void accept(@Nullable Character value) {
        this.value = value == null ? Character.MIN_VALUE : value;
    }

    /**
     * Retrieves the configured character value from the configuration object
     * using the specified path. If the value is not found, the provided default
     * value is used. The configuration value is expected to be a string,
     * and only the first character of this string is used.
     *
     * @param path         the path in the configuration object to retrieve the value from
     * @param defaultValue the default value to return if the path does not exist
     * @return the configured character value from the configuration, or the default value if not found
     * @since 3.0.0-paper-api.1.21-R0.1-SNAPSHOT
     */
    @Override
    protected @NotNull Character getConfiguredValue(@NotNull String path, @Nullable Character defaultValue) {
        return this.getConfiguration().getString(path, defaultValue == null ? String.valueOf(Character.MIN_VALUE) : String.valueOf(defaultValue.charValue())).charAt(0);
    }
}
