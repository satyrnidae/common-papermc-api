package dev.satyrn.papermc.api.configuration.v1;

import dev.satyrn.papermc.api.util.v1.Cast;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.*;
import java.util.logging.Level;

/**
 * Gets a map list from the config, using specified types instead of rawtypes.
 *
 * @param <K> The key type.
 * @param <V> The value type.
 *
 * @author Isabel Maskrey
 * @since 1.3.0
 */
@SuppressWarnings("unused")
public abstract class TypedMapListNode<K, V> extends ConfigurationNode<List<Map<K, V>>> {
    /**
     * Creates a new typed map list node.
     *
     * @param parent The parent container.
     * @param name   The name of the node.
     *
     * @since 1.3.0
     */
    public TypedMapListNode(final @NotNull ConfigurationNode<?> parent, final @NotNull String name) {
        super(parent, name);
    }

    /**
     * Gets the value of the configuration node.
     *
     * @return The value as a list of maps.
     *
     * @since 1.3.0
     */
    public @NotNull @Unmodifiable List<Map<K, V>> value() {
        final List<Map<?, ?>> mapList = this.getConfig().getMapList(this.getValuePath());
        final List<Map<K, V>> result = new ArrayList<>();
        for (Map<?, ?> item : mapList) {
            Map<K, V> newItem = new HashMap<>();
            for (Map.Entry<?, ?> entry : item.entrySet()) {
                @NotNull K key;
                @Nullable V value;
                try {
                    key = this.getKeyFor(entry.getKey());
                    value = this.getValueFor(entry.getValue());
                    newItem.put(key, value);
                } catch (ClassCastException | IllegalArgumentException ex) {
                    final Plugin plugin = this.getPlugin();
                        this.getLogger()
                                .log(Level.WARNING, "[Configuration] Unable to read value in {0}: {1}. This entry has been discarded.", new Object[]{this.getValuePath(), ex.getMessage()});
                }
            }
            result.add(newItem);
        }

        return result;
    }

    /**
     * Gets the value of an object in the map list.
     *
     * @param value The object value.
     *
     * @return The object as the given class.
     *
     * @since 1.3.0
     */
    @Contract(value = "null -> null; !null -> _", pure = true)
    protected @Nullable V getValueFor(@Nullable Object value) {
        if (value == null) {
            return null;
        }
        Optional<V> cast = Cast.as(value);
        return cast.isEmpty() ? null : cast.get();
    }

    /**
     * Gets the value of a key in the map list.
     *
     * @param key The key value.
     * @return The key as the given class.
     *
     * @throws IllegalArgumentException If the key is null, or does not conform to the expected class K.
     * @since 1.3.0
     */
    @Contract(value = "_ -> !null", pure = true)
    protected @NotNull K getKeyFor(@NotNull Object key) throws IllegalArgumentException {
        Optional<K> cast = Cast.as(key);
        if (cast.isEmpty()) {
            throw new IllegalArgumentException("key was not assignable to key type, or was null.");
        }
        return cast.get();
    }

    /**
     * Gets the default value of the node.
     *
     * @return The value.
     *
     * @since 1.3.0
     */
    @Override
    public final @NotNull @Unmodifiable List<Map<K, V>> defaultValue() {
        return List.of();
    }
}
