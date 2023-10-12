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
 * @since 1.1.0
 */
@SuppressWarnings("unused")
public abstract class CommandHandler implements CommandExecutor, TabCompleter {
    // The command handler's parent plugin.
    private final transient @NotNull Plugin plugin;
    // The command usage string.
    private transient @Nullable String usage;

    /**
     * Initializes a new instance of a command handler.
     *
     * @param plugin The parent plugin.
     *
     * @since 1.1.0
     */
    protected CommandHandler(@NotNull Plugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Sets up a plugin command.
     *
     * @param command The plugin command.
     *
     * @return The command handler instance.
     *
     * @since 1.1.0
     */
    @Contract(value = "_ -> this", mutates = "this")
    public final @NotNull CommandHandler setupCommand(final @NotNull PluginCommand command) {
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
     *
     * @return The command handler instance.
     *
     * @since 1.1.0
     */
    @Contract(value = "_, _ -> this", mutates = "this")
    public final @NotNull CommandHandler setupCommand(final @NotNull Plugin plugin, final @NotNull String commandName) {
        final @NotNull PluginCommand command = Objects.requireNonNull(plugin.getServer().getPluginCommand(commandName));
        return this.setupCommand(command);
    }

    /**
     * Gets the command usage hint.
     *
     * @param sender  The command's sender.
     * @param command The command to default to if the usage is not set on the handler.
     *
     * @return The command usage hint.
     *
     * @since 1.1.0
     */
    @NotNull
    protected String getUsage(@NotNull final CommandSender sender, final @NotNull Command command) {
        return this.usage == null || this.usage.isEmpty() ? command.getUsage() : this.usage;
    }

    /**
     * Gets the plugin instance.
     *
     * @return The plugin instance.
     *
     * @since 1.2.0
     */
    @NotNull
    public Plugin getPlugin() {
        return plugin;
    }

    /**
     * Sets the command usage hint.
     *
     * @param usage The new usage hint.
     *
     * @since 1.1.0
     */
    @SuppressWarnings("UnusedReturnValue")
    @NotNull
    @Contract(value = "_ -> this", mutates = "this")
    public final CommandHandler setUsage(@Nullable final String usage) {
        this.usage = usage;
        return this;
    }
}
