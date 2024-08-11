package dev.satyrn.paperwasp.configuration.v1;

import org.bukkit.configuration.Configuration;
import org.checkerframework.common.returnsreceiver.qual.This;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Represents the base class of a configuration node.
 *
 * @param <T> The type of the data contained within the node.
 * @author Isabel Maskrey
 * @since 3.0.0-paper-api.1.21-R0.1-SNAPSHOT
 */
public abstract class Node<T> {
    private final @Nullable Node<?> parent;
    private final @Nullable String name;
    private final @NotNull Configuration config;
    private final @NotNull Collection<@NotNull Node<?>> children = new HashSet<>();
    private final @NotNull List<String> comments = new ArrayList<>();
    private final @NotNull List<String> valueComments = new ArrayList<>();
    protected T value;
    protected T defaultValue;

    protected Node(final @Nullable String name,
                   final @Nullable Node<?> parent,
                   final @NotNull Configuration config) {
        this.name = name;
        this.parent = parent;
        this.config = config;
    }

    protected Node(final @Nullable String name,
                   final @NotNull Node<?> parent) {
        this(name, parent, parent.getConfig());
        this.parent.addChild(this);
    }

    public abstract @Nullable T getValue();

    public abstract void setValue(final @Nullable T value);

    public abstract @Nullable BiFunction<String, T, T> getConfigGetFunction();

    public abstract @Nullable BiConsumer<String, T> getConfigSetFunction();

    protected final void setValueInConfig(final @Nullable T value) {
        final @Nullable BiConsumer<String, T> configSetFunction = this.getConfigSetFunction();
        if (configSetFunction != null) {
            final @NotNull String valuePath = this.getValuePath();
            if (!valuePath.isBlank()) {
                this.getConfigSetFunction().accept(valuePath, this.getDefaultValue());
            }
        }
    }

    protected final @Nullable T getValueFromConfig() {
        final @Nullable BiFunction<String, T, T> configGetFunction = this.getConfigGetFunction();
        if (configGetFunction != null) {
            final @NotNull String valuePath = this.getValuePath();
            if (!valuePath.isBlank()) {
                return this.getConfigGetFunction().apply(valuePath, this.getDefaultValue());
            }
        }
        return null;
    }

    public @Nullable T getDefaultValue() {
        return this.defaultValue;
    }

    /**
     * Gets the configuration instance for the configuration node.
     * @return The configuration instance.
     * @since 3.0.0-paper-api.1.21-R0.1-SNAPSHOT
     */
    public final @NotNull Configuration getConfig() {
        return this.config;
    }

    public final @Nullable Node<?> getParent() {
        return this.parent;
    }

    public final boolean hasParent() {
        return this.parent != null;
    }

    public final @NotNull @Unmodifiable Collection<@NotNull Node<?>> getChildren() {
        return Set.copyOf(this.children);
    }

    public final boolean hasChildren() {
        return !this.children.isEmpty();
    }

    public final @Nullable String getName() {
        return this.hasName() ? this.name : null;
    }

    public final boolean hasName() {
        return this.name != null && !this.name.isBlank();
    }

    public final @NotNull String getBasePath() {
        return this.getBasePath(new StringBuilder());
    }

    @Contract(mutates = "param1")
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

    public final @NotNull String getValuePath() {
        return this.getValuePath(new StringBuilder());
    }

    @Contract(mutates = "param1")
    public final @NotNull String getValuePath(@NotNull StringBuilder stringBuilder) {
        this.getBasePath(stringBuilder);
        if (this.parent != null && this.hasChildren()) {
            stringBuilder.append(".value");
        }
        return stringBuilder.toString();
    }

    public final @NotNull @Unmodifiable List<String> getComments() {
        return List.copyOf(this.comments);
    }

    public final @This Node<T> addComment(final @NotNull String comment) {
        this.comments.add(comment);
        return this;
    }

    public final @This Node<T> addComments(final @NotNull String... comments) {
        this.comments.addAll(List.of(comments));
        return this;
    }

    public final @This Node<T> setComments(final @NotNull String... comments) {
        this.comments.clear();
        return this.addComments(comments);
    }

    public final @NotNull @Unmodifiable List<String> getValueComments() {
        return List.copyOf(this.valueComments);
    }

    public final @This Node<T> addValueComment(final @NotNull String comment) {
        this.valueComments.add(comment);
        return this;
    }

    public final @This Node<T> addValueComments(final @NotNull String... comments) {
        this.valueComments.addAll(List.of(comments));
        return this;
    }

    public final @This Node<T> setValueComments(final @NotNull String... comments) {
        this.valueComments.clear();
        return this.addValueComments(comments);
    }

    /**
     * Adds a child node to this node.
     * <p>
     * If this node contains a value, the value of this node will be able to be accessed via the {@link #getValuePath()}
     * path.
     * <p>
     * If the added node matches a child node that was previously added to this node, that child node will be removed
     * and replaced with the new child node.
     * @param child The child node to add.
     * @since 3.0.0-paper-api.1.21-R0.1-SNAPSHOT
     */
    protected final void addChild(final @NotNull Node<?> child) {
        Optional<Node<?>> matchingChild = this.children.stream().filter(x -> x.equals(child)).findFirst();
        if (matchingChild.isPresent() && matchingChild.get() != child) {
            this.children.remove(matchingChild.get());
        }
        this.children.add(child);
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
    public boolean equals(final @Nullable Object other) {
        boolean isEqual = super.equals(other);
        if (!isEqual) {
            if (other instanceof Node<?> otherNode) {
                final @NotNull Configuration otherConfig = otherNode.getConfig();
                if (otherConfig == this.getConfig()) {
                    final @NotNull String otherBasePath = otherNode.getBasePath();
                    isEqual = otherBasePath.equals(this.getBasePath());
                }
            }
        }
        return isEqual;
    }

    /**
     * Applies the current value in the node to the underlying configuration.
     * <p>
     * This does not "save" the configuration; the configuration will still need to be persisted if that is desired.
     * @since 3.0.0-paper-api.1.21-R0.1-SNAPSHOT
     */
    public final @This Node<T> apply() {
        final @NotNull String basePath = this.getBasePath();
        if (!basePath.isBlank()) {
            this.config.setComments(basePath, this.getComments());
        }
        final @NotNull String valuePath = this.getValuePath();
        if (!valuePath.isBlank()) {
            this.setValueInConfig(this.getValue());
            if (this.hasChildren()) {
                this.config.setComments(valuePath, this.getValueComments());
            }
        }
        if (this.hasChildren()) {
            for (final @NotNull Node<?> child : this.getChildren()) {
                child.apply();
            }
        }
        return this;
    }

    /**
     * Reloads the node from the config.
     * @since 3.0.0-paper-api.1.21-R0.1-SNAPSHOT
     */
    public final @This Node<T> refresh() {
        final @NotNull String basePath = this.getValuePath();
        if (!basePath.isBlank()) {
            final List<String> comments = this.config.getComments(basePath);
            this.setComments(comments.toArray(new String[0]));
        }
        final @NotNull String valuePath = this.getValuePath();
        if (!valuePath.isBlank()) {
            this.setValue(this.getValueFromConfig());
            if (this.hasChildren()) {
                final List<String> valueComments = this.config.getComments(valuePath);
                this.setValueComments(valueComments.toArray(new String[0]));
            }
        }
        if (this.hasChildren()) {
            for (final @NotNull Node<?> child : this.getChildren()) {
                child.refresh();
            }
        }
        return this;
    }
}
