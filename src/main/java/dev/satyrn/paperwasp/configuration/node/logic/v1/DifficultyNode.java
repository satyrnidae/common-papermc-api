package dev.satyrn.paperwasp.configuration.node.logic.v1;

import dev.satyrn.paperwasp.configuration.node.v1.Node;
import org.bukkit.Difficulty;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a node that selects sub-nodes based on the difficulty settings of a Minecraft server.
 * This class extends {@link SwitchNode} and provides specific implementations for the {@link Difficulty} enum.
 * It allows for managing nodes corresponding to the different difficulty settings: PEACEFUL, EASY, NORMAL, and HARD.
 *
 * <p>To use this class, subclasses must provide implementations for the methods that return the specific sub-nodes
 * for each difficulty setting. These nodes are then registered with the {@link SwitchNode#registerSubNode(Enum, Node)}
 * method.</p>
 *
 * @param <T> The type of the value held by the sub-nodes.
 * @author Isabel Maskrey
 * @since 3.0.0-paper-api.1.21-R0.1-SNAPSHOT
 */
public abstract class DifficultyNode<T> extends SwitchNode<Difficulty, T> {
    /**
     * Constructs a {@code DifficultyNode} with the specified name and parent node.
     *
     * <p>This constructor sets up the difficulty node by registering sub-nodes for all difficulty settings
     * (PEACEFUL, EASY, NORMAL, and HARD) using the corresponding methods provided by subclasses. The provided parent
     * node is assigned as the parent of this difficulty node.</p>
     *
     * @param name The name of the node.
     * @param parent The parent node of this difficulty node.
     * @throws IllegalArgumentException if the name or parent node is null.
     * @since 3.0.0-paper-api.1.21-R0.1-SNAPSHOT
     */
    public DifficultyNode(final @NotNull String name,
                          final @NotNull Node<?> parent) {
        super(name, parent);
        this.registerSubNode(Difficulty.PEACEFUL, this.getPeacefulNode());
        this.registerSubNode(Difficulty.EASY, this.getEasyNode());
        this.registerSubNode(Difficulty.NORMAL, this.getNormalNode());
        this.registerSubNode(Difficulty.HARD, this.getHardNode());
    }

    /**
     * Constructs a {@code DifficultyNode} with the specified name and no parent node.
     *
     * <p>This constructor sets up the difficulty node by registering sub-nodes for all difficulty settings
     * (PEACEFUL, EASY, NORMAL, and HARD) using the corresponding methods provided by subclasses. Since no parent
     * node is specified, this node is considered a top-level node.</p>
     *
     * @param name The name of the node.
     * @throws IllegalArgumentException if the name is null.
     * @since 3.0.0-paper-api.1.21-R0.1-SNAPSHOT
     */
    protected DifficultyNode(@NotNull String name) {
        super(name);
        this.registerSubNode(Difficulty.PEACEFUL, this.getPeacefulNode());
        this.registerSubNode(Difficulty.EASY, this.getEasyNode());
        this.registerSubNode(Difficulty.NORMAL, this.getNormalNode());
        this.registerSubNode(Difficulty.HARD, this.getHardNode());
    }

    /**
     * Retrieves the node associated with the PEACEFUL difficulty setting.
     *
     * <p>This method is abstract and must be implemented by subclasses to provide
     * the appropriate node for the PEACEFUL difficulty setting.</p>
     *
     * @return The {@link Node} associated with the PEACEFUL difficulty setting.
     * @since 3.0.0-paper-api.1.21-R0.1-SNAPSHOT
     */
    public abstract Node<T> getPeacefulNode();

    /**
     * Retrieves the node associated with the EASY difficulty setting.
     *
     * <p>This method is abstract and must be implemented by subclasses to provide
     * the appropriate node for the EASY difficulty setting.</p>
     *
     * @return The {@link Node} associated with the EASY difficulty setting.
     * @since 3.0.0-paper-api.1.21-R0.1-SNAPSHOT
     */
    public abstract Node<T> getEasyNode();

    /**
     * Retrieves the node associated with the NORMAL difficulty setting.
     *
     * <p>This method is abstract and must be implemented by subclasses to provide
     * the appropriate node for the NORMAL difficulty setting.</p>
     *
     * @return The {@link Node} associated with the NORMAL difficulty setting.
     * @since 3.0.0-paper-api.1.21-R0.1-SNAPSHOT
     */
    public abstract Node<T> getNormalNode();

    /**
     * Retrieves the node associated with the HARD difficulty setting.
     *
     * <p>This method is abstract and must be implemented by subclasses to provide
     * the appropriate node for the HARD difficulty setting.</p>
     *
     * @return The {@link Node} associated with the HARD difficulty setting.
     * @since 3.0.0-paper-api.1.21-R0.1-SNAPSHOT
     */
    public abstract Node<T> getHardNode();
}
