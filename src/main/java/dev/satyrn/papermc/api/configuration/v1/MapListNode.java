package dev.satyrn.papermc.api.configuration.v1;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;
import java.util.Map;

/**
 * Represents a configuration node with a map list value.
 *
 * @author Isabel Maskrey
 * @since 1.0.0
 */
@SuppressWarnings("unused")
public class MapListNode extends ConfigurationNode<List<Map<?, ?>>> {
    /**
     * Creates a new configuration node with a Map list value.
     *
     * @param parent The parent container.
     * @param name   The node's name.
     *
     * @since 1.0.0
     */
    public MapListNode(final @NotNull ConfigurationNode<?> parent, final @NotNull String name) {
        super(parent, name);
    }

    /**
     * Returns the entire configuration node as a map list.
     *
     * @return The entire configuration node as a map list.
     *
     * @since 1.0.0
     */
    @Override
    public @NotNull @Unmodifiable List<Map<?, ?>> value() {
        return this.getConfig().getMapList(this.getValuePath());
    }

    /**
     * Gets the default value of the node.
     * <p>
     * Defaults to an empty, unmodifiable list.
     *
     * @return The default value.
     *
     * @since 1.3.0
     */
    @Override
    public final @NotNull @Unmodifiable List<Map<?, ?>> defaultValue() {
        return List.of();
    }
}
