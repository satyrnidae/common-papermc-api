package dev.satyrn.papermc.api.configuration.v1;

import org.bukkit.configuration.Configuration;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

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
    private final @Nullable ConfigurationNode<?> parent;
    // The name of the node.
    private final @Nullable String name;
    // The plugin. Cannot be null.
    private final @NotNull Plugin plugin;
    // All child objects added to this node.
    private final @NotNull Collection<ConfigurationNode<?>> children = new ArrayList<>();

    /**
     * Initializes a new Configuration node.
     *
     * @param parent The parent node.
     * @param name   The node name.
     * @param config The configuration instance.
     * @since 1.0-SNAPSHOT
     *
     * @deprecated since 1.9.0.
     */
    @Deprecated(since = "1.9.0")
    protected ConfigurationNode(final @NotNull ConfigurationNode<?> parent, final @Nullable String name, final @NotNull Configuration config) {
        this(parent.getPlugin(), parent, name, config);
    }

    /**
     * Initializes a new Configuration node.
     *
     * @param plugin The plugin instance.
     * @param parent The parent node.
     * @param name   The node name.
     * @param config The configuration instance.
     * @since 1.6.0
     *
     * @deprecated since 1.9.0
     */
    @Deprecated(since = "1.9.0")
    protected ConfigurationNode(final @NotNull Plugin plugin, @Nullable final ConfigurationNode<?> parent, @Nullable final String name, @NotNull final Configuration config) {
        this(plugin, parent, name);
    }

    /**
     * Initializes a new Configuration node.
     *
     * @param parent The parent node.
     * @param name   The node name.
     * @since 1.0-SNAPSHOT
     */
    protected ConfigurationNode(final @NotNull ConfigurationNode<?> parent, final @Nullable String name) {
        this(parent.getPlugin(), parent, name);
    }

    /**
     * Initializes a new Configuration node.
     *
     * @param plugin The plugin instance.
     * @param parent The parent node.
     * @param name   The node name.
     * @since 1.6.0
     */
    protected ConfigurationNode(final @NotNull Plugin plugin, @Nullable final ConfigurationNode<?> parent, @Nullable final String name) {
        this.plugin = plugin;
        this.parent = parent;
        this.name = name;

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
    @NotNull
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
        return plugin.getConfig();
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
        if (this.hasName()) {
            stringBuilder.append(this.getName());
        }

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
        this.getConfig().set(this.getValuePath(), value == null ? this.defaultValue() : value);
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
     * Whether the configuration node is a sub node or a root node.
     * @return {@code true} if the node has a parent node; otherwise, {@code false}
     */
    public final boolean isSubNode() {
        return this.parent != null;
    }

    /**
     * Whether the configuration node is named.
     * @return {@code true} if the root node name is not null or blank; otherwise, {@code false}
     */
    public final boolean hasName() {
        return this.name != null && !this.name.isBlank();
    }

    /**
     * Writes the value of the node to the config file.
     *
     * @since 1.9.0
     */
    public void save() {
        Object value = this.value();
        if (value != null && this.isSubNode()) {
            this.getConfig().set(this.getValuePath(), value);
        }
        if (this.hasChildren()) {
            for (ConfigurationNode<?> child : this.children) {
                child.save();
            }
        }
    }

    /**
     * Gets the comment list for the value node path.
     * <p>
     * If no comments exist, an empty list will be returned. A null entry
     * represents an empty line and an empty String represents an empty comment
     * line.
     *
     * @return An unmodifiable list of the node's comments. Every entry
     * represents one line.
     *
     * @since 1.9.0
     */
    public @NotNull List<String> getComments() {
        return this.getComments(false);
    }

    /**
     * Gets the comment list for this node.
     * <p>
     * If no comments exist, an empty list will be returned. A null entry
     * represents an empty line and an empty String represents an empty comment
     * line.
     *
     * @param basePath If {@code true}, the node's base path will be used instead
     *                 of its value path. This is useful if you have nested value
     *                 nodes.
     * @return An unmodifiable list of the node's comments. Every entry
     * represents one line.
     *
     * @since 1.9.0
     */
    public @NotNull List<String> getComments(boolean basePath) {
        final @NotNull StringBuilder stringBuilder = new StringBuilder();
        return this.getConfig().getComments(basePath ? this.getBasePath(stringBuilder) : this.getValuePath(stringBuilder));
    }

    /**
     * Gets the inline comment list for the value node path.
     * <p>
     * If no comments exist, an empty list will be returned. A null entry
     * represents an empty line and an empty String represents an empty comment
     * line.
     *
     * @return An unmodifiable list of the node's inline comments. Every entry
     * represents one line.
     *
     * @since 1.9.0
     */
    public @NotNull List<String> getInlineComments() {
        return this.getInlineComments(false);
    }

    /**
     * Gets the inline comment list for this node.
     * <p>
     * If no comments exist, an empty list will be returned. A null entry
     * represents an empty line and an empty String represents an empty comment
     * line.
     *
     * @param basePath If {@code true}, the node's base path will be used instead
     *                 of the value path. This is useful if you have nested value
     *                 nodes.
     * @return An unmodifiable list of the node's inline comments. Every entry
     * represents one line.
     *
     * @since 1.9.0
     */
    public @NotNull List<String> getInlineComments(boolean basePath) {
        final @NotNull StringBuilder stringBuilder = new StringBuilder();
        return this.getConfig().getInlineComments(basePath ? this.getBasePath(stringBuilder) : this.getValuePath(stringBuilder));
    }

    /**
     * Sets the comment list at the value node path.
     * <p>
     * If value is null, the comments will be removed. A null entry is an empty
     * line and an empty String entry is an empty comment line. If the path does
     * not exist, no comments will be set. Any existing comments will be
     * replaced, regardless of what the new comments are.
     * <p>
     * Some implementations may have limitations on what persists. See their
     * individual javadocs for details.
     * @param comments New comments to set at the value node. Every entry represents a new line.
     *
     * @since 1.9.0
     */
    public void setComments(String... comments) {
        this.setComments(false, comments);
    }

    /**
     * Sets the comment list for the node.
     * <p>
     * If value is null, the comments will be removed. A null entry is an empty
     * line and an empty String entry is an empty comment line. If the path does
     * not exist, no comments will be set. Any existing comments will be
     * replaced, regardless of what the new comments are.
     * <p>
     * Some implementations may have limitations on what persists. See their
     * individual javadocs for details.
     * @param basePath If {@code true}, the node's base path will be used instead
     *                 of its value path. This is useful if you have nested value
     *                 nodes.
     * @param comments New comments to set for the node. Every entry represents a new line.
     *
     * @since 1.9.0
     */
    public void setComments(boolean basePath, String... comments) {
        final @NotNull StringBuilder stringBuilder = new StringBuilder();
        this.getConfig().setComments(basePath ? this.getBasePath(stringBuilder) : this.getValuePath(stringBuilder), List.of(comments));
    }

    /**
     * Sets the inline comment list at the value node path.
     * <p>
     * If value is null, the comments will be removed. A null entry is an empty
     * line and an empty String entry is an empty comment line. If the path does
     * not exist, no comment will be set. Any existing comments will be
     * replaced, regardless of what the new comments are.
     * <p>
     * Some implementations may have limitations on what persists. See their
     * individual javadocs for details.
     * @param comments New comments to set at the value node path. Every entry represents a new line.
     *
     * @since 1.9.0
     */
    public void setInlineComments(String... comments) {
        this.setInlineComments(false, comments);
    }

    /**
     * Sets the inline comment list for the node.
     * <p>
     * If value is null, the comments will be removed. A null entry is an empty
     * line and an empty String entry is an empty comment line. If the path does
     * not exist, no comment will be set. Any existing comments will be
     * replaced, regardless of what the new comments are.
     * <p>
     * Some implementations may have limitations on what persists. See their
     * individual javadocs for details.
     * @param basePath if {@code true}, the base path will be used instead of the value path.
     *                 This can be useful if you have nested value nodes.
     * @param comments New comments to set for the node. Every entry represents a new line.
     *
     * @since 1.9.0
     */
    public void setInlineComments(boolean basePath, String... comments) {
        final @NotNull StringBuilder stringBuilder = new StringBuilder();
        this.getConfig().setInlineComments(basePath ? this.getBasePath(stringBuilder) : this.getValuePath(stringBuilder), List.of(comments));
    }

    /**
     * Returns the plugin logger associated with this server's logger. The
     * returned logger automatically tags all log messages with the plugin's
     * name.
     *
     * @return Logger associated with this plugin
     *
     * @since 1.9.0
     */
    public @NotNull Logger getLogger() {
        return plugin.getLogger();
    }

    /**
     * Returns a new slf4j Logger instance associated with the plugin.
     *
     * @return A new slf4j Logger associated with the plugin.
     *
     * @since 1.9.0
     */
    public @NotNull org.slf4j.Logger getSLF4JLogger() {
        return plugin.getSLF4JLogger();
    }
}
