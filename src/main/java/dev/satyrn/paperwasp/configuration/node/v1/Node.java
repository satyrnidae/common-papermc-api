package dev.satyrn.paperwasp.configuration.node.v1;

import dev.satyrn.lunamoth.util.function.v1.TriFunction;
import dev.satyrn.lunamoth.util.v1.MathUtil;
import dev.satyrn.lunamoth.util.v1.Parameters;
import org.apache.commons.lang3.NotImplementedException;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationOptions;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.checkerframework.common.returnsreceiver.qual.This;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Represents the base class of a configuration node.
 *
 * @param <T> The type of the data contained within the node.
 * @author Isabel Maskrey
 * @since 3.0.0-paper-api.1.21-R0.1-SNAPSHOT
 */
public abstract class Node<T> implements Supplier<T>, Consumer<T>, Collection<Node<?>> {
    private @Nullable Node<?> parent;
    private final @Nullable String name;
    private @NotNull Configuration configuration;
    private final @NotNull Collection<@NotNull Node<?>> children = new HashSet<>();
    private final @NotNull List<String> comments = new ArrayList<>();
    private final @NotNull List<String> inlineComments = new ArrayList<>();
    private final @NotNull List<String> valueComments = new ArrayList<>();
    private final @NotNull List<String> inlineValueComments = new ArrayList<>();
    private T defaultValue;

    /**
     * Constructs a new {@code Node} with the specified name, parent node, and configuration.
     *
     * @param name the name of this node
     * @param parent the parent node of this node
     * @throws IllegalArgumentException if either {@code name} or {@code parent} is {@code null}
     * @since 3.0.0-paper-api.1.21-R0.1-SNAPSHOT
     */
    protected Node(final @NotNull String name,
                   final @NotNull Node<?> parent) {
        this(name, parent, parent.getConfiguration());
        Parameters.requireNonNull("name", name);
        Parameters.requireNonNull("parent", parent);
    }

    /**
     * Constructs a new {@code Node} with the specified configuration, using {@code null} for the name and parent node.
     *
     * @param configuration the configuration associated with this node, must not be {@code null}
     * @throws IllegalArgumentException if {@code configuration} is {@code null}
     * @since 3.0.0-paper-api.1.21-R0.1-SNAPSHOT
     */
    protected Node(final @NotNull Configuration configuration) {
        this(null, null, configuration);
        Parameters.requireNonNull("configuration", configuration);
    }

    /**
     * Constructs a new {@code Node} with the specified name, using {@code null} for the parent node
     * and a default {@code DummyConfiguration} for the configuration.
     *
     * @param name the name of the node, must not be {@code null}
     * @throws IllegalArgumentException if {@code name} is {@code null}
     * @since 3.0.0-paper-api.1.21-R0.1-SNAPSHOT
     */
    protected Node(final @NotNull String name) {
        this(name, null, new DummyConfiguration());
        Parameters.requireNonNull("name", name);
    }

    /**
     * Constructs a new {@code Node} with the specified name, parent, and configuration.
     * <p>
     * This constructor initializes the node with the given {@code name}, {@code parent}, and {@code configuration}.
     * If a parent node is provided, the current node is added to the parent's list of child nodes.
     * </p>
     *
     * @param name the name of the node, can be {@code null}
     * @param parent the parent node, can be {@code null}
     * @param configuration the configuration for this node, must not be {@code null}
     * @throws IllegalArgumentException if {@code configuration} is {@code null}
     * @since 3.0.0-paper-api.1.21-R0.1-SNAPSHOT
     */
    private Node(final @Nullable String name,
                 final @Nullable Node<?> parent,
                 final @NotNull Configuration configuration) {
        Parameters.requireNonNull("configuration", configuration);
        this.name = name;
        this.parent = parent;
        this.configuration = configuration;

        if (parent != null) {
            parent.add(this);
        }
    }

    /**
     * Retrieves the value provided by this {@code Node}.
     * <p>
     * This method is part of the {@link Supplier} interface and returns the value associated with this node.
     * The value may be {@code null} if it has not been set or if it is allowed to be {@code null}.
     * </p>
     *
     * @return the value of type {@code T} associated with this node, which may be {@code null}
     * @since 3.0.0-paper-api.1.21-R0.1-SNAPSHOT
     */
    @Override
    public abstract @Nullable T get();

    /**
     * Accepts a value and processes it.
     * <p>
     * This method is abstract and must be implemented by subclasses to define
     * how the provided value should be handled. The value can be null, depending
     * on the specific implementation.
     *
     * @param value the value to process, which may be null
     *
     * @since 3.0.0-paper-api.1.21-R0.1-SNAPSHOT
     */
    @Override
    public abstract void accept(final @Nullable T value);

    /**
     * Retrieves a child node by its name.
     * <p>
     * This method searches for a child node with the specified name and returns it if found.
     * If no child node with the given name exists, {@code null} is returned.
     * <p>
     * The {@code childName} parameter must not be {@code null}. If it is, an
     * {@link IllegalArgumentException} will be thrown.
     *
     * @param childName the name of the child node to retrieve, must not be {@code null}
     * @return the child node with the specified name, or {@code null} if no such node exists
     * @throws IllegalArgumentException if {@code childName} is {@code null}
     * @since 3.0.0-paper-api.1.21-R0.1-SNAPSHOT
     */
    public final @Nullable Node<?> get(final @NotNull String childName) {
        Parameters.requireNonNull("childName", childName);
        if (this.isEmpty()) return null;
        return this.children.stream().filter(node -> childName.equals(node.name)).findAny().orElse(null);
    }

    /**
     * Retrieves the value of this node from the underlying configuration using the specified path.
     * <p>
     * This abstract method must be implemented to pull the node's value from the
     * {@link org.bukkit.configuration.Configuration} object. The implementation should
     * handle various data types and use the appropriate method for retrieving values based on
     * the node's data type.
     * <p>
     * If the value at the specified path is not found, the {@code defaultValue} will be returned.
     * If the path is {@code null}, an {@link IllegalArgumentException} will be thrown.
     *
     * @param path the path to the value in the configuration, must not be {@code null}
     * @param defaultValue the default value to return if the path does not exist, may be {@code null}
     * @return the value of this node from the configuration, or {@code defaultValue} if the path is not found
     * @throws IllegalArgumentException if {@code path} is {@code null}
     * @since 3.0.0-paper-api.1.21-R0.1-SNAPSHOT
     */
    protected abstract @Nullable T getConfiguredValue(final @NotNull String path, final @Nullable T defaultValue);

