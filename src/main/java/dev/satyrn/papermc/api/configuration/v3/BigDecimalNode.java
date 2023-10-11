package dev.satyrn.papermc.api.configuration.v3;

import dev.satyrn.papermc.api.configuration.v1.ConfigurationContainer;
import dev.satyrn.papermc.api.configuration.v1.ConfigurationNode;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;

/**
 * Represents a configurable BigDecimal value.
 *
 * @author Isabel Maskrey
 * @since 1.3-SNAPSHOT
 *
 * @deprecated Since 1.9.0 versioning refactor. To be removed in a future version; use {@link dev.satyrn.papermc.api.configuration.v1.BigDecimalNode} instead.
 */
@Deprecated(since = "1.9.0", forRemoval = true)
@SuppressWarnings("unused")
public class BigDecimalNode extends ConfigurationNode<BigDecimal> {
    /**
     * Initializes a new Configuration node.
     *
     * @param parent The parent node.
     * @param name   The node's name.
     * @since 1.3-SNAPSHOT
     */
    public BigDecimalNode(final @NotNull ConfigurationContainer parent, final @NotNull String name) {
        super(parent, name, parent.getConfig());
    }

    /**
     * Gets the value of the node.
     *
     * @return The value.
     * @since 1.3-SNAPSHOT
     */
    @Override
    public final @NotNull BigDecimal value() {
        final String stringValue = this.getConfig().getString(this.getPath());
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
     *
     * @return The value.
     * @since 1.3-SNAPSHOT
     */
    @Override
    public @NotNull BigDecimal defaultValue() {
        return BigDecimal.ZERO;
    }
}
