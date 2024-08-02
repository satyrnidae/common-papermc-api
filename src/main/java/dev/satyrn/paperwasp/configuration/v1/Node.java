package dev.satyrn.paperwasp.configuration.v1;

import org.bukkit.configuration.Configuration;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

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

    private Node(final @Nullable String name,
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

    protected Node(final @NotNull Plugin plugin) {
        this(plugin.getConfig());
    }

    protected Node(final @NotNull Configuration config) {
        this(null, null, config);
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

    public final boolean hasChildren() {
        return !this.children.isEmpty();
    }

    public final @NotNull @Unmodifiable Collection<@NotNull Node<?>> getChildren() {
        return Set.copyOf(this.children);
    }

    public final boolean hasName() {
        return this.name != null && !this.name.isBlank();
    }

    public final @Nullable String getName() {
        return this.hasName() ? this.name : null;
    }

    public @NotNull String getValueNodeName() {
        return "value";
    }

    public final @NotNull String getBasePath() {
        return this.getBasePath(new StringBuilder());
    }

    @Contract(mutates = "param1")
    public final @NotNull String getBasePath(@NotNull StringBuilder stringBuilder) {
        if (this.parent != null) {
            parent.getBasePath(stringBuilder);
            if (parent.getParent() != null) {
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
            final @NotNull String valueNode = this.getValueNodeName();
            if (!valueNode.isBlank()) {
                stringBuilder.append('.').append(valueNode);
            }
        }
        return stringBuilder.toString();
    }

    protected final void addChild(final @NotNull Node<?> child) {
        boolean validChild = false;
        Optional<Node<?>> matchingChild = this.children.stream().filter(x -> x.equals(child)).findFirst();
        if (matchingChild.isPresent() && matchingChild.get() != child) {

        }
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
}
