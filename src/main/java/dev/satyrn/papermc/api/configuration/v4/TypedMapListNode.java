package dev.satyrn.papermc.api.configuration.v4;

import dev.satyrn.papermc.api.configuration.v1.ConfigurationContainer;
import org.jetbrains.annotations.NotNull;

/**
 * Gets a map list from the config, using specified types instead of rawtypes.
 *
 * @param <K> The key type.
 * @param <V> The value type.
 *
 * @author Isabel Maskrey
 * @since 1.3.0
 * @deprecated Since 1.9.0 versioning refactor. Use {@link dev.satyrn.papermc.api.configuration.v1.TypedMapListNode} instead.
 */
@Deprecated(since = "1.9.0", forRemoval = true)
@SuppressWarnings("unused")
public abstract class TypedMapListNode<K, V> extends dev.satyrn.papermc.api.configuration.v1.TypedMapListNode<K, V> {

    /**
     * Creates a new typed map list node.
     *
     * @param parent The parent container.
     * @param name   The name of the node.
     *
     * @since 1.3.0
     */
    public TypedMapListNode(@NotNull ConfigurationContainer parent, @NotNull String name) {
        super(parent, name);
    }
}