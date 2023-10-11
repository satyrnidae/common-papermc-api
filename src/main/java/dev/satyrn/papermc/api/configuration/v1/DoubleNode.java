package dev.satyrn.papermc.api.configuration.v1;

import dev.satyrn.papermc.api.util.v1.MathHelper;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a configuration node with a double-precision floating point value.
 *
 * @author Isabel Maskrey
 * @since 1.0-SNAPSHOT
 */
@SuppressWarnings("unused")
public class DoubleNode extends ConfigurationNode<Double> {
    // The minimum value of the node.
    private final double minValue;
    // The maximum value of the node.
    private final double maxValue;

    /**
     * Creates a new configuration node with a double-precision floating point value.
     *
     * @param parent The parent container.
     * @param name   The node name.
     * @since 1.0-SNAPSHOT
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
     * @since 1.0-SNAPSHOT
     */
    @Override
    public final @NotNull Double value() {
        return MathHelper.clampd(this.getConfig()
                .getDouble(this.getValuePath(), this.defaultValue()), this.minValue, this.maxValue);
    }

    /**
     * Gets the default value of the node.
     *
     * @return The value.
     * @since 1.3-SNAPSHOT
     */
    @Override
    public @NotNull Double defaultValue() {
        return 0D;
    }

    /**
     * Sets the value of the node.
     *
     * @param value The value to set.
     * @since 1.9.0
     */
    @Override
    public void setValue(Double value) {
        this.getConfig().set(this.getValuePath(), value == null ? this.defaultValue() : MathHelper.clampd(value, this.minValue, this.maxValue));
    }
}
