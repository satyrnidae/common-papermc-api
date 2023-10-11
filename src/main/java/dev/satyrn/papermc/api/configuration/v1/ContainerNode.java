package dev.satyrn.papermc.api.configuration.v1;

import dev.satyrn.papermc.api.configuration.v1.ConfigurationNode;
import org.bukkit.configuration.Configuration;
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
     *
     * @since 1.9.0
     */
    protected ContainerNode(final @NotNull ConfigurationNode<?> parent, final @Nullable String name) {
        super(parent, name, parent.getConfig());
    }

    /**
     * Initializes the node as a root node.
     * @param plugin The plugin instance.
     *
     * @since 1.9.0
     */
    protected ContainerNode(final @NotNull Plugin plugin) {
        super(plugin, null, null, plugin.getConfig());
    }

    /**
     * Initializes the node as a root node.
     *
     * @param plugin The plugin instance.
     * @param config The configuration instance.
     *
     * @since 1.9.0
     */
    protected ContainerNode(final @NotNull Plugin plugin, final @NotNull Configuration config) {
        super(plugin, null, null, config);
    }

    /**
     * Gets the current value of the node.
     * Always returns null for configuration containers.
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
     * Always returns null for configuration containers.
     *
     * @return The value.
     *
     * @since 1.9.0
     */
    @Override
    public final @Nullable Void defaultValue() {
        return null;
    }

    /**
     * Sets the value of the node.
     * @param value The value to set.
     *
     * @since 1.9.0
     */
    @Override
    public void setValue(Void value) { }
}
