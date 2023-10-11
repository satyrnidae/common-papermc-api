package dev.satyrn.papermc.api.configuration.v1;

import org.bukkit.configuration.Configuration;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * Represents a single configuration node. Cannot contain sub-nodes or containers.
 *
 * @param <T> The value type.
 * @author Isabel Maskrey
 * @since 1.0-SNAPSHOT
 */
public abstract class ConfigurationNode<T> {
    // The parent configuration container.
    private final @Nullable ConfigurationNode<?> parent;
    // The name of the node.
    private final @Nullable String name;

    // The configuration file instance.
    private final @NotNull Configuration config;
    // The plugin. Can be null.
    private final @Nullable Plugin plugin;

    // All child objects added to this node.
    private final @NotNull Collection<ConfigurationNode<?>> children = new ArrayList<>();

    /**
     * Initializes a new Configuration node.
     *
     * @param parent The parent node.
     * @param name   The node name.
     * @param config The configuration instance.
     * @since 1.0-SNAPSHOT
     */
    protected ConfigurationNode(final @Nullable ConfigurationNode<?> parent, final @Nullable String name, final @NotNull Configuration config) {
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
    protected ConfigurationNode(final @Nullable Plugin plugin, @Nullable final ConfigurationNode<?> parent, @Nullable final String name, @NotNull final Configuration config) {
        this.plugin = plugin;
        this.parent = parent;
        this.name = name;
        this.config = config;

        if (this.isSubNode()) {
            this.parent.addChild(this);
        }
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
    @Nullable
    public String getName() {
        return this.name;
    }

    /**
     * Constructs the full node path.
     *
     * @return The full node path.
     * @since 1.0-SNAPSHOT
     *
     * @deprecated since 1.9.0. Use {@link ConfigurationNode getBasePath} or {@link ConfigurationNode getValuePath} instead.
     */
    @Deprecated(since = "1.9.0")
    @NotNull
    public String getPath() {
        return this.getBasePath(new StringBuilder());
    }

    /**
     * Constructs the full node path.
     *
     * @param stringBuilder The StringBuilder with which to build out the full node path.
     * @return The full node path.
     * @since 1.0-SNAPSHOT
     *
     * @deprecated since 1.9.0. Use {@link ConfigurationNode getBasePath} or {@link ConfigurationNode getValuePath} instead.
     */
    @Deprecated(since = "1.9.0")
    @NotNull
    public final String getPath(@NotNull final StringBuilder stringBuilder) {
        return this.getBasePath(stringBuilder);
    }

    /**
     * Gets the path of the node that contains the value.
     * May or may not match the value of {@link ConfigurationNode getNodePath}.
     *
     * @return The full path of the node which contains the value.
     *
     * @since 1.9.0
     */
    @NotNull
    public final String getValuePath() {
        return this.getValuePath(new StringBuilder());
    }

    /**
     * Gets the path of the node that contains the value.
     * May or may not match the value of {@link ConfigurationNode getNodePath}.
     *
     * @param stringBuilder The string builder with which to build out the full path name.
     * @return The full path of the node which contains the value.
     *
     * @since 1.9.0
     */
    @NotNull
    public final String getValuePath(@NotNull final StringBuilder stringBuilder) {
        this.getBasePath(stringBuilder);
        // If the node contains children, but still stores a value, it must contain a value node
        // If the name of the node is empty then it is a root node.
        if (this.hasChildren()) {
            if (!stringBuilder.isEmpty()) {
                stringBuilder.append(".");
            }
            stringBuilder.append(this.getValueNodeName());
        }
        return stringBuilder.toString();
    }

    /**
     * Gets the path of the base node.
     * May or may not match the value of {@link ConfigurationNode getValuePath} if this node contains children.
     *
     * @param stringBuilder The string builder with which to build out the full path name.
     * @return The full path of this node.
     *
     * @since 1.9.0
     */
    @NotNull
    public final String getBasePath(@NotNull final StringBuilder stringBuilder) {
        if (this.isSubNode()) {
            parent.getBasePath(stringBuilder);
            if (!stringBuilder.isEmpty()) {
                stringBuilder.append('.');
            }
        }
        stringBuilder.append(this.getName());

        return stringBuilder.toString();
    }

    /**
     * Gets the name of the value node, which will be used if this node contains children and a value.
     * Defaults to "value" for most node types.
     *
     * @return The name of the value node.
     * @since 1.9.0
     */
    public @NotNull String getValueNodeName() { return "value"; }

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

    /**
     * Sets the value of the node.
     * @param value The value to set.
     *
     * @since 1.9.0
     */
    public void setValue(@Nullable T value) {
        this.config.set(this.getValuePath(), value == null ? this.defaultValue() : value);
    }

    /**
     * Ensures that the node's list of children includes the specified child node.
     *
     * @param configurationNode the child to add to the node.
     * @throws UnsupportedOperationException if the {@code add} operation
     *         is not supported by this collection
     * @throws ClassCastException if the class of the specified element
     *         prevents it from being added to this collection
     * @throws NullPointerException if the specified element is null and this
     *         collection does not permit null elements
     * @throws IllegalArgumentException if some property of the element
     *         prevents it from being added to this collection
     * @throws IllegalStateException if the element cannot be added at this
     *         time due to insertion restrictions
     *
     * @since 1.9.0
     */
    protected final void addChild(@NotNull ConfigurationNode<?> configurationNode) {
        this.children.add(configurationNode);
    }

    /**
     * Whether the configuration node contains children.
     *
     * @return {@code true} if the child list is not empty; otherwise, {@code false}
     *
     * @since 1.9.0
     */
    public final boolean hasChildren() {
        return !this.children.isEmpty();
    }

    /**
     * Whether the configuration node is a root node.
     * @return {@code true} if the root node name is null or blank; otherwise, {@code false}
     */
    public final boolean isSubNode() {
        return this.parent != null;
    }
}
