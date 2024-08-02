package dev.satyrn.papermc.api.configuration.v1;

import dev.satyrn.papermc.api.util.v1.Cast;
import dev.satyrn.papermc.api.util.v1.MathHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a configuration node with an integer value.
 *
 * @author Isabel Maskrey
 * @since 1.0.0
 */
@SuppressWarnings("unused")
public class IntegerNode extends ConfigurationNode<Integer> implements ValueCaching<Integer> {
    // The minimum value of the node.
    private final int minValue;
    // The maximum value of the node.
    private final int maxValue;
    // The last value that was successfully read from the config.
    private int cachedValue = this.defaultValue();

    /**
     * Creates a new configuration node with an integer value.
     *
     * @param parent The parent container.
     * @param name   The node's name.
     *
     * @since 1.0.0
     */
    public IntegerNode(final @NotNull ConfigurationNode<?> parent, final @NotNull String name) {
        super(parent, name);
        this.minValue = Integer.MIN_VALUE;
        this.maxValue = Integer.MAX_VALUE;
    }

    /**
     * Creates a new configuration node with an integer value. The value is bounded by min and max.
     *
     * @param parent   The parent configuration container.
     * @param name     The node's name.
     * @param minValue The minimum value allowed by the node.
     * @param maxValue The maximum value allowed by the node.
     *
     * @since 1.6.2
     */
    public IntegerNode(final @NotNull ConfigurationNode<?> parent, final @NotNull String name, int minValue, int maxValue) {
        super(parent, name);
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    /**
     * Returns the integer value of the node.
     *
     * @return The integer value.
     *
     * @since 1.0.0
     */
    @Override
    public final @NotNull Integer value() {
        this.cachedValue = MathHelper.clamp(this.getConfig()
                .getInt(this.getValuePath(), this.cachedValue), this.minValue, this.maxValue);
        return this.cachedValue;
    }

    /**
     * Gets the default value of the node.
     * <p>
     * Defaults to {@code 0}.
     *
     * @return The default value.
     *
     * @since 1.3.0
     */
    @Override
    public @NotNull Integer defaultValue() {
        return 0;
    }

    /**
     * Sets the value of the node.
     *
     * @param value The value to set.
     *
     * @since 1.9.0
     */
    @Override
    public void setConfigValue(@Nullable Object value) {
        final int intValue = Cast.as(Integer.class, value, this::defaultValue);
        super.setConfigValue(MathHelper.clamp(intValue, this.minValue, this.maxValue));
    }

    /**
     * Gets the cached value of the node.
     *
     * @return The current value of the node.
     *
     * @since 2.0.0
     */
    @Override
    public final @NotNull Integer getCachedValue() {
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
    public final void setCachedValue(@NotNull Integer cachedValue) {
        this.cachedValue = cachedValue;
    }
}
