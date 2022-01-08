package dev.satyrn.papermc.api.configuration.v1;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a configuration node with a Log Level value.
 *
 * @author Isabel Maskrey
 * @since 1.0-SNAPSHOT
 */
@SuppressWarnings("unused")
public abstract class EnumNode<E extends Enum<E>> extends ConfigurationNode<E> {
    /**
     * Creates a new configuration node with a Log Level value.
     *
     * @param parent The parent container.
     * @param name   The node's name.
     * @since 1.0-SNAPSHOT
     */
    public EnumNode(final @NotNull ConfigurationContainer parent, final @NotNull String name) {
        super(parent, name, parent.getConfig());
    }

    /**
     * Returns the Log Level value of the node.
     *
     * @return The Log Level value.
     * @since 1.0-SNAPSHOT
     */
    @Override
    public final @NotNull E value() {
        final @Nullable String enumName = this.getConfig().getString(this.getPath());
        if (enumName != null && !enumName.isEmpty()) {
            try {
                return this.parse(enumName);
            } catch (IllegalArgumentException ex) {
                return this.getDefault();
            }
        }
        return this.getDefault();
    }

    /**
     * Parses the enum value.
     *
     * @param value The string value from the config file
     * @return The parsed enum value.
     * @throws IllegalArgumentException Thrown when the enum value parses.
     * @since 1.0-SNAPSHOT
     */
    protected abstract @NotNull E parse(final @NotNull String value) throws IllegalArgumentException;

    /**
     * Gets the default enum value.
     *
     * @return The default enum value.
     * @since 1.0-SNAPSHOT
     */
    protected abstract @NotNull E getDefault();

    /**
     * Gets the default value of the node.
     *
     * @return The value.
     * @since 1.3-SNAPSHOT
     */
    @Override
    public final @NotNull E defaultValue() {
        return getDefault();
    }
}
