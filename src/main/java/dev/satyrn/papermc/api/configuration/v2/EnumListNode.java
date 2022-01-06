package dev.satyrn.papermc.api.configuration.v2;

import dev.satyrn.papermc.api.configuration.v1.ConfigurationContainer;
import dev.satyrn.papermc.api.configuration.v1.ConfigurationNode;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a configuration node with a list of enum values.
 *
 * @param <E> The enum type.
 * @author Isabel Maskrey
 * @since 1.0-SNAPSHOT
 */
@SuppressWarnings("unused")
public abstract class EnumListNode<E extends Enum<E>> extends ConfigurationNode<List<E>> {
    /**
     * Creates a new configuration node with a list of enum values.
     *
     * @param parent The parent configuration.
     * @param name   The node's name.
     * @since 1.0-SNAPSHOT
     */
    public EnumListNode(@NotNull final ConfigurationContainer parent, @NotNull final String name) {
        super(parent, name, parent.getConfig());
    }

    /**
     * Gets the value of the node.
     *
     * @return The value.
     * @since 1.0-SNAPSHOT
     */
    @Override
    @NotNull
    public List<E> value() {
        @NotNull final List<E> list = new ArrayList<>();
        @NotNull final List<String> values = this.getConfig().getStringList(this.getPath());
        for (final String value : values) {
            if (value != null && !value.isEmpty()) {
                try {
                    final E parsedValue = this.parse(value);
                    list.add(parsedValue);
                } catch (IllegalArgumentException ex) {
                    // Does not add the null value to the list.
                }
            }
        }
        return list;
    }

    /**
     * Parses the enum value.
     *
     * @param value The string value from the config file
     * @return The parsed enum value.
     * @throws IllegalArgumentException Thrown when an invalid value is parsed.
     * @since 1.0-SNAPSHOT
     */
    @NotNull
    protected abstract E parse(@NotNull final String value) throws IllegalArgumentException;
}