    /**
     * Retrieves the configured value of this node using the path obtained from {@link #getValuePath()}.
     * <p>
     * This method first fetches the configuration path from the node using {@code getValuePath()}. If the path is not blank,
     * it delegates the retrieval to the {@link #getConfiguredValue(String, Object)} method, passing the path and the
     * default value obtained from {@code getDefaultValue()}. If the path is blank, {@code null} is returned.
     * <p>
     * This method is intended for internal use to simplify the retrieval of configuration values based on the node's
     * value path and default value.
     *
     * @return the configured value of this node if the path is valid; otherwise, {@code null}
     * @since 3.0.0-paper-api.1.21-R0.1-SNAPSHOT
     */
    private @Nullable T getConfiguredValue() {
        final @NotNull String valuePath = this.getValuePath();
        if (!valuePath.isBlank()) {
            return this.getConfiguredValue(valuePath, this.getDefaultValue());
        }
        return null;
    }

    /**
     * Sets the configured value of this node in the underlying configuration using the specified path.
     * <p>
     * This method sets the value in the configuration object using the provided path and value. By default, this method
     * directly invokes {@code this.configuration.set(path, value)}, which handles the conversion of the value to a
     * configuration-compatible format. This approach works for most cases, as {@code org.bukkit.configuration.Configuration}
     * usually manages type-to-string conversion automatically.
     * <p>
     * This method is protected and can be overridden by subclasses if custom behavior is needed for handling specific
     * types or formats that require special processing beyond the default conversion.
     *
     * @param path the path in the configuration where the value should be set; must not be {@code null}
     * @param value the value to set in the configuration; can be {@code null}
     * @throws IllegalArgumentException if {@code path} is {@code null}
     * @since 3.0.0-paper-api.1.21-R0.1-SNAPSHOT
     */
    protected void setConfiguredValue(final @NotNull String path, final @Nullable T value) {
        Parameters.requireNonNull("path", path);
        this.configuration.set(path, value);
    }

    /**
     * Sets the configured value of this node using the value path retrieved from the node.
     * <p>
     * This method retrieves the value path for this node by calling {@code this.getValuePath()}. If the retrieved path
     * is not blank, it then calls the protected {@link #setConfiguredValue(String, Object)} method to set the value in
     * the configuration using the obtained path.
     * <p>
     * This method is intended for internal use to simplify the process of setting the value in the configuration, avoiding
     * the need for clients to specify the path manually.
     *
     * @param value the value to set in the configuration; can be {@code null}
     * @since 3.0.0-paper-api.1.21-R0.1-SNAPSHOT
     */
    private void setConfiguredValue(final @Nullable T value) {
        final @NotNull String valuePath = this.getValuePath();
        if (!valuePath.isBlank()) {
            this.setConfiguredValue(valuePath, value);
        }
    }

    /**
     * Retrieves the default value for this node.
     * <p>
     * The default value is used as a fallback if the value read from the configuration file is {@code null} or if
     * the configuration file does not contain the value for this node. This ensures that a meaningful value can be provided
     * even if the configuration does not specify one.
     *
     * @return the default value of this node; can be {@code null} if no default value is set
     * @since 3.0.0-paper-api.1.21-R0.1-SNAPSHOT
     */
    public final @Nullable T getDefaultValue() {
        return this.defaultValue;
    }

    /**
     * Sets the default value for this node.
     * <p>
     * This method allows setting a fallback value that will be used if the value read from the configuration file is
     * {@code null} or if the configuration does not specify a value for this node. The default value can be {@code null}
     * if no specific default value is needed.
     * <p>
     * This method returns the current node instance to allow for method chaining.
     *
     * @param defaultValue the default value to set; can be {@code null}
     * @return this node instance for method chaining
     * @since 3.0.0-paper-api.1.21-R0.1-SNAPSHOT
     */
    public final @This Node<T> setDefaultValue(final @Nullable T defaultValue) {
        this.defaultValue = defaultValue;
        return this;
    }

    /**
     * Returns the underlying configuration object associated with this node.
     *
     * @return the {@link Configuration} object associated with this node; never {@code null}
     * @since 3.0.0-paper-api.1.21-R0.1-SNAPSHOT
     */
    public final @NotNull Configuration getConfiguration() {
        return this.configuration;
    }

    /**
     * Returns the parent node of this node, if it exists.
     *
     * @return the parent {@link Node} of this node, or {@code null} if this node has no parent
     * @since 3.0.0-paper-api.1.21-R0.1-SNAPSHOT
     */
    public final @Nullable Node<?> getParent() {
        return this.parent;
    }

    /**
     * Sets the parent node for this node.
     * <p>
     * This method is intended for internal use and is generally used by the {@link dev.satyrn.paperwasp.configuration.v1.ConfigurationTreeBuilder} class
     * to manage node hierarchies. It updates the parent node reference and ensures that the configuration matches
     * the new parent node's configuration.
     * <p>
     * If the current parent is not {@code null}, it removes this node from its current parent's list of children.
     * The parent node's configuration is then used to update the configuration of this node. If the new parent is
     * {@code null} (such as for a root node), the parent is set to {@code null} and the configuration is set to {@code null}.
     * <p>
     * Note that changing the parent node is a non-standard operation and may impact the node hierarchy. It should
     * generally be avoided unless necessary for internal reconfiguration purposes.
     *
     * @param parent the new parent node; can be {@code null} for a root node
     * @since 3.0.0-paper-api.1.21-R0.1-SNAPSHOT
     */
    final void setParent(final @Nullable Node<?> parent) {
        if (this.parent != null) {
            this.parent.remove(this);
        }
        this.parent = parent;
        if (parent != null) {
            this.configuration = parent.getConfiguration();
            parent.add(this);
        }
    }

    /**
     * Checks if this node has a parent node.
     *
     * @return {@code true} if this node has a parent; {@code false} otherwise
     * @since 3.0.0-paper-api.1.21-R0.1-SNAPSHOT
     */
    public final boolean hasParent() {
        return this.parent != null;
    }

