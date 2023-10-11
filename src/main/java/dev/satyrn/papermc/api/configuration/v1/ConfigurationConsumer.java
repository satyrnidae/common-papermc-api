package dev.satyrn.papermc.api.configuration.v1;

import org.jetbrains.annotations.NotNull;

/**
 * Represents a class which requires access to a Configuration Container and should be able to refresh its state when
 * the configuration is reloaded from disk.
 *
 * @param <T> The configuration class
 * @author Isabel Maskrey
 * @since 1.9.0
 * @deprecated Since 1.6.1. Use configuration node values directly.
 */
@Deprecated(since = "1.6.1")
@SuppressWarnings("unused")
public interface ConfigurationConsumer<T extends ConfigurationContainer> {
    /**
     * Reloads the configuration file.
     *
     * @param configuration The configuration file.
     * @since 1.4-SNAPSHOT
     */
    void reloadConfiguration(final @NotNull T configuration);
}
