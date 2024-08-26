package dev.satyrn.paperwasp.configuration.node.logic.v1;

import dev.satyrn.lunamoth.util.function.v1.ParametricSupplier;
import dev.satyrn.paperwasp.configuration.node.v1.Node;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * Represents a node that indexes sub-nodes based on the values contained within a parameter enum type.
 *
 * <p>The {@code SwitchNode} class extends {@code Node<T>} and implements both {@code ParametricSupplier<E, T>}
 * and {@code BiConsumer<E, T>}. It manages a set of sub-nodes indexed by values of type {@code E}, which
 * extends {@code Enum<E>}. The value of the {@code SwitchNode} is determined by the values of its sub-nodes.
 * This class provides methods to retrieve and modify the values of its sub-nodes and to register new sub-nodes.
 *
 * @param <E> the type of the enum used to index the sub-nodes
 * @param <T> the type of values held by this node and its sub-nodes
 * @author Isabel Maskrey
 * @since 3.0.0-paper-api.1.21-R0.1-SNAPSHOT
 */
public class SwitchNode<E extends Enum<E>, T> extends Node<T> implements ParametricSupplier<E, T>, BiConsumer<E, T> {
    private final @NotNull Map<E, Node<T>> subNodeValues = new HashMap<>();

    /**
     * Constructs a new {@code SwitchNode} with the specified name and parent node.
     * This node will be set up as a sub-node of the provided parent node.
     *
     * @param name The name of this node. Must not be {@code null}.
     * @param parent The parent node to which this node will be attached. Must not be {@code null}.
     * @throws IllegalArgumentException If the {@code name} or {@code parent} is {@code null}.
     * @since 3.0.0-paper-api.1.21-R0.1-SNAPSHOT
     */
    public SwitchNode(final @NotNull String name,
                      final @NotNull Node<?> parent) {
        super(name, parent);
    }

    /**
     * Constructs a new {@code SwitchNode} with the specified name.
     * This constructor is intended for use when creating a {@code MovableNode} variant of this node type.
     *
     * @param name The name of this node.
     * @throws IllegalArgumentException If {@code name} is null or blank.
     * @since 3.0.0-paper-api.1.21-R0.1-SNAPSHOT
     */
    protected SwitchNode(final @NotNull String name) {
        super(name);
    }

    /**
     * Retrieves the current value of this switch node. This method delegates to the
     * {@link #get(E)} method with a {@code null} selector.
     *
     * @return the current value of the node, or {@code null} if the value is not set.
     * @since 3.0.0-paper-api.1.21-R0.1-SNAPSHOT
     */
    @Override
    public @Nullable T get() {
        return this.get((E) null);
    }

    /**
     * Sets the value of this switch node. This method delegates to the
     * {@link #accept(E, T)} method with a {@code null} selector.
     *
     * @param value the value to set for this node, which may be {@code null}.
     * @since 3.0.0-paper-api.1.21-R0.1-SNAPSHOT
     */
    @Override
    public void accept(final @Nullable T value) {
        this.accept(null, value);
    }

    /**
     * Retrieves the value associated with the specified option from the sub-nodes.
     * If no sub-node exists for the given option, the default value is returned.
     *
     * @param option The option to select the sub-node. May be {@code null}.
     * @return The value from the selected sub-node, or the default value if no sub-node exists.
     * @since 3.0.0-paper-api.1.21-R0.1-SNAPSHOT
     */
    @Override
    public @Nullable T get(final @Nullable E option) {
        Node<T> subNode = this.subNodeValues.get(option);
        if (subNode != null) {
            return subNode.get();
        } else {
            return this.getDefaultValue();
        }
    }

    /**
     * Sets the value for the sub-node associated with the specified option.
     * If no sub-node exists for the given option, the default value is updated instead.
     *
     * @param option The option to select the sub-node. May be {@code null}.
     * @param value The value to set for the selected sub-node, or the default value if no sub-node exists.
     * @since 3.0.0-paper-api.1.21-R0.1-SNAPSHOT
     */
    @Override
    public void accept(final @Nullable E option,
                       final @Nullable T value) {
        Node<T> subNode = this.subNodeValues.get(option);
        if (subNode != null) {
            subNode.accept(value);
        } else {
            this.setDefaultValue(value);
        }
    }

    /**
     * Retrieves the configured value for this node. Since {@code SwitchNode} does not store its own value and instead
     * relies on its sub-nodes, this method always returns {@code null}.
     *
     * @param path The path in the configuration. Ignored in this implementation.
     * @param defaultValue The default value. Ignored in this implementation.
     * @return Always {@code null} for {@code SwitchNode}.
     * @since 3.0.0-paper-api.1.21-R0.1-SNAPSHOT
     */
    @Override
    protected final @Nullable T getConfiguredValue(@NotNull String path, @Nullable T defaultValue) {
        return null;
    }

    /**
     * Registers a new sub-node to this {@code SwitchNode}. The sub-node must be a direct descendant of this node.
     * If the sub-node's parent does not match this node, an {@link IllegalStateException} is thrown.
     *
     * @param enumConstant The enum constant used as the key to register the sub-node. This represents the value
     *                     associated with the sub-node in this switch structure.
     * @param subNode The {@link Node} instance to be registered as a sub-node. Must be a direct descendant of this node.
     * @throws IllegalArgumentException if {@code subNode} is not a direct descendant of this node.
     */
    public void registerSubNode(E enumConstant, Node<T> subNode) {
        if (subNode.getParent() != this) {
            throw new IllegalArgumentException("subNode must be an actual sub-node of this node!");
        }
        this.subNodeValues.put(enumConstant, subNode);
    }
}
