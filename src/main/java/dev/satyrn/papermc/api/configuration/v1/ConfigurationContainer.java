package dev.satyrn.papermc.api.configuration.v1;

import org.bukkit.configuration.Configuration;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a base type for a class which contains several other configuration containers and/or nodes.
 *
 * @author Isabel Maskrey
 * @since 1.0-SNAPSHOT
 */
@SuppressWarnings("unused")
public abstract class ConfigurationContainer extends ConfigurationNode<Void> {
    /**
     * Initializes the configuration container as a child of another container.
     *
     * @param parent The node parent.
     * @since 1.0-SNAPSHOT
     */
    protected ConfigurationContainer(final @NotNull ConfigurationContainer parent, final @NotNull String name) {
        super(parent, name, parent.getConfig());
    }

    /**
     * Initializes the configuration container as a root container.
     *
     * @param config The configuration instance.
     * @since 1.0-SNAPSHOT
     * @deprecated Use constructors which specify parent plugin instead.
     */
    @Deprecated
    protected ConfigurationContainer(final @NotNull Configuration config) {
        super(null, null, "", config);
    }

    /**
     * Initializes the configuration container as a root container.
     * @param plugin The plugin instance.
     */
    protected ConfigurationContainer(final @NotNull Plugin plugin) {
        super(plugin, null, "", plugin.getConfig());
    }

    /**
     * Initializes the configuration container as a root container.
     *
     * @param plugin The plugin instance.
     * @param config The configuration instance.
     * @since 1.6.0
     */
    protected ConfigurationContainer(final @NotNull Plugin plugin, final @NotNull Configuration config) {
        super(plugin, null, "", config);
    }

    /**
     * Always returns null for configuration containers.
     *
     * @return {@code null}
     * @since 1.0-SNAPSHOT
     */
    @Override
    public final @Nullable Void value() {
        return null;
    }

    /**
     * Gets the default value of the node.
     *
     * @return The value.
     * @since 1.3-SNAPSHOT
     */
    @Override
    public final @Nullable Void defaultValue() {
        return null;
    }
}
