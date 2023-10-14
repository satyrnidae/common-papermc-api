package dev.satyrn.papermc.api.configuration.v1;

import org.bukkit.configuration.Configuration;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a base type for a class which contains several other configuration containers and/or nodes.
 *
 * @author Isabel Maskrey
 * @since 1.0.0
 * @deprecated Since 1.9.0. Use {@link ContainerNode} and {@link RootNode} instead. Will be removed in a future version.
 */
@Deprecated(since = "1.9.0")
@SuppressWarnings("unused")
public abstract class ConfigurationContainer extends ConfigurationNode<Void> {
    /**
     * Initializes the configuration container as a child of another container.
     *
     * @param parent The node parent.
     * @param name The name of the container.
     *
     * @since 1.0.0
     */
    protected ConfigurationContainer(final @NotNull ConfigurationNode<?> parent, final @Nullable String name) {
        super(parent, name);
    }

    /**
     * Initializes the configuration container as a root container.
     *
     * @param config The configuration instance.
     *
     * @since 1.0.0
     * @deprecated Since 1.6.0. Use constructors which specify parent plugin instead. Will be removed in a future version.
     */
    @Deprecated(since = "1.6.0", forRemoval = true)
    @SuppressWarnings({"ConstantConditions", "removal"})
    protected ConfigurationContainer(final @NotNull Configuration config) {
        super(null, null, null, config);
    }

    /**
     * Initializes the configuration container as a root container.
     *
     * @param plugin The plugin instance.
     *
     * @since 1.0.0
     */
    protected ConfigurationContainer(final @NotNull Plugin plugin) {
        super(plugin, null, null);
    }

    /**
     * Initializes the configuration container as a root container.
     *
     * @param plugin The plugin instance.
     * @param config The configuration instance.
     *
     * @since 1.6.0
     * @deprecated since 1.9.0. To be removed in a future version.
     */
    @Deprecated(since = "1.9.0", forRemoval = true)
    @SuppressWarnings("removal")
    protected ConfigurationContainer(final @NotNull Plugin plugin, final @NotNull Configuration config) {
        super(plugin, null, null, config);
    }

    /**
     * Gets the current value of the node.
     * <p>
     * Always returns null for configuration containers.
     *
     * @return {@code null}
     *
     * @since 1.0-SNAPSHOT
     */
    @Override
    public final @Nullable Void value() {
        return null;
    }

    /**
     * Gets the default value of the node.
     * <p>
     * Always returns null for configuration containers.
     *
     * @return The default value.
     *
     * @since 1.3.0
     */
    @Override
    public final @Nullable Void defaultValue() {
        return null;
    }

    /**
     * Sets the value of the node.
     * <p>
     * Does nothing for configuration containers.
     *
     * @param value The value to set.
     *
     * @since 1.9.0
     */
    @Override
    public void setValue(@Nullable Void value) { }
}
