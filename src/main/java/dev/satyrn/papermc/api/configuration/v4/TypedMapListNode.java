package dev.satyrn.papermc.api.configuration.v4;

import dev.satyrn.papermc.api.configuration.v1.ConfigurationContainer;
import dev.satyrn.papermc.api.configuration.v1.ConfigurationNode;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class TypedMapListNode<K, V> extends ConfigurationNode<List<Map<K, V>>> {
    public TypedMapListNode(final @NotNull ConfigurationContainer parent, final @NotNull String name) {
        super(parent, name, parent.getConfig());
    }

    public @NotNull List<Map<K, V>> value() {
        final List<Map<?, ?>> mapList = this.getConfig().getMapList(this.getPath());
        final List<Map<K, V>> result = new ArrayList<>();
        for (Map<?, ?> item : mapList) {
            Map<K, V> newItem = new HashMap<>();
            for (Map.Entry<?, ?> entry : item.entrySet()) {
                K key = this.getKeyFor(entry.getKey());
                V value = this.getValueFor(entry.getValue());
                newItem.put(key, value);
            }
            result.add(newItem);
        }

        return result;
    }

    protected abstract V getValueFor(Object value);

    protected abstract K getKeyFor(Object key);

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
