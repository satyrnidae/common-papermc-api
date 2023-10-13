package dev.satyrn.papermc.api.configuration.v1;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

/**
 * A configuration node
 *
 * @param <T> The type of the configuration node.
 *
 * @author Isabel Maskrey
 * @since 1.9.1
 */
@SuppressWarnings("unused")
public abstract class OptionalConfigurationNode<T> extends ConfigurationNode<Optional<T>>{
    /**
     * Initializes a new Configuration node.
     *
     * @param parent The parent node.
     * @param name   The node name.
     *
     * @since 1.9.1
     */
    protected OptionalConfigurationNode(@NotNull ConfigurationNode<?> parent, @Nullable String name) {
        super(parent, name);
    }

    /**
     * Gets the value of the node.
     *
     * @return The value.
     *
     * @since 1.9.1
     */
    @Override
    public final @NotNull Optional<T> value() {
        T value = this.getActualValue();
        return value == null ? this.defaultValue() : Optional.of(value);
    }

    /**
     * Gets the default value of the node.
     *
     * @return The default value.
     *
     * @since 1.9.1
     */
    @Override
    public @NotNull Optional<T> defaultValue() {
        return Optional.empty();
    }

    /**
     * Gets the underlying value of the node.
     *
     * @return The actual value of the node.
     *
     * @since 1.9.1
     */
    public abstract @Nullable T getActualValue();

    /**
     * Writes the value of the node to the config file.
     *
     * @since 1.9.1
     */
    @Override
    public void save() {
        T value = this.getActualValue();
        if (this.isSubNode()) {
            this.getConfig().set(this.getValuePath(), value);
        }
        if (this.hasChildren()) {
            for (ConfigurationNode<?> child : this.getChildren()) {
                child.save();
            }
        }
    }
}
