package dev.satyrn.paperwasp.configuration.v1;

import dev.satyrn.paperwasp.configuration.node.v1.EnumNode;
import dev.satyrn.paperwasp.configuration.node.v1.MovableNode;
import dev.satyrn.paperwasp.configuration.node.v1.Node;
import org.jetbrains.annotations.NotNull;

/**
 * An abstract class for creating custom nodes that handle enum values.
 * <p>
 * This class extends {@link EnumNode} to provide a custom implementation for nodes that contain
 * enum values of a specific type. It also implements {@link MovableNode} to support the movement
 * of nodes within the configuration hierarchy.
 * <p>
 * Subclasses should specify the enum type when extending this class and provide any additional
 * customization as needed. This class ensures that the parent node can be set in a controlled
 * manner while maintaining type safety for the enum values.
 *
 * @param <E> The type of the enum handled by this node.
 */
public abstract class CustomEnumNode<E extends Enum<E>> extends EnumNode<E> implements MovableNode {
    /**
     * Constructs a new instance of {@code CustomEnumNode} with the specified name.
     *
     * @param name The name of the node.
     */
    public CustomEnumNode(final @NotNull String name) {
        super(name);
    }

    /**
     * Sets the parent node and ensures that the move operation is handled by the {@link MovableNode}
     * interface's default implementation.
     * <p>
     * This method is final to prevent subclasses from overriding the movement logic.
     *
     * @param parent The parent node to set for this node.
     */
    @Override
    public final void moveTo(@NotNull Node<?> parent) {
        MovableNode.super.moveTo(parent);
    }
}
