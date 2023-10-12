package dev.satyrn.papermc.api.configuration.v1;

import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;

/**
 * Represents a configurable BigDecimal value.
 *
 * @author Isabel Maskrey
 * @since 1.3.0
 */
@SuppressWarnings("unused")
public class BigDecimalNode extends ConfigurationNode<BigDecimal> {
    /**
     * Initializes a new Configuration node.
     *
     * @param parent The parent node.
     * @param name   The node's name.
     *
     * @since 1.3.0
     */
    public BigDecimalNode(final @NotNull ConfigurationNode<?> parent, final @NotNull String name) {
        super(parent, name);
    }

    /**
     * Gets the value of the node.
     *
     * @return The value.
     *
     * @since 1.3.0
     */
    @Override
    public final @NotNull BigDecimal value() {
        final String stringValue = this.getConfig().getString(this.getValuePath());
        if (stringValue == null) {
            return this.defaultValue();
        }
        try {
            return new BigDecimal(stringValue);
        } catch (NumberFormatException ex) {
            return this.defaultValue();
        }
    }

    /**
     * Gets the default value of the node.
     * <p>
     * Defaults to {@link BigDecimal ZERO}.
     *
     * @return The default value.
     *
     * @since 1.3.0
     */
    @Override
    public @NotNull BigDecimal defaultValue() {
        return BigDecimal.ZERO;
    }
}