    /**
     * Returns an unmodifiable view of the collection of child nodes.
     *
     * <p>This method provides a snapshot of the current list of child nodes. The returned collection is unmodifiable,
     * meaning that any attempt to modify it will result in an {@link UnsupportedOperationException}. This ensures
     * that the internal state of the node's children cannot be altered directly, and any modifications must be
     * done through the methods provided by the {@code Node} class.
     *
     * @return an unmodifiable {@link Collection} of child {@code Node} instances
     * @since 3.0.0-paper-api.1.21-R0.1-SNAPSHOT
     */
    public final @NotNull @Unmodifiable Collection<@NotNull Node<?>> getChildren() {
        return Set.copyOf(this.children);
    }

    /**
     * Checks if the collection of child nodes is empty.
     *
     * <p>This method returns {@code true} if there are no child nodes, indicating that the node has no children.
     * Otherwise, it returns {@code false}. This method is useful for determining whether a node has any child nodes
     * without directly accessing the internal collection.
     *
     * @return {@code true} if the collection of child nodes is empty; {@code false} otherwise
     * @since 3.0.0-paper-api.1.21-R0.1-SNAPSHOT
     */
    @Override
    public final boolean isEmpty() {
        return this.children.isEmpty();
    }

    /**
     * Retrieves the name of the node.
     *
     * <p>This method returns the name of the node if it exists and is not blank. If the node does not have a valid name,
     * it returns {@code null}. This method is useful for accessing the name of the node while taking into account that
     * the name may be optional or not set.
     *
     * @return the name of the node if it exists and is not blank; {@code null} otherwise
     * @since 3.0.0-paper-api.1.21-R0.1-SNAPSHOT
     */
    public final @Nullable String getName() {
        return this.hasName() ? this.name : null;
    }

    /**
     * Checks whether this node has a name.
     * <p>
     * This method returns {@code true} if the node has a non-blank name. Root nodes, which generally
     * do not have a name, will return {@code false}.
     *
     * @return {@code true} if this node has a non-blank name; {@code false} otherwise
     * @since 3.0.0-paper-api.1.21-R0.1-SNAPSHOT
     */
    public final boolean hasName() {
        return this.name != null && !this.name.isBlank();
    }

    /**
     * Constructs the base path of this node.
     * <p>
     * The base path is constructed from the names of this node and its ancestors,
     * delimited by dots. If the node has no parent, the base path is simply the node's name.
     *
     * @return the base path of this node, or an empty string if the node has no name and no parent
     * @since 3.0.0-paper-api.1.21-R0.1-SNAPSHOT
     */
    public final @NotNull String getBasePath() {
        return this.getBasePath(new StringBuilder());
    }

    /**
     * Constructs the base path of this node and appends it to the provided {@link StringBuilder}.
     * <p>
     * This method builds the base path from the names of this node and its ancestors,
     * delimited by dots. If the node has no parent, the base path consists only of the node's name.
     * The result is appended to the given {@link StringBuilder}.
     *
     * @param stringBuilder the {@link StringBuilder} to which the base path will be appended
     * @return the {@link StringBuilder} containing the base path of this node
     * @since 3.0.0-paper-api.1.21-R0.1-SNAPSHOT
     */
    public final @NotNull String getBasePath(@NotNull StringBuilder stringBuilder) {
        if (this.hasParent()) {
            this.parent.getBasePath(stringBuilder);
            if (this.parent.getParent() != null) {
                stringBuilder.append('.');
            }
        }
        if (this.hasName()) {
            stringBuilder.append(this.getName());
        }
        return stringBuilder.toString();
    }

    /**
     * Constructs the value path of this node.
     * <p>
     * The value path is constructed from the base path of this node, appending "._value"
     * if the node has children. This is used to address the node's value in cases where
     * the node contains child nodes, with the actual value stored under a special YAML key.
     *
     * @return the value path of this node
     * @since 3.0.0-paper-api.1.21-R0.1-SNAPSHOT
     */
    public final @NotNull String getValuePath() {
        return this.getValuePath(new StringBuilder());
    }

    /**
     * Constructs the value path of this node and appends it to the provided {@link StringBuilder}.
     * <p>
     * This method builds the value path from the base path of this node, appending "._value"
     * if the node has children. The result is appended to the given {@link StringBuilder}.
     * This is used to address the node's value in cases where the node contains child nodes,
     * with the actual value stored under a special YAML key.
     *
     * @param stringBuilder the {@link StringBuilder} to which the value path will be appended
     * @return the {@link StringBuilder} containing the value path of this node
     * @since 3.0.0-paper-api.1.21-R0.1-SNAPSHOT
     */
    public final @NotNull String getValuePath(@NotNull StringBuilder stringBuilder) {
        this.getBasePath(stringBuilder);
        if (this.parent != null && !this.isEmpty()) {
            stringBuilder.append("._value");
        }
        return stringBuilder.toString();
    }

    /**
     * Retrieves an unmodifiable list of comments associated with this node's base path.
     * <p>
     * These comments appear above the base path node in the generated configuration YAML file.
     *
     * @return an unmodifiable list of comments for this node
     * @since 3.0.0-paper-api.1.21-R0.1-SNAPSHOT
     */
    public final @NotNull @Unmodifiable List<String> getComments() {
        return List.copyOf(this.comments);
    }

    /**
     * Adds a comment to the list of comments for this node's base path.
     * <p>
     * The comment will be added to the base path node's section in the generated configuration YAML file.
     *
     * @param comment the comment to add
     * @throws IllegalArgumentException if the provided comment is null
     * @since 3.0.0-paper-api.1.21-R0.1-SNAPSHOT
     */
    public final void addComment(final @NotNull String comment) {
        Parameters.requireNonNull("comment", comment);
        this.comments.add(comment);
    }

    /**
     * Adds multiple comments to the list of comments for this node's base path.
     * <p>
     * The comments will be added to the base path node's section in the generated configuration YAML file.
     *
     * @param comments the comments to add
     * @throws IllegalArgumentException if any of the provided comments are null
     * @since 3.0.0-paper-api.1.21-R0.1-SNAPSHOT
     */
    public final void addComments(final @NotNull String... comments) {
        Parameters.requireAllNonNull("comments", comments);
        this.comments.addAll(List.of(comments));
    }

    /**
     * Sets the comments for this node's base path, replacing any existing comments.
     * <p>
     * The existing comments are cleared and replaced with the provided comments. These comments
     * will appear above the base path node in the generated configuration YAML file.
     *
     * @param comments the comments to set
     * @throws IllegalArgumentException if any of the provided comments are null
     * @since 3.0.0-paper-api.1.21-R0.1-SNAPSHOT
     */
    public final void setComments(final @NotNull String... comments) {
        this.clearComments();
        this.addComments(comments);
    }

