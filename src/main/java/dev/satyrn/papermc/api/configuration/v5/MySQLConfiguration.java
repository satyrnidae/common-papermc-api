package dev.satyrn.papermc.api.configuration.v5;

import dev.satyrn.papermc.api.configuration.v1.ConfigurationContainer;

/**
 * Represents a container of nodes which dictate the function of and options for the MySQL server backend.
 *
 * @author Isabel Maskrey
 * @since 1.6.0
 * @deprecated since 1.9.0 versioning refactor. To be removed in a future version; use {@link dev.satyrn.papermc.api.configuration.v1.MySQLConfiguration} instead
 */
@Deprecated(since="1.9.0", forRemoval = true)
public final class MySQLConfiguration extends dev.satyrn.papermc.api.configuration.v1.MySQLConfiguration {
    /**
     * Creates a new MySQL configuration container.
     *
     * @param parent The parent container.
     *
     * @since 1.6.0
     */
    public MySQLConfiguration(ConfigurationContainer parent) {
        super(parent);
    }
}