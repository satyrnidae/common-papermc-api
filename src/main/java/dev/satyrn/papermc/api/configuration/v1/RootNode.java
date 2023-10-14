package dev.satyrn.papermc.api.configuration.v1;

import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Level;

/**
 * Represents a value-less node which contains other nodes.
 *
 * @author Isabel Maskrey
 * @since 1.9.0
 */
@SuppressWarnings("unused")
public abstract class RootNode extends ContainerNode {
    /**
     * The version of the config file.
     *
     * @since 1.9.0
     */
    private final IntegerNode _version = new IntegerNode(this, "_version", 0, Integer.MAX_VALUE);

    /**
     * Initializes the configuration container as a root container.
     *
     * @param plugin The plugin instance.
     *
     * @since 1.9.0
     */
    protected RootNode(@NotNull Plugin plugin) {
        super(plugin);
    }

    /**
     * Gets the config version.
     *
     * @return The version of the configuration file.
     *
     * @since 1.9.0
     */
    public int getVersion() {
        return this._version.value();
    }

    /**
     * Sets the config version.
     *
     * @param newVersion The new version of the config file.
     *
     * @since 1.9.0
     */
    public void setVersion(int newVersion) {
        final int previousVersion = this.getVersion();

        this.getLogger().log(Level.FINE, "Config file updated to version " + newVersion + " from " + previousVersion);
        this._version.setValue(newVersion);
        this._version.setComments(
                "Internal value to update the config file to the latest version.",
                "Do not touch this!"
        );
    }

    /**
     * Writes the value of the node to the config file.
     *
     * @since 1.9.0
     */
    @Override
    public void save() {
        super.save();

        this.getPlugin().saveConfig();
    }

    /**
     * Upgrades the configuration to the latest version.
     *
     * @since 1.9.0
     */
    public abstract void upgrade();
}