    /**
     * Clears all comments associated with this node's base path.
     * <p>
     * After this method is called, the node will have no comments above its base path in the generated configuration YAML file.
     *
     * @since 3.0.0-paper-api.1.21-R0.1-SNAPSHOT
     */
    public final void clearComments() {
        this.comments.clear();
    }

    /**
     * Retrieves an unmodifiable list of comments associated with this node's value path.
     * <p>
     * These comments appear above the value node in the generated configuration YAML file.
     *
     * @return an unmodifiable list of comments for this node's value path
     * @since 3.0.0-paper-api.1.21-R0.1-SNAPSHOT
     */
    public final @NotNull @Unmodifiable List<String> getValueComments() {
        return List.copyOf(this.valueComments);
    }

    /**
     * Adds a comment to the list of comments for this node's value path.
     * <p>
     * The comment will be added to the value node's section in the generated configuration YAML file.
     *
     * @param comment the comment to add
     * @throws IllegalArgumentException if the provided comment is null
     * @since 3.0.0-paper-api.1.21-R0.1-SNAPSHOT
     */
    public final void addValueComment(final @NotNull String comment) {
        Parameters.requireNonNull("comment", comment);
        this.valueComments.add(comment);
    }

    /**
     * Adds multiple comments to the list of comments for this node's value path.
     * <p>
     * The comments will be added to the value node's section in the generated configuration YAML file.
     *
     * @param comments the comments to add
     * @throws IllegalArgumentException if any of the provided comments are null
     * @since 3.0.0-paper-api.1.21-R0.1-SNAPSHOT
     */
    public final void addValueComments(final @NotNull String... comments) {
        Parameters.requireAllNonNull("comments", comments);
        this.valueComments.addAll(List.of(comments));
    }

    /**
     * Sets the comments for this node's value path, replacing any existing comments.
     * <p>
     * The existing comments are cleared and replaced with the provided comments. These comments
     * will appear above the value node in the generated configuration YAML file.
     *
     * @param comments the comments to set
     * @throws IllegalArgumentException if any of the provided comments are null
     * @since 3.0.0-paper-api.1.21-R0.1-SNAPSHOT
     */
    public final void setValueComments(final @NotNull String... comments) {
        this.clearValueComments();
        this.addValueComments(comments);
    }

    /**
     * Clears all comments associated with this node's value path.
     * <p>
     * After this method is called, the node will have no comments above its value path in the generated configuration YAML file.
     *
     * @since 3.0.0-paper-api.1.21-R0.1-SNAPSHOT
     */
    public final void clearValueComments() {
        this.valueComments.clear();
    }

    /**
     * Retrieves an unmodifiable list of inline comments associated with this node.
     * <p>
     * These comments appear inline with the node and its value in the generated configuration YAML file,
     * if the value is stored in the base node and not in a separate value node.
     *
     * @return an unmodifiable list of inline comments for this node
     * @since 3.0.0-paper-api.1.21-R0.1-SNAPSHOT
     */
    public final @NotNull @Unmodifiable List<String> getInlineComments() {
        return List.copyOf(this.inlineComments);
    }

    /**
     * Adds a single inline comment to this node.
     * <p>
     * The comment will be displayed inline with the node and its value in the generated configuration YAML file,
     * if applicable.
     *
     * @param comment the inline comment to add
     * @throws IllegalArgumentException if the provided comment is null
     * @since 3.0.0-paper-api.1.21-R0.1-SNAPSHOT
     */
    public final void addInlineComment(final @NotNull String comment) {
        Parameters.requireNonNull("comment", comment);
        this.inlineComments.add(comment);
    }

    /**
     * Adds multiple inline comments to this node.
     * <p>
     * The comments will be displayed inline with the node and its value in the generated configuration YAML file,
     * if applicable.
     *
     * @param comments the inline comments to add
     * @throws IllegalArgumentException if any of the provided comments are null
     * @since 3.0.0-paper-api.1.21-R0.1-SNAPSHOT
     */
    public final void addInlineComments(final @NotNull String... comments) {
        Parameters.requireAllNonNull("comments", comments);
        this.inlineComments.addAll(List.of(comments));
    }

    /**
     * Sets the inline comments for this node, replacing any existing comments.
     * <p>
     * The existing inline comments are cleared and replaced with the provided comments. These comments
     * will appear inline with the node and its value in the generated configuration YAML file,
     * if applicable.
     *
     * @param comments the inline comments to set
     * @throws IllegalArgumentException if any of the provided comments are null
     * @since 3.0.0-paper-api.1.21-R0.1-SNAPSHOT
     */
    public final void setInlineComments(final @NotNull String... comments) {
        this.clearInlineComments();
        this.addInlineComments(comments);
    }

    /**
     * Clears all inline comments associated with this node.
     * <p>
     * After this method is called, the node will have no inline comments in the generated configuration YAML file.
     *
     * @since 3.0.0-paper-api.1.21-R0.1-SNAPSHOT
     */
    public final void clearInlineComments() {
        this.inlineComments.clear();
    }

    /**
     * Retrieves an unmodifiable list of inline comments associated with the value of this node.
     * <p>
     * These comments appear inline with the node's value in the generated configuration YAML file,
     * if the value is stored in the value node rather than the base node.
     *
     * @return an unmodifiable list of inline value comments for this node
     * @since 3.0.0-paper-api.1.21-R0.1-SNAPSHOT
     */
    public final @NotNull @Unmodifiable List<String> getInlineValueComments() {
        return List.copyOf(this.inlineValueComments);
    }

    /**
     * Adds a single inline comment to the value of this node.
     * <p>
     * The comment will be displayed inline with the node's value in the generated configuration YAML file,
     * if applicable.
     *
     * @param comment the inline comment to add
     * @throws IllegalArgumentException if the provided comment is null
     * @since 3.0.0-paper-api.1.21-R0.1-SNAPSHOT
     */
    public final void addInlineValueComment(final @NotNull String comment) {
        Parameters.requireNonNull("comment", comment);
        this.inlineValueComments.add(comment);
    }

    /**
     * Adds multiple inline comments to the value of this node.
     * <p>
     * The comments will be displayed inline with the node's value in the generated configuration YAML file,
     * if applicable.
     *
     * @param comments the inline comments to add
     * @throws IllegalArgumentException if any of the provided comments are null
     * @since 3.0.0-paper-api.1.21-R0.1-SNAPSHOT
     */
    public final void addInlineValueComments(final @NotNull String... comments) {
        Parameters.requireAllNonNull("comments", comments);
        this.inlineValueComments.addAll(List.of(comments));
    }

