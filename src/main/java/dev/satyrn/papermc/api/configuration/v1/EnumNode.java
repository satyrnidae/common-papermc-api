package dev.satyrn.papermc.api.configuration.v1;

import dev.satyrn.papermc.api.util.v1.Cast;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a configuration node with an enum value.
 *
 * @param <E> The enum type
 *
 * @author Isabel Maskrey
 * @since 1.0.0
 */
@SuppressWarnings("unused")
public abstract class EnumNode<E extends Enum<E>> extends ConfigurationNode<E> implements ValueCaching<E> {
    private @NotNull E cachedValue = this.defaultValue();

    /**
     * Creates a new configuration node with an enum value.
     *
     * @param parent The parent container.
     * @param name   The node's name.
     *
     * @since 1.0.0
     */
    public EnumNode(final @NotNull ConfigurationNode<?> parent, final @NotNull String name) {
        super(parent, name);
    }

    /**
     * Returns the enum value of the node.
     *
     * @return The enum value.
     *
     * @since 1.0.0
     */
    @Override
    public final @NotNull E value() {
        final @Nullable String enumName = this.getConfig().getString(this.getValuePath());
        if (enumName != null && !enumName.isEmpty()) {
            try {
                this.cachedValue = this.parse(enumName);
            } catch (IllegalArgumentException ex) {
                this.getLogger()
                        .warning(String.format("Invalid value for %s: %s. The current value %s will be used instead.", this.getValuePath(), enumName, this.defaultValue()));
                this.getLogger().fine(ex::getMessage);
                for (var line : ex.getStackTrace()) {
                    this.getLogger().finest(line::toString);
                }
            }
        }
        return this.cachedValue;
    }

    /**
     * Parses the enum value.
     *
     * @param value The string value from the config file
     *
     * @return The parsed enum value.
     *
     * @throws IllegalArgumentException Thrown when the enum value parses.
     * @since 1.0.0
     */
    protected abstract @NotNull E parse(final @NotNull String value) throws IllegalArgumentException;

    /**
     * Gets the default enum value.
     *
     * @return The default enum value.
     *
     * @since 1.0.0
     */
    protected abstract @NotNull E getDefault();

    /**
     * Gets the default value of the node.
     *
     * @return The default value.
     *
     * @since 1.3.0
     */
    @Override
    public final @NotNull E defaultValue() {
        return getDefault();
    }

    /**
     * Sets the value of the node in the configuration file.
     *
     * @param value The value to set.
     *
     * @since 1.10.0
     */
    @Override
    public void setConfigValue(@Nullable Object value) {
        final E enumValue = Cast.as(value, this::getDefault);
        super.setConfigValue(enumValue.name());
    }

    /**
     * Gets the cached value of the node.
     *
     * @return The current value of the node.
     *
     * @since 2.0.0
     */
    @Override
    public final @NotNull E getCachedValue() {
        return this.cachedValue;
    }

    /**
     * Sets the current value of the node.
     *
     * @param cachedValue The current value of the node.
     *
     * @since 2.0.0
     */
    @Override
    public final void setCachedValue(@NotNull E cachedValue) {
        this.cachedValue = cachedValue;
    }
}
