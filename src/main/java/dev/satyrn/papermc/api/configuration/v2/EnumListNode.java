package dev.satyrn.papermc.api.configuration.v2;

import dev.satyrn.papermc.api.configuration.v1.ConfigurationContainer;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a configuration node with a list of enum values.
 *
 * @param <E> The enum type.
 * @author Isabel Maskrey
 * @since 1.0-SNAPSHOT
 *
 * @deprecated since 1.9.0 versioning refactor. To be removed in a future version; use {@link dev.satyrn.papermc.api.configuration.v1.EnumListNode}
 */
@Deprecated(since = "1.9.0", forRemoval = true)
@SuppressWarnings("unused")
public abstract class EnumListNode<E extends Enum<E>> extends dev.satyrn.papermc.api.configuration.v1.EnumListNode<E> {
    /**
     * Creates a new configuration node with a list of enum values.
     *
     * @param parent The parent configuration.
     * @param name   The node's name.
     * @since 1.0-SNAPSHOT
     */
    public EnumListNode(@NotNull ConfigurationContainer parent, @NotNull String name) {
        super(parent, name);
    }
}
