package dev.satyrn.papermc.api.configuration.v4;

import dev.satyrn.papermc.api.configuration.v1.ConfigurationContainer;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a class which requires access to a Configuration Container and should be able to refresh its state when
 * the configuration is reloaded from disk.
 *
 * @param <T> The configuration class
 * @author Isabel Maskrey
 * @since 1.4-SNAPSHOT
 * @deprecated Since 1.6.1. Use configuration node values directly.
 */
@Deprecated
@SuppressWarnings("unused")
public interface ConfigurationConsumer<T extends ConfigurationContainer> {
    /**
     * Reloads the configuration file.
     *
     * @param configuration The configuration file.
     */
    void reloadConfiguration(final @NotNull T configuration);
}
