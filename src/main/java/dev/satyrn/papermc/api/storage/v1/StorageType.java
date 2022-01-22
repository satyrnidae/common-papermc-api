package dev.satyrn.papermc.api.storage.v1;

/**
 * Indexes multiple types of storage.
 *
 * @author Isabel Maskrey
 * @since 1.6.0
 */
public enum StorageType {
    /**
     * Flat-file storage, such as YAML or text files.
     * Generally uses YAML configuration file structure.
     *
     * @since 1.6.0
     */
    FLAT_FILE,
    /**
     * Storage which is handled via a MySQL-like database.
     *
     * @since 1.6.0
     */
    MYSQL
}
