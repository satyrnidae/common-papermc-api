package dev.satyrn.papermc.api.storage.v1;

import org.jetbrains.annotations.Nullable;

import java.sql.Connection;

/**
 * Manages connections to a data source.
 *
 * @author Isabel Maskrey
 * @since 1.6.0
 * @deprecated Use {@link dev.satyrn.lunamoth.storage.v1.ConnectionSupplier} instead
 */
@Deprecated(since = "3.0.0-paper-api.1.21-R0.1-SNAPSHOT", forRemoval = true)
@SuppressWarnings("unused")
public interface ConnectionManager {
    /**
     * Opens a connection to the data source.
     *
     * @return The connection to the data source.
     *
     * @since 1.6.0
     */
    @Nullable Connection connect();
}
