package dev.satyrn.papermc.api.configuration.v3;

import dev.satyrn.papermc.api.configuration.v1.ConfigurationContainer;
import dev.satyrn.papermc.api.configuration.v1.ConfigurationNode;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;

/**
 * Represents a configurable BigInteger value.
 *
 * @author Isabel Maskrey
 * @since 1.3-SNAPSHOT
 */
public class BigIntegerNode extends ConfigurationNode<BigInteger> {

    /**
     * Initializes a new Configuration node.
     *
     * @param parent The parent node.
     * @param name The node name.
     * @since 1.0-SNAPSHOT
     */
    public BigIntegerNode(@NotNull ConfigurationContainer parent, @NotNull String name) {
        super(parent, name, parent.getConfig());
    }

    /**
     * Gets the value of the node.
     *
     * @return The value.
     * @since 1.0-SNAPSHOT
     */
    @Override
    public final @NotNull BigInteger value() {
        final String stringValue = this.getConfig().getString(this.getPath());
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
     *
     * @return The value.
     * @since 1.3-SNAPSHOT
     */
    @Override
    public @NotNull BigInteger defaultValue() {
        return BigInteger.ZERO;
    }
}
