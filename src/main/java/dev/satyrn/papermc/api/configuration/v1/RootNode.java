package dev.satyrn.papermc.api.configuration.v1;

import org.bukkit.configuration.Configuration;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

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
    public final IntegerNode _version = new IntegerNode(this, "_version", 0, Integer.MAX_VALUE);

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
     * Initializes the configuration container as a root container.
     *
     * @param plugin The plugin instance.
     * @param config The configuration instance.
     *               
     * @since 1.9.0
     */
    protected RootNode(@NotNull Plugin plugin, @NotNull Configuration config) {
        super(plugin, config);
    }
}
