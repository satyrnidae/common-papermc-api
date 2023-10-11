package dev.satyrn.papermc.api.configuration.v1;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Models a registry for classes which listen to a configuration file for changes.
 *
 * @param <T> The configuration container type.
 * @since 1.9.0
 * @deprecated Since 1.6.1. Use configuration node values directly.
 */
@Deprecated(since = "1.6.1")
@SuppressWarnings("unused")
public abstract class ConfigurationConsumerRegistry<T extends ConfigurationContainer> {
    private final @NotNull List<ConfigurationConsumer<T>> consumers = new ArrayList<>();

    /**
     * Registers a configuration consumer to the instance.
     *
     * @param instance The configuration consumer.
     * @param <K>      The consumer type.
     *
     * @since 1.4-SNAPSHOT
     */
    public final <K extends ConfigurationConsumer<T>> void register(final @NotNull K instance) {
        if (!this.consumers.contains(instance)) {
            this.consumers.add(instance);
        }
    }

    /**
     * Reloads the configuration for each consumer.
     *
     * @param configuration The configuration instance.
     *
     * @since 1.4-SNAPSHOT
     */
    public final void reload(final @NotNull T configuration) {
        for (final ConfigurationConsumer<T> instance : this.consumers) {
            instance.reloadConfiguration(configuration);
        }
    }
}
