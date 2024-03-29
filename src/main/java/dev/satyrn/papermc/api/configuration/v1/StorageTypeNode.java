package dev.satyrn.papermc.api.configuration.v1;

import dev.satyrn.papermc.api.storage.v1.StorageType;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a configuration node which stores a {@link StorageType} value.
 *
 * @author Isabel Maskrey
 * @since 1.6.0
 */
@SuppressWarnings("unused")
public class StorageTypeNode extends EnumNode<StorageType> {
    /**
     * Creates a new configuration node with a Log Level value.
     *
     * @param parent The parent container.
     * @param name   The node's name.
     *
     * @since 1.6.0
     */
    public StorageTypeNode(@NotNull ConfigurationNode<?> parent, @NotNull String name) {
        super(parent, name);
    }

    /**
     * Parses the enum value.
     *
     * @param value The string value from the config file
     * @return The parsed enum value.
     *
     * @throws IllegalArgumentException Thrown when the enum value fails to parse.
     * @since 1.6.0
     */
    @Override
    protected @NotNull StorageType parse(@NotNull String value) throws IllegalArgumentException {
        return StorageType.valueOf(value);
    }

    /**
     * Gets the default enum value.
     * <p>
     * Defaults to {@link StorageType FLAT_FILE}.
     *
     * @return The default enum value.
     * @since 1.6.0
     */
    @Override
    protected @NotNull StorageType getDefault() {
        return StorageType.FLAT_FILE;
    }
}
