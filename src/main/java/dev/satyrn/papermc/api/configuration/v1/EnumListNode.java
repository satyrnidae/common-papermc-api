package dev.satyrn.papermc.api.configuration.v1;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

/**
 * Represents a configuration node with a list of enum values.
 *
 * @param <E> The enum type.
 *
 * @author Isabel Maskrey
 * @since 1.0.0
 */
@SuppressWarnings("unused")
public abstract class EnumListNode<E extends Enum<E>> extends ConfigurationNode<List<E>> {
    /**
     * Creates a new configuration node with a list of enum values.
     *
     * @param parent The parent configuration.
     * @param name   The node's name.
     *
     * @since 1.0.0
     */
    public EnumListNode(final @NotNull ConfigurationNode<?> parent, final @NotNull String name) {
        super(parent, name);
    }

    /**
     * Gets the value of the node.
     *
     * @return The value.
     *
     * @since 1.0.0
     */
    @Override
    public @NotNull @Unmodifiable List<E> value() {
        final @NotNull List<E> list = new ArrayList<>();
        final @NotNull List<String> values = this.getConfig().getStringList(this.getValuePath());
        for (final @Nullable String value : values) {
            if (value != null && !value.isEmpty()) {
                try {
                    final E parsedValue = this.parse(value);
                    list.add(parsedValue);
                } catch (IllegalArgumentException ex) {
                        this.getLogger()
                                .log(Level.WARNING, String.format("[Configuration] The list %s contains an invalid value: %s. This entry has been discarded!", this.getValuePath(), value));
                }
            }
        }
        return List.copyOf(list);
    }

    /**
     * Parses the enum value.
     *
     * @param value The string value from the config file
     *
     * @return The parsed enum value.
     *
     * @throws IllegalArgumentException Thrown when an invalid value is parsed.
     * @since 1.0.0
     */
    protected abstract @NotNull E parse(final @NotNull String value) throws IllegalArgumentException;

    /**
     * Gets the default value of the node.
     * <p>
     * Defaults to an empty, unmodifiable list.
     *
     * @return The value.
     *
     * @since 1.3.0
     */
    @Override
    public final @NotNull @Unmodifiable List<E> defaultValue() {
        return List.of();
    }
}
