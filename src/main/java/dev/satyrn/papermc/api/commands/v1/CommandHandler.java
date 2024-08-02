package dev.satyrn.papermc.api.commands.v1;

import org.bukkit.command.*;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * This class represented a generic handler for plugin commands.
 * <p>
 * However, it has been replaced with an improved implementation in
 * {@link dev.satyrn.paperwasp.command.v1.LocalizedCommandHandler}.
 *
 * @deprecated This class has been deprecated and will be removed in a future update.
 *             <p>
 *             Please use {@link dev.satyrn.paperwasp.command.v1.LocalizedCommandHandler} instead.
 * @author     Isabel Maskrey
 * @since      1.1.0
 * @see        org.bukkit.command.CommandExecutor
 * @see        org.bukkit.command.TabCompleter
 */
@Deprecated(since = "3.0.0-paper-api.1.21-R0.1-SNAPSHOT", forRemoval = true)
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
     * @deprecated   This constructor has been deprecated and will be removed in a future update along with its class.
     *               <p>
     *               Use {@link dev.satyrn.paperwasp.command.v1.LocalizedCommandHandler} and its associated constructor
     *               instead.
     * @since        1.1.0
     */
    @Deprecated(since = "3.0.0-paper-api.1.21-R0.1-SNAPSHOT", forRemoval = true)
    protected CommandHandler(@NotNull Plugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Sets up a plugin command.
     *
     * @param command The plugin command.
     * @return        The command handler instance.
     * @deprecated    This function has been deprecated alongside its class and will be removed in a future update.
     * @since         1.1.0
     */
    @Deprecated(since = "3.0.0-paper-api.1.21-R0.1-SNAPSHOT", forRemoval = true)
    @Contract(value = "_ -> this")
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
     * @return            The command handler instance.
     * @deprecated        This function has been deprecated alongside its class and will be removed in a future update.
     * @since             1.1.0
     */
    @Deprecated(since = "3.0.0-paper-api.1.21-R0.1-SNAPSHOT", forRemoval = true)
    @Contract(value = "_, _ -> this")
    public final @NotNull CommandHandler setupCommand(final @NotNull Plugin plugin, final @NotNull String commandName) {
        final @NotNull PluginCommand command = Objects.requireNonNull(plugin.getServer().getPluginCommand(commandName));
        return this.setupCommand(command);
    }

    /**
     * Gets the command usage hint.
     *
     * @param sender  The command's sender.
     * @param command The command to default to if the usage is not set on the handler.
     * @return        The command usage hint.
     * @deprecated    This function has been deprecated alongside its class and will be removed in a future update.
     * @since         1.1.0
     */
    @Deprecated(since = "3.0.0-paper-api.1.21-R0.1-SNAPSHOT", forRemoval = true)
    @NotNull
    protected String getUsage(@NotNull final CommandSender sender, final @NotNull Command command) {
        return this.usage == null || this.usage.isEmpty() ? command.getUsage() : this.usage;
    }

    /**
     * Gets the plugin instance.
     *
     * @return     The plugin instance.
     * @deprecated This function has been deprecated alongside its class and will be removed in a future update.
     * @since      1.2.0
     */
    @Deprecated(since = "3.0.0-paper-api.1.21-R0.1-SNAPSHOT", forRemoval = true)
    @NotNull
    public Plugin getPlugin() {
        return plugin;
    }

    /**
     * Sets the command usage hint.
     *
     * @param usage The new usage hint.
     * @return      Self.
     * @deprecated   This function has been deprecated alongside its class and will be removed in a future update.
     * @since       1.1.0
     */
    @Deprecated(since = "3.0.0-paper-api.1.21-R0.1-SNAPSHOT", forRemoval = true)
    @SuppressWarnings("UnusedReturnValue")
    @NotNull
    @Contract(value = "_ -> this", mutates = "this")
    public final CommandHandler setUsage(@Nullable final String usage) {
        this.usage = usage;
        return this;
    }
}
