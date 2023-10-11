package dev.satyrn.papermc.api.configuration.v1;

import org.bukkit.configuration.Configuration;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
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
    private final @Nullable ConfigurationContainer parent;
    // The name of the node.
    private final @NotNull String name;

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
     * @throws IllegalArgumentException if the name is empty, but the node is added to a parent
     * @throws IllegalArgumentException if the name is not empty, but the node is not added to a parent
     */
    protected ConfigurationNode(final @Nullable Plugin plugin, @Nullable final ConfigurationContainer parent, @NotNull final String name, @NotNull final Configuration config) {
        this.plugin = plugin;
        this.parent = parent;
        this.name = name;
        this.config = config;

        if (parent != null) {
            if (this.name.isBlank()) {
                throw new IllegalArgumentException("A non-named node cannot be added to a parent!");
            }
            parent.addChild(this);
        } else if (!this.name.isBlank()) {
            throw new IllegalArgumentException("A named node cannot be used as a root node.");
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
        // If the node contains children, but still stores a value, it must contain a value node
        // If the name of the node is empty then it is a root node.
        if (!this.getName().isBlank() && !this.children.isEmpty()) {
            stringBuilder.append(".value");
        }
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

    /**
     * Sets the value of the node.
     * @param value The value to set.
     *
     * @since 1.9.0
     */
    public void setValue(T value) {
        this.config.set(this.getPath(), value == null ? this.defaultValue() : value);
        this.config.setComments(this.getPath(), this.getComments());
    }

    /**
     * Gets a list of comments.
     *
     * @return A list of the comments for the config node.
     *
     * @since 1.9.0
     */
    public @Nullable List<String> getComments() {
        return null;
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
    protected void addChild(ConfigurationNode<?> configurationNode) {
        children.add(configurationNode);
    }

    /**
     * Sets the comment list at the specified path.
     * <p>
     * If value is null, the comments will be removed. A null entry is an empty
     * line and an empty String entry is an empty comment line. If the path does
     * not exist, no comments will be set. Any existing comments will be
     * replaced, regardless of what the new comments are.
     * <p>
     * Some implementations may have limitations on what persists. See their
     * individual javadocs for details.
     *
     * @since 1.9.0
     */
    public void setComments() {
        config.setComments(this.getPath(), this.getComments());
    }
}
