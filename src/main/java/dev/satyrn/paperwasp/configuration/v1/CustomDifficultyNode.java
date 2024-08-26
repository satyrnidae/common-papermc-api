package dev.satyrn.paperwasp.configuration.v1;

import dev.satyrn.paperwasp.configuration.node.logic.v1.DifficultyNode;
import dev.satyrn.paperwasp.configuration.node.v1.MovableNode;
import dev.satyrn.paperwasp.configuration.node.v1.Node;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a custom node implementation that manages configuration based on difficulty modes.
 * <p>
 * This abstract class extends {@link DifficultyNode} and implements {@link MovableNode} to handle configuration
 * nodes that are associated with different difficulty modes. The difficulty modes can be PEACEFUL, EASY, NORMAL, or HARD.
 * <p>
 * It provides a mechanism for custom nodes to manage and adapt their behavior based on the difficulty setting
 * and ensures that the node can be moved to a new parent node as needed.
 *
 * @param <T> The type of value held by the node.
 * @see DifficultyNode
 * @see MovableNode
 * @since 3.0.0-paper-api.1.21-R0.1-SNAPSHOT
 */
public abstract class CustomDifficultyNode<T> extends DifficultyNode<T> implements MovableNode {
    /**
     * Constructs a new {@code CustomDifficultyNode} with the specified name.
     *
     * @param name The name of the node. Must not be null.
     */
    protected CustomDifficultyNode(final @NotNull String name) {
        super(name);
    }

    /**
     * Moves this node to a new parent node. This method is a final implementation to ensure
     * that subclasses cannot override the behavior of moving the node.
     *
     * @param parent The new parent node to which this node will be moved. Must not be null.
     */
    @Override
    public final void moveTo(@NotNull Node<?> parent) {
        MovableNode.super.moveTo(parent);
    }
}
