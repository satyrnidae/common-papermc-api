package dev.satyrn.papermc.api.configuration.v1;

import dev.satyrn.papermc.api.util.v1.Cast;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;

/**
 * Represents a configurable BigDecimal value.
 *
 * @author Isabel Maskrey
 * @since 1.3.0
 */
@SuppressWarnings("unused")
public class BigDecimalNode extends ConfigurationNode<BigDecimal> implements ValueCaching<BigDecimal> {
    // Last successful read value.
    private @NotNull BigDecimal cachedValue = this.defaultValue();

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
        if (stringValue != null) {
            try {
                this.cachedValue = new BigDecimal(stringValue.trim());
            } catch (NumberFormatException ex) {
                this.getLogger().warning(String.format("Invalid value for BigDecimal node %s! Keeping current value %s.", this.getValuePath(), this.cachedValue));
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

    /**
     * Sets the value of the node in the configuration file.
     *
     * @param value The value to set.
     *
     * @since 1.10.0
     */
    @Override
    public void setConfigValue(@Nullable Object value) {
        BigDecimal bigDecimal = Cast.as(BigDecimal.class, value, this::defaultValue);
        super.setConfigValue(bigDecimal.toString());
    }

    /**
     * Gets the cached value of the node.
     *
     * @return The current value of the node.
     *
     * @since 2.0.0
     */
    @Override
    public final @NotNull BigDecimal getCachedValue() {
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
    public final void setCachedValue(@NotNull BigDecimal cachedValue) {
        this.cachedValue = cachedValue;
    }
}
