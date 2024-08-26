package dev.satyrn.paperwasp.configuration.node.v1;

import org.jetbrains.annotations.NotNull;

/**
 * An interface that allows a custom node to be moved within a configuration tree.
 * <p>
 * The {@code MovableNode} interface provides a method for setting the parent of a node,
 * enabling the node to be used in the {@code ConfigurationBuilder.addCustomNode} method call.
 * This interface is particularly useful for custom nodes that need to be repositioned within
 * the configuration structure after creation.
 * <p>
 * The {@code setFinalParent} method is implemented as a default method to ensure that the parent node
 * is set only if the implementing class is an instance of {@link Node}. This helps maintain type safety
 * while allowing flexibility in node management.
 *
 * @since 3.0.0-paper-api.1.21-R0.1-SNAPSHOT
 */
public interface MovableNode {
    /**
     * Sets the parent node of the current node.
     * <p>
     * If the implementing class is an instance of {@link Node}, this method sets the specified node as its parent.
     * This method allows custom nodes to be moved within the configuration tree after they have been created.
     *
     * @param parent The new parent node for the current node.
     */
    default void moveTo(final @NotNull Node<?> parent) {
        if (this instanceof Node<?> node) {
            node.setParent(parent);
        }
    }
}
