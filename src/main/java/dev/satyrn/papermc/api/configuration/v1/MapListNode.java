package dev.satyrn.papermc.api.configuration.v1;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

/**
 * Represents a configuration node with a map list value.
 *
 * @author Isabel Maskrey
 * @since 1.0-SNAPSHOT
 */
@SuppressWarnings("unused")
public final class MapListNode extends ConfigurationNode<List<Map<?,?>>> {
    /**
     * Creates a new configuration node with a Map list value.
     *
     * @param parent The parent container.
     * @param name The node's name.
     * @since 1.0-SNAPSHOT
     */
    public MapListNode(@NotNull final ConfigurationContainer parent, @NotNull final String name) {
        super(parent, name, parent.getConfig());
    }

    /**
     * Returns the entire configuration node as a map list.
     *
     * @return The entire configuration node as a map list.
     * @since 1.0-SNAPSHOT
     */
    @Override
    @NotNull
    public List<Map<?,?>> value() {
        return this.getConfig().getMapList(this.getPath());
    }
}
