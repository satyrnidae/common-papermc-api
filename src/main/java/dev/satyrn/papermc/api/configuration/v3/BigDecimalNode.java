package dev.satyrn.papermc.api.configuration.v3;

import dev.satyrn.papermc.api.configuration.v1.ConfigurationContainer;
import org.jetbrains.annotations.NotNull;

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
public class BigDecimalNode extends dev.satyrn.papermc.api.configuration.v1.BigDecimalNode {
    /**
     * Initializes a new Configuration node.
     *
     * @param parent The parent node.
     * @param name   The node's name.
     *
     * @since 1.3.0
     */
    public BigDecimalNode(@NotNull ConfigurationContainer parent, @NotNull String name) {
        super(parent, name);
    }
}
