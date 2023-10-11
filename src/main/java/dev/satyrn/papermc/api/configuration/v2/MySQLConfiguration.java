package dev.satyrn.papermc.api.configuration.v2;

import dev.satyrn.papermc.api.configuration.v1.*;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a container of nodes which dictate the function of and options for the MySQL server backend.
 *
 * @author Isabel Maskrey
 * @since 1.9.0
 */
@SuppressWarnings("unused")
public class MySQLConfiguration extends ContainerNode {
    /**
     * Whether the MySQL server backend should be enabled.
     *
     * @since 1.6.0
     */
    @SuppressWarnings("unused")
    public final transient BooleanNode enabled = new BooleanNode(this, "enabled");

    /**
     * The MySQL server hostname.
     *
     * @since 1.6.0
     */
    public final transient StringNode hostname = new StringNode(this, "hostname") {
        @Override
        public @NotNull String defaultValue() {
            return "localhost";
        }
    };

    /**
     * The MySQL server port.
     *
     * @since 1.6.0
     */
    public final transient IntegerNode port = new IntegerNode(this, "port") {
        @Override
        public @NotNull Integer defaultValue() {
            return 3306;
        }
    };

    /**
     * The name of the database to use.
     *
     * @since 1.6.0
     */
    public final transient StringNode database = new StringNode(this, "database") {
        @Override
        public @NotNull String defaultValue() {
            return "spigot";
        }
    };

    /**
     * The MySQL user ID.
     *
     * @since 1.6.0
     */
    public final transient StringNode userID = new StringNode(this, "userID") {
        @Override
        public @NotNull String defaultValue() {
            return "root";
        }
    };

    /**
     * The MySQL user password.
     *
     * @since 1.6.0
     */
    public final transient StringNode password = new StringNode(this, "password") {
        @Override
        public @NotNull String defaultValue() {
            return "password";
        }
    };

    /**
     * Options for the MySQL connection.
     *
     * @since 1.6.0
     **/
    public final transient MapListNode flags = new MapListNode(this, "flags");

    /**
     * Optional prefix for any created table's names.
      *
     * @since 1.6.0
     */
    @SuppressWarnings("unused")
    public final transient StringNode tablePrefix = new StringNode(this, "tablePrefix");

    /**
     * Whether to automatically add an underscore between the prefix and the table name.
     * Defaults to true.
     *
     * @since 1.6.0
     */
    @SuppressWarnings("unused")
    public final transient BooleanNode appendUnderscoreToPrefix = new BooleanNode(this, "appendUnderscoreToPrefix") {
        @Override
        public @NotNull Boolean defaultValue() {
            return true;
        }
    };

    /**
     * Creates a new MySQL configuration container.
     *
     * @param parent The parent container.
     * @since 1.6.0
     */
    public MySQLConfiguration(ConfigurationNode<?> parent) {
        super(parent, "mysql");
    }
}
