package dev.satyrn.papermc.api.configuration.v1;

import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a value-less node which contains other nodes.
 *
 * @author Isabel Maskrey
 * @since 1.9.0
 */
@SuppressWarnings("unused")
public abstract class ContainerNode extends ConfigurationNode<Void> {
    /**
     * Initializes the node as a child of another node.
     *
     * @param parent The node parent.
     * @param name The name of the container.
     *
     * @since 1.9.0
     */
    protected ContainerNode(final @NotNull ConfigurationNode<?> parent, final @Nullable String name) {
        super(parent, name);
    }

    /**
     * Initializes the node without a parent.
     *
     * @param plugin The plugin instance.
     *
     * @since 1.9.0
     */
    protected ContainerNode(final @NotNull Plugin plugin) {
        super(plugin, null, null);
    }

    /**
     * Gets the current value of the node.
     * <p>
     * Always returns null for container nodes.
     *
     * @return {@code null}
     *
     * @since 1.9.0
     */
    @Override
    public final @Nullable Void value() {
        return null;
    }

    /**
     * Gets the default value of the node.
     * <p>
     * Always returns null for container nodes.
     *
     * @return The default value.
     *
     * @since 1.9.0
     */
    @Override
    public final @Nullable Void defaultValue() {
        return null;
    }

    /**
     * Sets the value of the node.
     * <p>
     * Always does nothing for container nodes.
     *
     * @param value The value to set.
     *
     * @since 1.9.0
     */
    @Override
    public void setValue(@Nullable Void value) { }
}