    /**
     * Sets the inline comments for the value of this node, replacing any existing comments.
     * <p>
     * The existing inline value comments are cleared and replaced with the provided comments. These comments
     * will appear inline with the node's value in the generated configuration YAML file,
     * if applicable.
     *
     * @param comments the inline value comments to set
     * @throws IllegalArgumentException if any of the provided comments are null
     * @since 3.0.0-paper-api.1.21-R0.1-SNAPSHOT
     */
    public final void setInlineValueComments(final @NotNull String... comments) {
        this.clearInlineValueComments();
        this.addInlineValueComments(comments);
    }

    /**
     * Clears all inline comments associated with the value of this node.
     * <p>
     * After this method is called, the node's value will have no inline comments in the generated configuration YAML file.
     *
     * @since 3.0.0-paper-api.1.21-R0.1-SNAPSHOT
     */
    public final void clearInlineValueComments() {
        this.inlineValueComments.clear();
    }

    /**
     * Adds a child node to this node.
     * <p>
     * If a child node with the same identity already exists, it is replaced by the new one.
     * The existing child node is removed before adding the new child node to ensure that there
     * are no duplicate nodes with the same identity.
     *
     * @param child the child node to add
     * @return {@code true} if the child node was added successfully; {@code false} otherwise
     * @throws IllegalArgumentException if the provided child node is null
     * @since 3.0.0-paper-api.1.21-R0.1-SNAPSHOT
     */
    public final boolean add(final @NotNull Node<?> child) {
        Optional<Node<?>> matchingChild = this.children.stream().filter(x -> x.equals(child)).findAny();
        if (matchingChild.isPresent() && matchingChild.get() != child) {
            this.children.remove(matchingChild.get());
        }
        this.children.add(child);
        return true;
    }

    /**
     * Removes a child node from this node.
     * <p>
     * The child node is identified by its identity. If a node with the same identity is found
     * in the list of children, it is removed from the collection.
     *
     * @param child the child node to remove
     * @return {@code true} if the child node was removed successfully; {@code false} if the node was not found
     * @throws IllegalArgumentException if the provided child node is null
     * @since 3.0.0-paper-api.1.21-R0.1-SNAPSHOT
     */
    public final boolean remove(final @NotNull Object child) {
        Optional<Node<?>> matchingChild = this.children.stream().filter(x -> x.equals(child)).findAny();
        if (matchingChild.isPresent()) {
            this.children.remove(matchingChild);
            return true;
        }
        return false;
    }

    /**
     * Indicates whether some other object is "equal to" this one.
     * <p>
     * Two {@code Node} objects are equivalent to each other based entirely on whether their configuration file and base
     * paths are the same.
     * @param other The object to check for equivalence.
     * @return {@code true} if the objects have the same configuration file and base path, and {@code false} if they do
     *         not.
     * @since 3.0.0-paper-api.1.21-R0.1-SNAPSHOT
     */
    @Override
    public final boolean equals(final @Nullable Object other) {
        boolean isEqual = super.equals(other);
        if (!isEqual) {
            if (other instanceof Node<?> otherNode) {
                final @NotNull Configuration otherConfig = otherNode.getConfiguration();
                if (otherConfig == this.getConfiguration()) {
                    final @NotNull String otherBasePath = otherNode.getBasePath();
                    isEqual = otherBasePath.equals(this.getBasePath());
                }
            }
        }
        return isEqual;
    }

    /**
     * Constructs an anonymous node instance for handling basic configuration values.
     * <p>
     * This method creates a new {@code Node} with the specified name and parent node. It provides a way to handle
     * basic configuration values by using a {@link TriFunction} to retrieve the value from the configuration.
     * The {@code TriFunction} should generally map to a method that retrieves a value from the {@link Configuration} section,
     * such as {@code ConfigurationSection::getItemStack} or {@code ConfigurationSection::getOfflinePlayer}.
     * <p>
     * This method is mainly used by the {@link dev.satyrn.paperwasp.configuration.v1.ConfigurationTreeBuilder} to avoid writing repetitive code for different
     * types of configuration values.
     *
     * @param <T> the type of the value stored in the node
     * @param name the name of the node; should not be null
     * @param parent the parent node of this node; should not be null
     * @param getConfiguredValue a {@link TriFunction} that retrieves the value from the {@link Configuration} based on
     *                           the path and default value
     * @return a new {@code Node} instance configured with the provided parameters
     * @throws IllegalArgumentException if {@code name}, {@code parent}, or {@code getConfiguredValue} is null
     * @since 3.0.0-paper-api.1.21-R0.1-SNAPSHOT
     */
    public static <T> Node<T> getSimpleNode(final @NotNull String name,
                                            final @NotNull Node<?> parent,
                                            final @NotNull TriFunction<@NotNull Configuration, @NotNull String, @Nullable T, @Nullable T> getConfiguredValue) {
        Parameters.requireNonNull("name", name);
        Parameters.requireNonNull("parent", parent);
        Parameters.requireNonNull("getConfiguredValue", getConfiguredValue);
        return new Node<>(name, parent) {
            private T value;
            @Override
            public @Nullable T get() {
                return value;
            }

            @Override
            public void accept(@Nullable T value) {
                this.value = value;
            }

            @Override
            protected @Nullable T getConfiguredValue(@NotNull String path, @Nullable T defaultValue) {
                return getConfiguredValue.apply(this.getConfiguration(), path, defaultValue);
            }
        };
    }

