package dev.satyrn.papermc.api.configuration.v2;

import dev.satyrn.papermc.api.configuration.v1.ConfigurationNode;
import dev.satyrn.papermc.api.configuration.v1.ContainerNode;
import org.bukkit.Difficulty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;

/**
 * Represents a node which has a value dependent on difficulty.
 *
 * @param <T> The value type, shared between each sub-node.
 *
 * @since 1.9.1
 * @author Isabel Maskrey
 */
@SuppressWarnings("unused")
public abstract class DifficultyDependentNode<T> extends ContainerNode {

    /**
     * Initializes a new difficulty-dependent configuration node.
     *
     * @param parent The parent node.
     * @param name   The node name.
     *
     * @since 1.9.1
     */
    protected DifficultyDependentNode(@NotNull ConfigurationNode<?> parent,
                                      @NotNull String name) {
        super(parent, name);
    }

    /**
     * Gets the node instance for the peaceful difficulty value.
     *
     * @return The peaceful node.
     *
     * @since 1.9.1
     */
    protected @Nullable ConfigurationNode<T> getPeacefulNode() {
        return null;
    }

    /**
     * Gets the node instance for the easy difficulty value.
     *
     * @return The easy node.
     *
     * @since 1.9.1
     */
    protected @Nullable ConfigurationNode<T> getEasyNode() {
        return null;
    }

    /**
     * Gets the node instance for the normal difficulty value.
     *
     * @return The normal node.
     *
     * @since 1.9.1
     */
    protected @Nullable ConfigurationNode<T> getNormalNode() {
        return null;
    }

    /**
     * Gets the node instance for the hard difficulty value.
     *
     * @return The hard node.
     *
     * @since 1.10.0
     */
    protected @Nullable ConfigurationNode<T> getHardNode() {
        return null;
    }

    /**
     * Gets the name of the value node, which will be used if this node contains children and a value.
     * <p>
     * Defaults to "value" for most node types.
     *
     * @return The name of the value node.
     *
     * @since 1.10.0
     */
    @Override
    public final @NotNull String getValueNodeName() {
        return "default";
    }

    /**
     * Gets a value specific to the given difficulty value.
     *
     * @param difficulty The difficulty of the current world.
     *
     * @return The value of the node for that difficulty.
     *
     * @since 1.10.0
     */
    public @Nullable T value(@NotNull Difficulty difficulty) {
        T value = null;
        switch (difficulty) {
            case PEACEFUL:
                final @Nullable ConfigurationNode<T> peacefulNode = this.getPeacefulNode();
                if (peacefulNode != null) {
                    value = peacefulNode.value();
                    break;
                }
            case EASY:
                final @Nullable ConfigurationNode<T> easyNode = this.getEasyNode();
                if (easyNode != null) {
                    value = easyNode.value();
                    break;
                }
            case NORMAL:
                final @Nullable ConfigurationNode<T> normalNode = this.getNormalNode();
                if (normalNode != null) {
                    value = normalNode.value();
                    break;
                }
            case HARD:
                final @Nullable ConfigurationNode<T> hardNode = this.getHardNode();
                if (hardNode != null) {
                    value = hardNode.value();
                    break;
                }
        }
        return value;
    }

    /**
     * Sets the value of the node for a specific difficulty.
     * 
     * @param difficulty The difficulty level.
     * @param value The value of the node at the difficulty level.
     *
     * @since 1.10.0
     */
    public final void setValue(@NotNull Difficulty difficulty, @Nullable T value) {
        switch (difficulty) {
            case PEACEFUL:
                final @Nullable ConfigurationNode<T> peacefulNode = this.getPeacefulNode();
                if (peacefulNode != null) {
                    peacefulNode.setValue(value);
                    break;
                }
            case EASY:
                final @Nullable ConfigurationNode<T> easyNode = this.getEasyNode();
                if (easyNode != null) {
                    easyNode.setValue(value);
                    break;
                }
            case NORMAL:
                final @Nullable ConfigurationNode<T> normalNode = this.getNormalNode();
                if (normalNode != null) {
                    normalNode.setValue(value);
                    break;
                }
            case HARD:
                final @Nullable ConfigurationNode<T> hardNode = this.getHardNode();
                if (hardNode != null) {
                    hardNode.setValue(value);
                    break;
                }
        }
    }

    /**
     * Writes the value of the node and its children to the config file.
     *
     * @since 1.10.0
     */
    @Override
    public void save() {
        // Saves each of the difficulty nodes separately from child nodes.
        final @Nullable ConfigurationNode<T> peacefulNode = this.getPeacefulNode();
        if (peacefulNode != null) {
            peacefulNode.save();
        }
        final @Nullable ConfigurationNode<T> easyNode = this.getEasyNode();
        if (easyNode != null) {
            easyNode.save();
        }
        final @Nullable ConfigurationNode<T> normalNode = this.getNormalNode();
        if (normalNode != null) {
            normalNode.save();
        }
        final @Nullable ConfigurationNode<T> hardNode = this.getHardNode();
        if (hardNode != null) {
            hardNode.save();
        }
        super.save();
    }

    /**
     * Gets an unmodifiable list of children for this node.
     *
     * @return An unmodifiable list of the item's children.
     *
     * @since 1.10.0
     */
    public @NotNull @Unmodifiable List<@NotNull ConfigurationNode<?>> getChildren() {
        return super.getChildren()
                .stream()
                .filter(x -> x != this.getPeacefulNode()
                        && x != this.getEasyNode()
                        && x != this.getNormalNode()
                        && x != this.getHardNode())
                .toList();
    }
}
