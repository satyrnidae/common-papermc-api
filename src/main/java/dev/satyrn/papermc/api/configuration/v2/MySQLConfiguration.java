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
public class MySQLConfiguration extends BooleanNode {
    /**
     * The MySQL server hostname.
     *
     * @since 1.9.0
     */
    public final transient StringNode hostname = new StringNode(this, "hostname") {
        /**
         * Gets the default value of the node.
         *
         * @return A string value, {@code localhost}
         *
         * @since 1.9.0
         */
        @Override
        public @NotNull String defaultValue() {
            return "localhost";
        }
    };

    /**
     * The MySQL server port.
     *
     * @since 1.9.0
     */
    public final transient IntegerNode port = new IntegerNode(this, "port") {
        /**
         * Gets the default value of the node.
         *
         * @return An integer value, {@code 3306}
         *
         * @since 1.9.0
         */
        @Override
        public @NotNull Integer defaultValue() {
            return 3306;
        }
    };

    /**
     * The name of the database to use.
     *
     * @since 1.9.0
     */
    public final transient StringNode database = new StringNode(this, "database") {
        /**
         * Gets the default value of the node.
         *
         * @return A string value, {@code spigot}
         *
         * @since 1.9.0
         */
        @Override
        public @NotNull String defaultValue() {
            return "spigot";
        }
    };

    /**
     * The MySQL user ID.
     *
     * @since 1.9.0
     */
    public final transient StringNode userID = new StringNode(this, "userID") {
        /**
         * Gets the default value of the node.
         *
         * @return A string value, {@code root}
         *
         * @since 1.9.0
         */
        @Override
        public @NotNull String defaultValue() {
            return "root";
        }
    };

    /**
     * The MySQL user password.
     *
     * @since 1.9.0
     */
    public final transient StringNode password = new StringNode(this, "password") {
        /**
         * Gets the default value of the node.
         *
         * @return A string value, {@code password}
         *
         * @since 1.9.0
         */
        @Override
        public @NotNull String defaultValue() {
            return "password";
        }
    };

    /**
     * Options for the MySQL connection.
     *
     * @since 1.9.0
     **/
    public final transient MapListNode flags = new MapListNode(this, "flags");

    /**
     * Optional prefix for any created table's names.
      *
     * @since 1.9.0
     */
    @SuppressWarnings("unused")
    public final transient StringNode tablePrefix = new StringNode(this, "tablePrefix");

    /**
     * Whether to automatically add an underscore between the prefix and the table name.
     * <p>
     * Defaults to true.
     *
     * @since 1.9.0
     */
    @SuppressWarnings("unused")
    public final transient BooleanNode appendUnderscoreToPrefix = new BooleanNode(this, "appendUnderscoreToPrefix") {
        /**
         * Gets the default value of the node.
         *
         * @return A boolean value, {@code true}.
         *
         * @since 1.9.0
         */
        @Override
        public @NotNull Boolean defaultValue() {
            return true;
        }
    };

    /**
     * Creates a new MySQL configuration container.
     *
     * @param parent The parent container.
     *
     * @since 1.9.0
     */
    public MySQLConfiguration(ConfigurationNode<?> parent) {
        super(parent, "mysql");
    }
}