    /**
     * Retrieves a configuration value of the specified type from the given configuration object.
     * <p>
     * This method handles common primitive and primitive-like types, including {@link Boolean}, {@link Double}, {@link Integer}, {@link Long},
     * {@link BigDecimal}, {@link BigInteger}, and {@link String}. If the requested type is not explicitly handled, it falls back to the
     * {@link Configuration#getObject(String, Class, Object)} method to retrieve the value.
     * <p>
     * If the configuration does not contain a value for the specified path, the method uses the provided default value. If the default value
     * is null and no value is found in the configuration, an appropriate default is used based on the type.
     *
     * @param <T> The type of the value to be retrieved.
     * @param valueClass The class object representing the type of the value.
     * @param configuration The configuration object from which the value is retrieved.
     * @param path The path within the configuration to retrieve the value from.
     * @param defaultValue The default value to use if the configuration does not contain a value for the specified path. May be null.
     * @return The retrieved value of type {@code T}, or {@code null} if no value is found and the default value is null.
     * @since 3.0.0-paper-api.1.21-R0.1-SNAPSHOT
     */
    @SuppressWarnings({"unchecked"})
    public static <T> T getConfiguredValue(final @NotNull Class<T> valueClass,
                                           final @NotNull Configuration configuration,
                                           final @NotNull String path,
                                           final @Nullable T defaultValue) {
        if (valueClass == Boolean.class) {
            return (T)Boolean.valueOf(configuration.getBoolean(path, defaultValue != null && (Boolean) defaultValue));
        } else if (valueClass == Float.class) {
            return (T)Float.valueOf((float) MathUtil.clamp(configuration.getDouble(path, defaultValue == null ? 0D : (Float)defaultValue), Float.MIN_VALUE, Float.MAX_VALUE));
        }else if (valueClass == Double.class) {
            return (T)Double.valueOf(configuration.getDouble(path, defaultValue == null ? 0D : (Double)defaultValue));
        } else if (valueClass == Byte.class) {
            return (T)Byte.valueOf((byte)MathUtil.clamp(configuration.getInt(path, defaultValue == null ? 0 : (Byte)defaultValue), Byte.MIN_VALUE, Byte.MAX_VALUE));
        } else if (valueClass == Short.class) {
            return (T)Short.valueOf((short)Math.clamp(configuration.getInt(path, defaultValue == null ? 0 : (Short)defaultValue), Short.MIN_VALUE, Short.MAX_VALUE));
        } else if (valueClass == Integer.class) {
            return (T)Integer.valueOf(configuration.getInt(path, defaultValue == null ? 0 : (Integer)defaultValue));
        } else if (valueClass == Long.class) {
            return (T)Long.valueOf(configuration.getLong(path, defaultValue == null ? 0L : (Long)defaultValue));
        } else if (valueClass == BigDecimal.class) {
            return (T)new BigDecimal(configuration.getString(path, defaultValue == null ? BigDecimal.ZERO.toPlainString() : ((BigDecimal)defaultValue).toPlainString()));
        } else if (valueClass == BigInteger.class) {
            return (T)new BigInteger(configuration.getString(path, defaultValue == null ? BigInteger.ZERO.toString() : defaultValue.toString()));
        } else if (valueClass == String.class) {
            return (T)configuration.getString(path, (String)defaultValue);
        } else if (valueClass == Character.class) {
            return (T)Character.valueOf(configuration.getString(path, defaultValue == null ? String.valueOf(Character.MIN_VALUE) : String.valueOf(((Character)defaultValue).charValue())).charAt(0));
        }
        return configuration.getObject(path, valueClass, defaultValue);
    }


    /**
     * Applies the current state of the node to the underlying {@link Configuration} object.
     * <p>
     * This method updates the configuration with the comments and inline comments associated with the
     * base path of the node, as well as the value path if the node contains both children and a value. It also recursively
     * applies the state of all child nodes.
     * <p>
     * Note that this method does not save the {@link Configuration} object to disk; manual saving
     * is required if the type of the configuration object supports it.
     * <p>
     * The application of comments and inline comments depends on the paths generated by the node's
     * {@link #getBasePath()} and {@link #getValuePath()} methods. The base path is used for comments
     * that appear above the node in the configuration, while the value path is used for comments
     * associated with the node's value.
     * <p>
     * Changes made to the node's value are set using {@link #setConfiguredValue(String, Object)}.
     * If the node has children, the method is called recursively on each child node.
     *
     * @since 3.0.0-paper-api.1.21-R0.1-SNAPSHOT
     */
    public final void apply() {
        final @NotNull String basePath = this.getBasePath();
        if (!basePath.isBlank()) {
            this.configuration.setComments(basePath, this.getComments());
            this.configuration.setInlineComments(basePath, this.getInlineComments());
        }
        final @NotNull String valuePath = this.getValuePath();
        if (!valuePath.isBlank()) {
            this.setConfiguredValue(this.get());
            if (!this.isEmpty()) {
                this.configuration.setComments(valuePath, this.getValueComments());
                this.configuration.setInlineComments(valuePath, this.getInlineValueComments());
            }
        }
        if (!this.isEmpty()) {
            for (final @NotNull Node<?> child : this.getChildren()) {
                child.apply();
            }
        }
    }

    /**
     * Refreshes the state of the node from the current state of the underlying {@link Configuration} object.
     * <p>
     * This method updates the node's state based on the comments and inline comments stored in the
     * configuration for the base path and value path of the node. It also sets the node's value from the
     * configuration and recursively refreshes all child nodes.
     * <p>
     * The base path is used to update comments and inline comments that appear above the node in the
     * configuration YAML file. The value path is used for comments and inline comments associated with
     * the node's value. If the node has children, the method is called recursively on each child node.
     * <p>
     * If the base path or value path is blank, no updates are made for that path. The method assumes that
     * the node's value and comments are properly represented in the configuration and will apply those
     * values to the node's state.
     *
     * @since 3.0.0-paper-api.1.21-R0.1-SNAPSHOT
     */
    public final void refresh() {
        final @NotNull String basePath = this.getValuePath();
        if (!basePath.isBlank()) {
            final List<String> comments = this.configuration.getComments(basePath);
            this.setComments(comments.toArray(new String[0]));
            final List<String> inlineComments = this.configuration.getInlineComments(basePath);
            this.setInlineComments(inlineComments.toArray(new String[0]));
        }
        final @NotNull String valuePath = this.getValuePath();
        if (!valuePath.isBlank()) {
            this.accept(this.getConfiguredValue());
            // If there aren't any children these will end up the same as the base path comments
            final List<String> valueComments = this.configuration.getComments(valuePath);
            this.setValueComments(valueComments.toArray(new String[0]));
            final List<String> inlineValueComments = this.configuration.getInlineComments(valuePath);
            this.setInlineValueComments(inlineValueComments.toArray(new String[0]));
        }
        if (!this.isEmpty()) {
            for (final @NotNull Node<?> child : this.getChildren()) {
                child.refresh();
            }
        }
    }

    /**
     * Returns the number of child nodes.
     *
     * @return the number of children nodes
     * @since 3.0.0-paper-api.1.21-R0.1-SNAPSHOT
     */
    @Override
    public final int size() {
        return this.getChildren().size();
    }

