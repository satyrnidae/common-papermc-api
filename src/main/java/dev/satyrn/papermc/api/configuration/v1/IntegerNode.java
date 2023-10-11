package dev.satyrn.papermc.api.configuration.v1;

import dev.satyrn.papermc.api.util.v1.MathHelper;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a configuration node with an integer value.
 *
 * @author Isabel Maskrey
 * @since 1.0-SNAPSHOT
 */
@SuppressWarnings("unused")
public class IntegerNode extends ConfigurationNode<Integer> {
    // The minimum value of the node.
    private final int minValue;
    // The maximum value of the node.
    private final int maxValue;

    /**
     * Creates a new configuration node with an integer value.
     *
     * @param parent The parent container.
     * @param name   The node's name.
     * @since 1.0-SNAPSHOT
     */
    public IntegerNode(final @NotNull ConfigurationContainer parent, final @NotNull String name) {
        super(parent, name, parent.getConfig());
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
     * @since 1.6.2
     */
    public IntegerNode(final @NotNull ConfigurationContainer parent, final @NotNull String name, int minValue, int maxValue) {
        super(parent, name, parent.getConfig());
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    /**
     * Returns the integer value of the node.
     *
     * @return The integer value.
     * @since 1.0-SNAPSHOT
     */
    @Override
    public final @NotNull Integer value() {
        return MathHelper.clamp(this.getConfig()
                .getInt(this.getValuePath(), this.defaultValue()), this.minValue, this.maxValue);
    }

    /**
     * Gets the default value of the node.
     *
     * @return The value.
     * @since 1.3-SNAPSHOT
     */
    @Override
    public @NotNull Integer defaultValue() {
        return 0;
    }

    /**
     * Sets the value of the node.
     *
     * @param value The value to set.
     * @since 1.9.0
     */
    @Override
    public void setValue(Integer value) {
        this.getConfig().set(this.getValuePath(), value == null ? this.defaultValue() : MathHelper.clamp(value, this.minValue, this.maxValue));
    }
}
