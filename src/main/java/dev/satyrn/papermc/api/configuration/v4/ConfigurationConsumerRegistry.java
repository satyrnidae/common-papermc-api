package dev.satyrn.papermc.api.configuration.v4;

import dev.satyrn.papermc.api.configuration.v1.ConfigurationContainer;

/**
 * Models a registry for classes which listen to a configuration file for changes.
 *
 * @param <T> The configuration container type.
 *
 * @author Isabel Maskrey
 * @deprecated Since 1.6.1. To be removed due to 1.9.0 versioning refactor. Use {@link dev.satyrn.papermc.api.configuration.v1.ConfigurationConsumerRegistry} instead.
 */
@Deprecated(since = "1.6.1", forRemoval = true)
@SuppressWarnings("unused")
public abstract class ConfigurationConsumerRegistry<T extends ConfigurationContainer> extends dev.satyrn.papermc.api.configuration.v1.ConfigurationConsumerRegistry<T> { }
