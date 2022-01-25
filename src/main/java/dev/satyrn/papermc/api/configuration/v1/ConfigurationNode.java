package dev.satyrn.papermc.api.configuration.v1;

import org.bukkit.configuration.Configuration;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * Represents a single configuration node. Cannot contain sub-nodes or containers.
 *
 * @param <T> The value type.
 * @author Isabel Maskrey
 * @since 1.0-SNAPSHOT
 */
@SuppressWarnings("unused")
public abstract class ConfigurationNode<T> {
    // The parent configuration container.
    private final transient @Nullable ConfigurationContainer parent;
    // The name of the node.
    private final transient @NotNull String name;
    // The configuration file instance.
    private final transient @NotNull Configuration config;
    // The plugin. Can be null.
    private final transient @Nullable Plugin plugin;

    /**
     * Initializes a new Configuration node.
     *
     * @param parent The parent node.
     * @param name   The node name.
     * @param config The configuration instance.
     * @since 1.0-SNAPSHOT
     */
    protected ConfigurationNode(final @Nullable ConfigurationContainer parent, final @NotNull String name, final @NotNull Configuration config) {
        this(parent == null ? null : parent.getPlugin(), parent, name, config);
    }

    /**
     * Initializes a new Configuration node.
     *
     * @param plugin The plugin instance.
     * @param parent The parent node.
     * @param name   The node name.
     * @param config The configuration instance.
     * @since 1.6.0
     */
    protected ConfigurationNode(final @Nullable Plugin plugin, @Nullable final ConfigurationContainer parent, @NotNull final String name, @NotNull final Configuration config) {
        this.plugin = plugin;
        this.parent = parent;
        this.name = name;
        this.config = config;
    }

    /**
     * Gets the plugin instance.
     *
     * @return The plugin instance. May be null.
     * @since 1.6.0
     */
    @Nullable
    public Plugin getPlugin() {
        return this.plugin;
    }

    /**
     * Gets the configuration instance.
     *
     * @return The configuration instance.
     * @since 1.0-SNAPSHOT
     */
    @NotNull
    public Configuration getConfig() {
        return config;
    }

    /**
     * Gets the name of the node.
     *
     * @return The name of the node.
     * @since 1.0-SNAPSHOT
     */
    @NotNull
    public String getName() {
        return this.name;
    }

    /**
     * Constructs the full node path.
     *
     * @return The full node path.
     * @since 1.0-SNAPSHOT
     */
    @NotNull
    public String getPath() {
        return this.getPath(new StringBuilder());
    }

    /**
     * Constructs the full node path.
     *
     * @param stringBuilder The StringBuilder with which to build out the full node path.
     * @return The full node path.
     * @since 1.0-SNAPSHOT
     */
    @NotNull
    public final String getPath(@NotNull final StringBuilder stringBuilder) {
        if (parent != null) {
            parent.getPath(stringBuilder);
            stringBuilder.append('.');
        }
        stringBuilder.append(this.getName());
        return stringBuilder.toString();
    }

    /**
     * Returns the value of the node as a string.
     *
     * @return The value as a string.
     * @since 1.0-SNAPSHOT
     */
    @Override
    public final @NotNull String toString() {
        return this.value() == null ? "" : Objects.requireNonNull(this.value()).toString();
    }

    /**
     * Gets the value of the node.
     *
     * @return The value.
     * @since 1.0-SNAPSHOT
     */
    public abstract @Nullable T value();

    /**
     * Gets the default value of the node.
     *
     * @return The value.
     * @since 1.3-SNAPSHOT
     */
    public abstract @Nullable T defaultValue();
}
