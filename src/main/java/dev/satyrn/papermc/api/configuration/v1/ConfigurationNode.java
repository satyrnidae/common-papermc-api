package dev.satyrn.papermc.api.configuration.v1;

import org.bukkit.configuration.Configuration;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Represents a single configuration node. Cannot contain sub-nodes or containers.
 *
 * @param <T> The value type.
 *
 * @author Isabel Maskrey
 * @since 1.0.0
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
    private final @NotNull List<ConfigurationNode<?>> children = new ArrayList<>();

    /**
     * Initializes a new Configuration node.
     *
     * @param parent The parent node.
     * @param name   The node name.
     * @param config The configuration instance.
     *
     * @since 1.0.0
     *
     * @deprecated since 1.9.0. Use a constructor which does not use a {@code configuration} parameter, as the value of the parameter is ignored.
     */
    @Deprecated(since = "1.9.0", forRemoval = true)
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
     *
     * @since 1.6.0
     *
     * @deprecated since 1.9.0. Use a constructor which does not use a {@code configuration} parameter, as the value of the parameter is ignored.
     */
    @Deprecated(since = "1.9.0", forRemoval = true)
    protected ConfigurationNode(final @NotNull Plugin plugin, @Nullable final ConfigurationNode<?> parent, @Nullable final String name, @NotNull final Configuration config) {
        this(plugin, parent, name);
    }

    /**
     * Initializes a new Configuration node.
     *
     * @param parent The parent node.
     * @param name   The node name.
     *
     * @since 1.0.0
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
     *
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
     * @return The plugin instance.
     *
     * @since 1.6.0
     */
    public @NotNull Plugin getPlugin() {
        return this.plugin;
    }

    /**
     * Gets the configuration instance for the plugin.
     *
     * @return The configuration instance.
     *
     * @since 1.0.0
     */
    public @NotNull Configuration getConfig() {
        return plugin.getConfig();
    }

    /**
     * Gets the name of the node.
     *
     * @return The name of the node.
     *
     * @since 1.0.0
     */
    public @Nullable String getName() {
        return this.name;
    }

    /**
     * Constructs the full node path.
     *
     * @return The full node path.
     *
     * @since 1.0.0
     *
     * @deprecated since 1.9.0. Use {@code getValuePath()} or {@code getBasePath(StringBuilder)} instead.
     */
    @Deprecated(since = "1.9.0")
    public @NotNull String getPath() {
        return this.getBasePath(new StringBuilder());
    }

    /**
     * Constructs the full node path.
     *
     * @param stringBuilder The {@link StringBuilder} with which to build out the full node path.
     *
     * @return The full node path.
     *
     * @since 1.0.0
     * @deprecated since 1.9.0. Use {@code getValuePath()} or {@code getBasePath(StringBuilder)} instead.
     */
    @Deprecated(since = "1.9.0")
    public final @NotNull String getPath(@NotNull final StringBuilder stringBuilder) {
        return this.getBasePath(stringBuilder);
    }

    /**
     * Gets the path of the node that contains the value.
     * <p>
     * Only matches the value of {@code getBasePath(StringBuilder)} if this node does not contain any children.
     *
     * @return The full path of the node which contains the value.
     *
     * @since 1.9.0
     */
    public final @NotNull String getValuePath() {
        return this.getValuePath(new StringBuilder());
    }

    /**
     * Gets the path of the node that contains the value.
     * <p>
     * Only matches the value of {@code getBasePath(StringBuilder)} if this node does not contain any children.
     *
     * @param stringBuilder The string builder with which to build out the full path name.
     *
     * @return The full path of the node which contains the value.
     *
     * @since 1.9.0
     */
    public final @NotNull String getValuePath(@NotNull final StringBuilder stringBuilder) {
        this.getBasePath(stringBuilder);
        // If the node contains children, but still stores a value, it must contain a value node
        // If the name of the node is empty then it is a root node.
        if (this.hasChildren()) {
            if (!stringBuilder.isEmpty() && !this.getValueNodeName().isEmpty()) {
                stringBuilder.append(".");
            }
            stringBuilder.append(this.getValueNodeName());
        }
        return stringBuilder.toString();
    }

    /**
     * Gets the path of the base node.
     * <p>
     * Only matches the value of {@code getValuePath(...)} if this node does not contain any children.
     *
     * @param stringBuilder The {@link StringBuilder} with which to build out the full path name.
     *
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
     * <p>
     * Defaults to "value" for most node types.
     *
     * @return The name of the value node.
     *
     * @since 1.9.0
     */
    public @NotNull String getValueNodeName() { return "value"; }

    /**
     * Returns the string representation of this node.
     * <p>
     * By default, this matches the format {@code path=value}, where {@code path}
     * is the path of the configuration value, and {@code value} is the string
     * representation of the value of this node.
     * <p>
     * This can be overridden indirectly by altering {@code toString(StringBuilder)}.
     *
     * @return The value as a string.
     *
     * @since 1.0.0
     */
    @Override
    public final @NotNull String toString() {
        final @NotNull StringBuilder stringBuilder = new StringBuilder();

        this.toString(stringBuilder);

        return stringBuilder.toString();
    }

    /**
     * Builds the string value of the node.
     * <p>
     * Override this to alter the value returned by {@code toString()}.
     * <p>
     * By default, this matches the format {@code path=value}, where {@code path}
     * is the path of the configuration value, and {@code value} is the string
     * representation of the value of this node.
     *
     * @param stringBuilder The {@link StringBuilder} with which to build the string.
     *
     * @since 1.9.1
     */
    public void toString(final @NotNull StringBuilder stringBuilder) {
        this.getValuePath(stringBuilder);
        stringBuilder.append('=');
        stringBuilder.append(this.value()); // If the value is null, outputs "null"
    }

    /**
     * Gets the value of the node.
     *
     * @return The value.
     *
     * @since 1.0.0
     */
    public abstract @Nullable T value();

    /**
     * Gets the default value of the node.
     *
     * @return The default value.
     *
     * @since 1.3.0
     */
    public abstract @Nullable T defaultValue();

    /**
     * Sets the value of the node.
     *
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
     *
     * @throws UnsupportedOperationException if the {@code add} operation
     *         is not supported by this collection
     * @throws ClassCastException if the class of the specified element
     *         prevents it from being added to this collection
     * @throws NullPointerException if the specified element is null and this
     *         collection does not permit null elements
     * @throws IllegalArgumentException if some property of the element
     *         prevents it from being added to this collection
     * @throws IllegalStateException if the element cannot be added at this
     *         time due to insertion restriction.
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
        return !this.getChildren().isEmpty();
    }

    /**
     * Whether the configuration node is a sub node or a root node.
     *
     * @return {@code true} if the node has a parent node; otherwise, {@code false}
     *
     * @since 1.9.0
     */
    public final boolean isSubNode() {
        return this.parent != null;
    }

    /**
     * Whether the configuration node is named.
     *
     * @return {@code true} if the root node name is not null or blank; otherwise, {@code false}
     *
     * @since 1.9.0
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
    public @NotNull @Unmodifiable List<String> getComments() {
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
     *
     * @return An unmodifiable list of the node's comments. Every entry
     * represents one line.
     *
     * @since 1.9.0
     */
    public @NotNull @Unmodifiable List<String> getComments(boolean basePath) {
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
    public @NotNull @Unmodifiable List<String> getInlineComments() {
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
     *
     * @return An unmodifiable list of the node's inline comments. Every entry
     * represents one line.
     *
     * @since 1.9.0
     */
    public @NotNull @Unmodifiable List<String> getInlineComments(boolean basePath) {
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
     *
     * @param comments New comments to set at the value node. Every entry represents a new line.
     *
     * @since 1.9.0
     */
    public void setComments(@Nullable String... comments) {
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
    public void setComments(boolean basePath, @Nullable String... comments) {
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
     *
     * @param comments New comments to set at the value node path. Every entry represents a new line.
     *
     * @since 1.9.0
     */
    public void setInlineComments(@Nullable String... comments) {
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
     *
     * @param basePath if {@code true}, the base path will be used instead of the value path.
     *                 This can be useful if you have nested value nodes.
     * @param comments New comments to set for the node. Every entry represents a new line.
     *
     * @since 1.9.0
     */
    public void setInlineComments(boolean basePath, @Nullable String... comments) {
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
     * Returns the plugin logger associated with this server's logger. The
     * returned logger automatically tags all log messages with the plugin's
     * name.
     *
     * @return SLF4J Logger associated with the plugin.
     *
     * @since 1.9.0
     */
    public @NotNull org.slf4j.Logger getSLF4JLogger() {
        return plugin.getSLF4JLogger();
    }

    /**
     * Gets an unmodifiable list of children for this node.
     *
     * @return An unmodifiable list of the item's children.
     *
     * @since 1.9.1
     */
    public @NotNull @Unmodifiable List<ConfigurationNode<?>> getChildren() {
        return List.copyOf(this.children);
    }
}
