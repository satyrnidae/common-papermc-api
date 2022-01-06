package dev.satyrn.papermc.api.commands.v1;

import org.bukkit.command.*;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * Represents a basic command handler.
 *
 * @author Isabel Maskrey
 * @since 1.1-SNAPSHOT
 */
@SuppressWarnings("unused")
public abstract class CommandHandler implements CommandExecutor, TabCompleter {
    // The command usage string.
    @Nullable
    private transient String usage;

    // The command handler's parent plugin.
    @NotNull
    private final transient Plugin plugin;

    /**
     * Initializes a new instance of a command handler.
     *
     * @param plugin The parent plugin.
     * @since 1.1-SNAPSHOT
     */
    protected CommandHandler(@NotNull Plugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Sets up a plugin command.
     *
     * @param command The plugin command.
     * @since 1.1-SNAPSHOT
     */
    @NotNull
    public final CommandHandler setupCommand(@NotNull final PluginCommand command) {
        command.setExecutor(this);
        command.setTabCompleter(this);
        this.setUsage(command.getUsage());
        return this;
    }

    /**
     * Sets up a plugin command by name.
     *
     * @param plugin      The plugin instance.
     * @param commandName The command name.
     * @return The command handler instance.
     * @since 1.1-SNAPSHOT
     */
    @NotNull
    public final CommandHandler setupCommand(@NotNull final Plugin plugin, @NotNull final String commandName) {
        @NotNull final PluginCommand command = Objects.requireNonNull(plugin.getServer().getPluginCommand(commandName));
        return this.setupCommand(command);
    }

    /**
     * Gets the command usage hint.
     *
     * @param command The command to default to if the usage is not set on the handler.
     * @return The command usage hint.
     * @since 1.1-SNAPSHOT
     */
    @NotNull
    protected String getUsage(@NotNull final CommandSender sender, @NotNull final Command command) {
        return this.usage == null || this.usage.isEmpty() ? command.getUsage() : this.usage;
    }

    /**
     * Gets the plugin instance.
     *
     * @return The plugin instance.
     * @since 1.2-SNAPSHOT
     */
    @NotNull
    public Plugin getPlugin() {
        return plugin;
    }

    /**
     * Sets the command usage hint.
     *
     * @param usage The new usage hint.
     * @since 1.1-SNAPSHOT
     */
    @SuppressWarnings("UnusedReturnValue")
    @NotNull
    @Contract(value = "_ -> this", mutates = "this")
    public final CommandHandler setUsage(@Nullable final String usage) {
        this.usage = usage;
        return this;
    }
}
