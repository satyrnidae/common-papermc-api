package dev.satyrn.papermc.api.configuration.v1;

import org.bukkit.configuration.Configuration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * Represents a single configuration node. Cannot contain sub-nodes or containers.
 *
 * @param <E> The value type.
 */
@SuppressWarnings("unused")
public abstract class ConfigurationNode<E> {
    /**
     * The parent configuration container.
     */
    private final transient @Nullable ConfigurationContainer parent;
    /**
     * The name of the node.
     */
    private final transient @NotNull String name;
    /**
     * The configuration file instance.
     */
    private final transient @NotNull Configuration config;

    /**
     * Initializes a new Configuration node.
     *
     * @param parent The parent node.
     */
    protected ConfigurationNode(final @Nullable ConfigurationContainer parent, final @NotNull String name, final @NotNull Configuration config) {
        this.parent = parent;
        this.name = name;
        this.config = config;
    }

    /**
     * Gets the configuration instance.
     *
     * @return The configuration instance.
     */
    public @NotNull Configuration getConfig() {
        return config;
    }

    /**
     * Gets the name of the node.
     *
     * @return The name of the node.
     */
    protected @NotNull String getName() {
        return this.name;
    }

    /**
     * Constructs the full node path.
     *
     * @return The full node path.
     */
    protected @NotNull String getPath() {
        return this.getPath(new StringBuilder());
    }

    /**
     * Constructs the full node path.
     *
     * @param stringBuilder The StringBuilder with which to build out the full node path.
     * @return The full node path.
     */
    public final @NotNull String getPath(final @NotNull StringBuilder stringBuilder) {
        if (parent != null) {
            parent.getPath(stringBuilder);
            stringBuilder.append('.');
        }
        stringBuilder.append(this.getName());
        return stringBuilder.toString();
    }

    /**
     * Returns the value of the node as a String.
     *
     * @return The value as a String.
     */
    @Override
    public final @NotNull String toString() {
        return this.value() == null ? "" : Objects.requireNonNull(this.value()).toString();
    }

    /**
     * Gets the value of the node.
     *
     * @return The value.
     */
    public abstract @Nullable E value();
}
