package dev.satyrn.papermc.api.configuration.v4;

import dev.satyrn.papermc.api.configuration.v1.ConfigurationContainer;

/**
 * Represents a class which requires access to a Configuration Container and should be able to refresh its state when
 * the configuration is reloaded from disk.
 *
 * @param <T> The configuration class
 * @author Isabel Maskrey
 * @since 1.4-SNAPSHOT
 * @deprecated Since 1.6.1. To be removed in a future version due to 1.9.0 versioning refactor. Use {@link dev.satyrn.papermc.api.configuration.v1.ConfigurationConsumer} instead.
 */
@Deprecated(since = "1.6.1", forRemoval = true)
@SuppressWarnings("unused")
public interface ConfigurationConsumer<T extends ConfigurationContainer> extends dev.satyrn.papermc.api.configuration.v1.ConfigurationConsumer<T> { }
