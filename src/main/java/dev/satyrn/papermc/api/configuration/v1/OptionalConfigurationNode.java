package dev.satyrn.papermc.api.configuration.v1;

import dev.satyrn.papermc.api.util.v1.Cast;
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
        T value = this.getValue();
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
    public final @NotNull Optional<T> defaultValue() {
        T actualDefault = this.getDefault();
        return Optional.ofNullable(actualDefault);
    }

    /**
     * Gets the underlying value of the node.
     *
     * @return The actual value of the node.
     *
     * @since 1.9.1
     */
    @Deprecated(since = "1.10.2")
    public @Nullable T getActualValue() {
        return this.getValue();
    }

    /**
     * Gets the underlying value of the node.
     *
     * @return The actual value of the node.
     *
     * @since 1.10.2
     */
    public abstract @Nullable T getValue();

    /**
     * Gets the underlying default value of the node.
     *
     * @return The default value of the node.
     *
     * @since 1.10.2
     */
    public abstract @Nullable T getDefault();

    /**
     * Sets the value of the node in the configuration file.
     *
     * @param value The value to set.
     *
     * @since 1.10.0
     */
    @Override
    public void setConfigValue(@Nullable Object value) {
        Optional<T> optional = Cast.as(value, Optional::empty);
        super.setConfigValue(optional.orElseGet(this::getDefault));
    }
}
