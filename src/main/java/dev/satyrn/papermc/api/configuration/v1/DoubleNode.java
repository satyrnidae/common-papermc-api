package dev.satyrn.papermc.api.configuration.v1;

import dev.satyrn.papermc.api.util.v1.Cast;
import dev.satyrn.papermc.api.util.v1.MathHelper;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a configuration node with a double-precision floating point value.
 *
 * @author Isabel Maskrey
 * @since 1.0.0
 */
@SuppressWarnings("unused")
public class DoubleNode extends ConfigurationNode<Double> implements ValueCaching<Double> {
    // The minimum value of the node.
    private final double minValue;
    // The maximum value of the node.
    private final double maxValue;
    // The last successfully read value of the node
    private double cachedValue = this.defaultValue();

    /**
     * Creates a new configuration node with a double-precision floating point value.
     *
     * @param parent The parent container.
     * @param name   The node name.
     *
     * @since 1.0.0
     */
    public DoubleNode(final @NotNull ConfigurationNode<?> parent, final @NotNull String name) {
        super(parent, name);
        this.minValue = Double.MIN_VALUE;
        this.maxValue = Double.MAX_VALUE;
    }

    /**
     * Creates a new configuration node with a double-precision floating point value.
     *
     * @param parent   The parent container.
     * @param name     The node name.
     * @param minValue The minimum value of the node.
     * @param maxValue The maximum value of the node.
     *
     * @since 1.6.2
     */
    public DoubleNode(final @NotNull ConfigurationNode<?> parent, final @NotNull String name, final double minValue, final double maxValue) {
        super(parent, name);
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    /**
     * Returns the double-precision floating point value of the node.
     *
     * @return The double-precision floating point value.
     *
     * @since 1.0.0
     */
    @Override
    public final @NotNull Double value() {
        this.cachedValue = MathHelper.clampd(this.getConfig()
                .getDouble(this.getValuePath(), this.cachedValue), this.minValue, this.maxValue);
        return this.cachedValue;
    }

    /**
     * Gets the default value of the node.
     * <p>
     * Defaults to {@code 0D}.
     *
     * @return The default value.
     *
     * @since 1.3.0
     */
    @Override
    public @NotNull Double defaultValue() {
        return 0D;
    }

    /**
     * Sets the value of the node.
     *
     * @param value The value to set.
     *
     * @since 1.9.0
     */
    @Override
    public void setConfigValue(Object value) {
        final double doubleValue = Cast.as(Double.class, value, this::defaultValue);
        super.setConfigValue(MathHelper.clampd(doubleValue, this.minValue, this.maxValue));
    }

    /**
     * Gets the cached value of the node.
     *
     * @return The current value of the node.
     *
     * @since 2.0.0
     */
    @Override
    public final @NotNull Double getCachedValue() {
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
    public void setCachedValue(@NotNull Double cachedValue) {
        this.cachedValue = cachedValue;
    }
}
