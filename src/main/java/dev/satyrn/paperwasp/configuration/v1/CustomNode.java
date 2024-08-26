package dev.satyrn.paperwasp.configuration.v1;

import dev.satyrn.paperwasp.configuration.node.v1.MovableNode;
import dev.satyrn.paperwasp.configuration.node.v1.Node;
import org.jetbrains.annotations.NotNull;

/**
 * A base class for creating custom configuration nodes with internal value logic.
 * <p>
 * The {@code CustomNode} class provides a foundation for end users to define their own custom configuration nodes
 * that can be integrated into the {@code ConfigurationBuilder}. By extending this abstract class, users can create
 * nodes with specific behaviors and logic tailored to their needs.
 * <p>
 * This class implements the {@link MovableNode} interface, allowing the custom node to be repositioned within the
 * configuration tree. The {@code CustomNode} class is generic, allowing the type of the node's value to be specified
 * by the subclass.
 *
 * @param <T> The type of the value held by this node.
 * @since 3.0.0-paper-api.1.21-R0.1-SNAPSHOT
 */
public abstract class CustomNode<T> extends Node<T> implements MovableNode {
    /**
     * Constructs a new {@code CustomNode} with the specified name.
     * <p>
     * The name is used to identify the node within the configuration structure. Subclasses should use this constructor
     * to initialize their custom nodes.
     *
     * @param name The name of the custom node.
     */
    protected CustomNode(final @NotNull String name) {
        super(name);
    }

    /**
     * Moves this node to the specified parent node, ensuring that the operation is not overridden by subclasses.
     * <p>
     * This method invokes the default implementation from the {@link MovableNode} interface,
     * effectively setting the parent of this node to the provided parent node.
     * By marking this method as {@code final}, it prevents subclasses from altering this behavior.
     *
     * @param parent The new parent node to which this node should be moved. Must not be {@code null}.
     */
    @Override
    public final void moveTo(@NotNull Node<?> parent) {
        MovableNode.super.moveTo(parent);
    }
}
