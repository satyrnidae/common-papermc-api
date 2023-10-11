package dev.satyrn.papermc.api.configuration.v5;

import dev.satyrn.papermc.api.configuration.v1.ConfigurationContainer;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a storage type node.
 *
 * @author Isabel Maskrey
 * @since 1.6.0
 *
 * @deprecated since 1.9.0 versioning refactor. To be removed in a future version; use {@link dev.satyrn.papermc.api.configuration.v1.StorageTypeNode} instead.
 */
@Deprecated(since = "1.9.0", forRemoval = true)
@SuppressWarnings("unused")
public final class StorageTypeNode extends dev.satyrn.papermc.api.configuration.v1.StorageTypeNode {
    /**
     * Creates a new configuration node with a Log Level value.
     *
     * @param parent The parent container.
     * @param name   The node's name.
     * @since 1.6.0
     */
    public StorageTypeNode(@NotNull ConfigurationContainer parent, @NotNull String name) {
        super(parent, name);
    }
}