package dev.satyrn.papermc.api.configuration.v1;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.logging.Level;

/**
 * Represents a configuration node with an enum value.
 *
 * @author Isabel Maskrey
 * @since 1.0.0
 */
@SuppressWarnings("unused")
public abstract class EnumNode<E extends Enum<E>> extends ConfigurationNode<E> {
    /**
     * Creates a new configuration node with an enum value.
     *
     * @param parent The parent container.
     * @param name   The node's name.
     *
     * @since 1.0.0
     */
    public EnumNode(final @NotNull ConfigurationNode<?> parent, final @NotNull String name) {
        super(parent, name);
    }

    /**
     * Returns the enum value of the node.
     *
     * @return The enum value.
     *
     * @since 1.0.0
     */
    @Override
    public final @NotNull E value() {
        final @Nullable String enumName = this.getConfig().getString(this.getValuePath());
        if (enumName != null && !enumName.isEmpty()) {
            try {
                return this.parse(enumName);
            } catch (IllegalArgumentException ex) {
                    this.getLogger()
                            .log(Level.WARNING, String.format("[Configuration] Invalid value for %s: %s. The default value %s will be used instead.", this.getValuePath(), enumName, this.defaultValue()));
                return this.getDefault();
            }
        }
        return this.getDefault();
    }

    /**
     * Parses the enum value.
     *
     * @param value The string value from the config file
     *
     * @return The parsed enum value.
     *
     * @throws IllegalArgumentException Thrown when the enum value parses.
     * @since 1.0.0
     */
    protected abstract @NotNull E parse(final @NotNull String value) throws IllegalArgumentException;

    /**
     * Gets the default enum value.
     *
     * @return The default enum value.
     *
     * @since 1.0.0
     */
    protected abstract @NotNull E getDefault();

    /**
     * Gets the default value of the node.
     *
     * @return The default value.
     *
     * @since 1.3.0
     */
    @Override
    public final @NotNull E defaultValue() {
        return getDefault();
    }
}
