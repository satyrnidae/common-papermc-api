package dev.satyrn.papermc.api.configuration.v1;

import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;

/**
 * Represents a configurable BigInteger value.
 *
 * @author Isabel Maskrey
 * @since 1.9.0
 */
@SuppressWarnings("unused")
public class BigIntegerNode extends ConfigurationNode<BigInteger> {

    /**
     * Initializes a new Configuration node.
     *
     * @param parent The parent node.
     * @param name   The node name.
     *
     * @since 1.3.0
     */
    public BigIntegerNode(final @NotNull ConfigurationNode<?> parent, final @NotNull String name) {
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
    public final @NotNull BigInteger value() {
        final String stringValue = this.getConfig().getString(this.getValuePath());
        if (stringValue == null) {
            return this.defaultValue();
        }
        try {
            return new BigInteger(stringValue);
        } catch (NumberFormatException ex) {
            return this.defaultValue();
        }
    }

    /**
     * Gets the default value of the node.
     * <p>
     * Defaults to {@link BigInteger ZERO}.
     *
     * @return The default value.
     *
     * @since 1.3.0
     */
    @Override
    public @NotNull BigInteger defaultValue() {
        return BigInteger.ZERO;
    }
}
