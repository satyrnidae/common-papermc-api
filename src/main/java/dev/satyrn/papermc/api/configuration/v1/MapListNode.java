package dev.satyrn.papermc.api.configuration.v1;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

/**
 * Represents a configuration node with a MapList value.
 */
@SuppressWarnings("unused")
public final class MapListNode extends ConfigurationNode<List<Map<?,?>>> {
    /**
     * Creates a new configuration node with a Map list value.
     *
     * @param parent The parent container.
     * @param name The node's name.
     */
    public MapListNode(final @NotNull ConfigurationContainer parent, final @NotNull String name) {
        super(parent, name, parent.getConfig());
    }

    /**
     * Returns the entire configuration node as a Map list.
     *
     * @return The entire configuration node as a Map list.
     */
    @Override
    public @NotNull List<Map<?,?>> value() {
        return this.getConfig().getMapList(this.getPath());
    }
}
