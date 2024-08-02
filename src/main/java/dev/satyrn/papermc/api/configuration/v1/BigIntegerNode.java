package dev.satyrn.papermc.api.configuration.v1;

import dev.satyrn.papermc.api.util.v1.Cast;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigInteger;

/**
 * Represents a configurable BigInteger value.
 *
 * @author Isabel Maskrey
 * @since 1.9.0
 */
@SuppressWarnings("unused")
public class BigIntegerNode extends ConfigurationNode<BigInteger> implements ValueCaching<BigInteger> {
    // Returns the last successfully read value of the node.
    private @NotNull BigInteger cachedValue = this.defaultValue();

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
        if (stringValue != null) {
            try {
                this.cachedValue = new BigInteger(stringValue.trim());
            } catch (NumberFormatException ex) {
                this.getLogger()
                        .warning(String.format("Invalid value for BigInteger node %s! Using current value %s.", this.getValuePath(), this.cachedValue));
                this.getLogger().fine(ex::getMessage);
                for (var line : ex.getStackTrace()) {
                    this.getLogger().finest(line::toString);
                }
            }
        }
        return this.cachedValue;
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

    /**
     * Sets the value of the node in the configuration file.
     *
     * @param value The value to set.
     *
     * @since 1.10.0
     */
    @Override
    public void setConfigValue(@Nullable Object value) {
        BigInteger intValue = Cast.as(BigInteger.class, value, this::defaultValue);
        super.setConfigValue(intValue.toString());
    }

    /**
     * Gets the cached value of the node.
     *
     * @return The current value of the node.
     *
     * @since 2.0.0
     */
    @Override
    public final @NotNull BigInteger getCachedValue() {
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
    public final void setCachedValue(@NotNull BigInteger cachedValue) {
        this.cachedValue = cachedValue;
    }
}
