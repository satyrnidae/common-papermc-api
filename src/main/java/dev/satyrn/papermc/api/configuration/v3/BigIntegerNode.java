package dev.satyrn.papermc.api.configuration.v3;

import dev.satyrn.papermc.api.configuration.v1.ConfigurationContainer;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a configurable BigInteger value.
 *
 * @author Isabel Maskrey
 * @since 1.3-SNAPSHOT
 *
 * @deprecated since 1.9.0 versioning refactor. To be removed in a future version; use {@link dev.satyrn.papermc.api.configuration.v1.BigIntegerNode} instead.
 */
@Deprecated(since = "1.9.0", forRemoval = true)
@SuppressWarnings("unused")
public class BigIntegerNode extends dev.satyrn.papermc.api.configuration.v1.BigIntegerNode {
    /**
     * Initializes a new Configuration node.
     *
     * @param parent The parent node.
     * @param name   The node name.
     * @since 1.0-SNAPSHOT
     */
    public BigIntegerNode(@NotNull ConfigurationContainer parent, @NotNull String name) {
        super(parent, name);
    }
}
