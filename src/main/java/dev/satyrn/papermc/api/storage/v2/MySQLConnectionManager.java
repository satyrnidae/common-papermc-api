package dev.satyrn.papermc.api.storage.v2;

import dev.satyrn.papermc.api.configuration.v2.MySQLConfiguration;
import dev.satyrn.papermc.api.storage.v1.ConnectionManager;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

/**
 * Handles connections to a MySQL-like data source.
 *
 * @author Isabel Maskrey
 * @since 1.6.0
 */
@SuppressWarnings({"unused", "ClassCanBeRecord"})
public class MySQLConnectionManager implements ConnectionManager {
    private final transient @NotNull Plugin plugin;
    private final transient @NotNull MySQLConfiguration configuration;

    public MySQLConnectionManager(final @NotNull Plugin plugin, final @NotNull MySQLConfiguration configuration) {
        this.plugin = plugin;
        this.configuration = configuration;
    }

    /**
     * Opens a connection to the data source.
     *
     * @return The connection to the data source.
     * @since 1.6.0
     */
    @Override
    public @Nullable Connection connect() {
        final @NotNull StringBuilder connectionURLBuilder = new StringBuilder("jdbc:mysql://").append(this.configuration.hostname.value())
                .append(':')
                .append(this.configuration.port.value())
                .append('/')
                .append(this.configuration.database.value());

        final @NotNull List<Map<?, ?>> flagsList = this.configuration.flags.value();
        if (!flagsList.isEmpty()) {
            int i = 0;
            for (final @NotNull Map<?, ?> flags : flagsList) {
                for (final @NotNull Map.Entry<?, ?> entry : flags.entrySet()) {
                    connectionURLBuilder.append(i++ == 0 ? '?' : '&')
                            .append(entry.getKey().toString())
                            .append('=')
                            .append(entry.getValue().toString());
                }
            }
        }

        @Nullable Connection connection = null;
        try {
            final @NotNull String connectionURL = connectionURLBuilder.toString();
            final @Nullable String userID = this.configuration.userID.value();
            this.plugin.getLogger()
                    .log(Level.FINE, String.format("[Storage] Attempting connection to MySQL-like database at %s with %s", connectionURL, userID == null ? "anonymous user" : "user " + userID));
            connection = DriverManager.getConnection(connectionURL, userID, this.configuration.password.value());
        } catch (SQLException ex) {
            this.plugin.getLogger()
                    .log(Level.SEVERE, String.format("[Storage] Failed to connect to the database: %s", ex.getMessage()), ex);
        }

        return connection;
    }
}