    /**
     * Checks if the specified object is present in the collection of child nodes.
     *
     * @param o the object to check for presence
     * @return {@code true} if the object is present, {@code false} otherwise
     * @since 3.0.0-paper-api.1.21-R0.1-SNAPSHOT
     */
    @Override
    public final boolean contains(Object o) {
        return this.getChildren().contains(o);
    }

    /**
     * Returns an iterator over the collection of child nodes.
     *
     * @return an iterator over the children nodes
     * @since 3.0.0-paper-api.1.21-R0.1-SNAPSHOT
     */
    @NotNull
    @Override
    public final Iterator<Node<?>> iterator() {
        return this.getChildren().iterator();
    }

    /**
     * Returns an array containing all child nodes.
     *
     * @return an array containing all children nodes
     * @since 3.0.0-paper-api.1.21-R0.1-SNAPSHOT
     */
    @NotNull
    @Override
    public final Object @NotNull [] toArray() {
        return this.getChildren().toArray();
    }

    /**
     * Returns an array containing all child nodes, using the provided array if it is large enough.
     *
     * @param a the array into which the elements of the collection are to be stored
     * @param <E> the type of the array
     * @return an array containing all children nodes
     * @since 3.0.0-paper-api.1.21-R0.1-SNAPSHOT
     */
    @NotNull
    @Override
    public final <E> E @NotNull [] toArray(@NotNull E @NotNull [] a) {
        return this.getChildren().toArray(a);
    }

    /**
     * Checks if all elements of the specified collection are present in the collection of child nodes.
     *
     * @param c the collection to check
     * @return {@code true} if all elements are present, {@code false} otherwise
     * @since 3.0.0-paper-api.1.21-R0.1-SNAPSHOT
     */
    @Override
    public final boolean containsAll(@NotNull Collection<?> c) {
        return this.getChildren().containsAll(c);
    }

    /**
     * Adds all elements of the specified collection to the collection of child nodes.
     *
     * @param c the collection of elements to add
     * @return {@code true} if the collection was modified as a result of the call, {@code false} otherwise
     * @since 3.0.0-paper-api.1.21-R0.1-SNAPSHOT
     */
    @Override
    public final boolean addAll(@NotNull Collection<? extends Node<?>> c) {
        return this.children.addAll(c);
    }

    /**
     * Removes all elements of the specified collection from the collection of child nodes.
     *
     * @param c the collection of elements to remove
     * @return {@code true} if the collection was modified as a result of the call, {@code false} otherwise
     * @since 3.0.0-paper-api.1.21-R0.1-SNAPSHOT
     */
    @Override
    public boolean removeAll(@NotNull Collection<?> c) {
        return this.children.removeAll(c);
    }

    /**
     * Retains only the elements in the collection of child nodes that are contained in the specified collection.
     *
     * @param c the collection of elements to retain
     * @return {@code true} if the collection was modified as a result of the call, {@code false} otherwise
     * @since 3.0.0-paper-api.1.21-R0.1-SNAPSHOT
     */
    @Override
    public boolean retainAll(@NotNull Collection<?> c) {
        return this.children.retainAll(c);
    }

    /**
     * Clears the collection of child nodes.
     *
     * @since 3.0.0-paper-api.1.21-R0.1-SNAPSHOT
     */
    @Override
    public void clear() {
        this.children.clear();
    }

    /**
     * A dummy implementation of the {@link Configuration} interface.
     * <p>
     * This class provides a no-op implementation of all methods in the {@link Configuration} interface,
     * throwing {@link NotImplementedException} for all operations. It is used as a placeholder in contexts where
     * a {@link Configuration} instance is required, but actual implementation is not needed.
     * </p>
     * <p>
     * This class is primarily used by {@link MovableNode} for scenarios where a configuration object is required
     * but its methods are not utilized before the node is moved into its parent and its configuration is updated to the
     * parent node's configuration.
     * </p>
     *
     * @since 3.0.0-paper-api.1.21-R0.1-SNAPSHOT
     */
    private static final class DummyConfiguration implements Configuration {
        @Override
        public @NotNull Set<String> getKeys(boolean b) {
            throw new NotImplementedException();
        }

        @Override
        public @NotNull Map<String, Object> getValues(boolean b) {
            throw new NotImplementedException();
        }

        @Override
        public boolean contains(@NotNull String s) {
            throw new NotImplementedException();
        }

        @Override
        public boolean contains(@NotNull String s, boolean b) {
            throw new NotImplementedException();
        }

        @Override
        public boolean isSet(@NotNull String s) {
            throw new NotImplementedException();
        }

        @Override
        public @Nullable String getCurrentPath() {
            throw new NotImplementedException();
        }

        @Override
        public @NotNull String getName() {
            throw new NotImplementedException();
        }

        @Override
        public @Nullable Configuration getRoot() {
            throw new NotImplementedException();
        }

        @Override
        public @Nullable ConfigurationSection getParent() {
            throw new NotImplementedException();
        }

        @Override
        public @Nullable Object get(@NotNull String s) {
            throw new NotImplementedException();
        }

        @Override
        public @Nullable Object get(@NotNull String s, @Nullable Object o) {
            throw new NotImplementedException();
        }

        @Override
        public void set(@NotNull String s, @Nullable Object o) {
            throw new NotImplementedException();
        }

        @Override
        public @NotNull ConfigurationSection createSection(@NotNull String s) {
            throw new NotImplementedException();
        }

        @Override
        public @NotNull ConfigurationSection createSection(@NotNull String s, @NotNull Map<?, ?> map) {
            throw new NotImplementedException();
        }

        @Override
        public @Nullable String getString(@NotNull String s) {
            throw new NotImplementedException();
        }

        @Override
        public @Nullable String getString(@NotNull String s, @Nullable String s1) {
            throw new NotImplementedException();
        }

        @Override
        public boolean isString(@NotNull String s) {
            throw new NotImplementedException();
        }

        @Override
        public int getInt(@NotNull String s) {
            throw new NotImplementedException();
        }

        @Override
        public int getInt(@NotNull String s, int i) {
            throw new NotImplementedException();
        }

        @Override
        public boolean isInt(@NotNull String s) {
            throw new NotImplementedException();
        }

        @Override
        public boolean getBoolean(@NotNull String s) {
            throw new NotImplementedException();
        }

        @Override
        public boolean getBoolean(@NotNull String s, boolean b) {
            throw new NotImplementedException();
        }

        @Override
        public boolean isBoolean(@NotNull String s) {
            throw new NotImplementedException();
        }

