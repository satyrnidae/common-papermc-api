package dev.satyrn.papermc.api.configuration.v1;

import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

/**
 * Implementation of the root node class for testing
 */
public class RootNodeImpl extends RootNode {
    /**
     * Initializes the configuration container as a root container.
     *
     * @param plugin The plugin instance.
     *
     * @since 1.9.0
     */
    protected RootNodeImpl(@NotNull Plugin plugin) {
        super(plugin);
    }

    /**
     * Does nothing for this utility class.
     *
     * @since 1.10.0
     */
    @Override
    public void upgrade() {

    }
}
