package dev.satyrn.papermc.api.configuration.v1;

import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

/**
 * Gets a map list from the config, using specified types instead of rawtypes.
 *
 * @param <K> The key type.
 * @param <V> The value type.
 * @author Isabel Maskrey
 * @since 1.9.0
 */
@SuppressWarnings("unused")
public abstract class TypedMapListNode<K, V> extends ConfigurationNode<List<Map<K, V>>> {
    /**
     * Creates a new typed map list node.
     *
     * @param parent The parent container.
     * @param name   The name of the node.
     * @since 1.3-SNAPSHOT
     */
    public TypedMapListNode(final @NotNull ConfigurationContainer parent, final @NotNull String name) {
        super(parent, name, parent.getConfig());
    }

    /**
     * Gets the value of the configuration node.
     *
     * @return The value as a list of maps.
     * @since 1.3-SNAPSHOT
     */
    public @NotNull List<Map<K, V>> value() {
        final List<Map<?, ?>> mapList = this.getConfig().getMapList(this.getValuePath());
        final List<Map<K, V>> result = new ArrayList<>();
        for (Map<?, ?> item : mapList) {
            Map<K, V> newItem = new HashMap<>();
            for (Map.Entry<?, ?> entry : item.entrySet()) {
                @Nullable K key;
                @Nullable V value;
                try {
                    key = this.getKeyFor(entry.getKey());
                    value = this.getValueFor(entry.getValue());
                    newItem.put(key, value);
                } catch (ClassCastException | IllegalArgumentException ex) {
                    final Plugin plugin = this.getPlugin();
                    if (plugin != null) {
                        plugin.getLogger()
                                .log(Level.WARNING, "[Configuration] Unable to read value in {0}: {1}. This entry has been discarded.", new Object[]{this.getValuePath(), ex.getMessage()});
                    }
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
     * @return The object as the given class.
     * @throws ClassCastException       If a class cast within the method fails.
     * @throws IllegalArgumentException If an illegal argument exception is thrown during conversion.
     * @since 1.3-SNAPSHOT
     */
    @Contract(value = "_ -> !null", pure = true)
    protected abstract @NotNull V getValueFor(Object value) throws ClassCastException, IllegalArgumentException;

    /**
     * Gets the value of a key in the map list.
     *
     * @param key The key value.
     * @return The key as the given class.
     * @throws ClassCastException       If a class cast within the method fails.
     * @throws IllegalArgumentException If an illegal argument exception is thrown during conversion.
     * @since 1.3-SNAPSHOT
     */
    @Contract(value = "_ -> !null", pure = true)
    protected abstract @NotNull K getKeyFor(Object key) throws ClassCastException, IllegalArgumentException;

    /**
     * Gets the default value of the node.
     *
     * @return The value.
     * @since 1.3-SNAPSHOT
     */
    @Override
    public final @NotNull List<Map<K, V>> defaultValue() {
        return new ArrayList<>();
    }
}