        @Override
        public double getDouble(@NotNull String s) {
            throw new NotImplementedException();
        }

        @Override
        public double getDouble(@NotNull String s, double v) {
            throw new NotImplementedException();
        }

        @Override
        public boolean isDouble(@NotNull String s) {
            throw new NotImplementedException();
        }

        @Override
        public long getLong(@NotNull String s) {
            throw new NotImplementedException();
        }

        @Override
        public long getLong(@NotNull String s, long l) {
            throw new NotImplementedException();
        }

        @Override
        public boolean isLong(@NotNull String s) {
            throw new NotImplementedException();
        }

        @Override
        public @Nullable List<?> getList(@NotNull String s) {
            throw new NotImplementedException();
        }

        @Override
        public @Nullable List<?> getList(@NotNull String s, @Nullable List<?> list) {
            throw new NotImplementedException();
        }

        @Override
        public boolean isList(@NotNull String s) {
            throw new NotImplementedException();
        }

        @Override
        public @NotNull List<String> getStringList(@NotNull String s) {
            throw new NotImplementedException();
        }

        @Override
        public @NotNull List<Integer> getIntegerList(@NotNull String s) {
            throw new NotImplementedException();
        }

        @Override
        public @NotNull List<Boolean> getBooleanList(@NotNull String s) {
            throw new NotImplementedException();
        }

        @Override
        public @NotNull List<Double> getDoubleList(@NotNull String s) {
            throw new NotImplementedException();
        }

        @Override
        public @NotNull List<Float> getFloatList(@NotNull String s) {
            throw new NotImplementedException();
        }

        @Override
        public @NotNull List<Long> getLongList(@NotNull String s) {
            throw new NotImplementedException();
        }

        @Override
        public @NotNull List<Byte> getByteList(@NotNull String s) {
            throw new NotImplementedException();
        }

        @Override
        public @NotNull List<Character> getCharacterList(@NotNull String s) {
            throw new NotImplementedException();
        }

        @Override
        public @NotNull List<Short> getShortList(@NotNull String s) {
            throw new NotImplementedException();
        }

        @Override
        public @NotNull List<Map<?, ?>> getMapList(@NotNull String s) {
            throw new NotImplementedException();
        }

        @Override
        public <T> @Nullable T getObject(@NotNull String s, @NotNull Class<T> aClass) {
            throw new NotImplementedException();
        }

        @Override
        public <T> @Nullable T getObject(@NotNull String s, @NotNull Class<T> aClass, @Nullable T t) {
            throw new NotImplementedException();
        }

        @Override
        public <T extends ConfigurationSerializable> @Nullable T getSerializable(@NotNull String s,
                                                                                 @NotNull Class<T> aClass) {
            throw new NotImplementedException();
        }

        @Override
        public <T extends ConfigurationSerializable> @Nullable T getSerializable(@NotNull String s,
                                                                                 @NotNull Class<T> aClass,
                                                                                 @Nullable T t) {
            throw new NotImplementedException();
        }

        @Override
        public @Nullable org.bukkit.util.Vector getVector(@NotNull String s) {
            throw new NotImplementedException();
        }

        @Override
        public @Nullable org.bukkit.util.Vector getVector(@NotNull String s, @Nullable Vector vector) {
            throw new NotImplementedException();
        }

        @Override
        public boolean isVector(@NotNull String s) {
            throw new NotImplementedException();
        }

        @Override
        public @Nullable OfflinePlayer getOfflinePlayer(@NotNull String s) {
            throw new NotImplementedException();
        }

        @Override
        public @Nullable OfflinePlayer getOfflinePlayer(@NotNull String s, @Nullable OfflinePlayer offlinePlayer) {
            throw new NotImplementedException();
        }

        @Override
        public boolean isOfflinePlayer(@NotNull String s) {
            throw new NotImplementedException();
        }

        @Override
        public @Nullable ItemStack getItemStack(@NotNull String s) {
            throw new NotImplementedException();
        }

        @Override
        public @Nullable ItemStack getItemStack(@NotNull String s, @Nullable ItemStack itemStack) {
            throw new NotImplementedException();
        }

        @Override
        public boolean isItemStack(@NotNull String s) {
            throw new NotImplementedException();
        }

        @Override
        public @Nullable Color getColor(@NotNull String s) {
            throw new NotImplementedException();
        }

        @Override
        public @Nullable Color getColor(@NotNull String s, @Nullable Color color) {
            throw new NotImplementedException();
        }

        @Override
        public boolean isColor(@NotNull String s) {
            throw new NotImplementedException();
        }

        @Override
        public @Nullable Location getLocation(@NotNull String s) {
            throw new NotImplementedException();
        }

        @Override
        public @Nullable Location getLocation(@NotNull String s, @Nullable Location location) {
            throw new NotImplementedException();
        }

        @Override
        public boolean isLocation(@NotNull String s) {
            throw new NotImplementedException();
        }

        @Override
        public @Nullable ConfigurationSection getConfigurationSection(@NotNull String s) {
            throw new NotImplementedException();
        }

        @Override
        public boolean isConfigurationSection(@NotNull String s) {
            throw new NotImplementedException();
        }

        @Override
        public @Nullable ConfigurationSection getDefaultSection() {
            throw new NotImplementedException();
        }

        @Override
        public void addDefault(@NotNull String s, @Nullable Object o) {
            throw new NotImplementedException();
        }

        @Override
        public @NotNull List<String> getComments(@NotNull String s) {
            throw new NotImplementedException();
        }

        @Override
        public @NotNull List<String> getInlineComments(@NotNull String s) {
            throw new NotImplementedException();
        }

        @Override
        public void setComments(@NotNull String s, @Nullable List<String> list) {
            throw new NotImplementedException();
        }

        @Override
        public void setInlineComments(@NotNull String s, @Nullable List<String> list) {
            throw new NotImplementedException();
        }

        @Override
        public void addDefaults(@NotNull Map<String, Object> map) {
            throw new NotImplementedException();
        }

        @Override
        public void addDefaults(@NotNull Configuration configuration) {
            throw new NotImplementedException();
        }

        @Override
        public void setDefaults(@NotNull Configuration configuration) {
            throw new NotImplementedException();
        }

        @Override
        public @Nullable Configuration getDefaults() {
            throw new NotImplementedException();
        }

        @Override
        public @NotNull ConfigurationOptions options() {
            throw new NotImplementedException();
        }
    }
}
