package dev.satyrn.papermc.api.configuration.v1;

import org.bukkit.configuration.Configuration;
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
    @Nullable
    private final transient ConfigurationContainer parent;
    // The name of the node.
    @NotNull
    private final transient String name;
    // The configuration file instance.
    @NotNull
    private final transient Configuration config;

    /**
     * Initializes a new Configuration node.
     *
     * @param parent The parent node.
     * @since 1.0-SNAPSHOT
     */
    protected ConfigurationNode(@Nullable final ConfigurationContainer parent, @NotNull final String name, @NotNull final Configuration config) {
        this.parent = parent;
        this.name = name;
        this.config = config;
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
    @NotNull
    public final String toString() {
        return this.value() == null ? "" : Objects.requireNonNull(this.value()).toString();
    }

    /**
     * Gets the value of the node.
     *
     * @return The value.
     * @since 1.0-SNAPSHOT
     */
    @Nullable
    public abstract T value();

    /**
     * Gets the default value of the node.
     *
     * @return The value.
     * @since 1.3-SNAPSHOT
     */
    @Nullable
    public abstract T defaultValue();
}
