package dev.satyrn.paperwasp.command.v1;

import org.bukkit.command.*;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Objects;

/**
 * Represents a basic Command Handler.
 *
 * @author Isabel Maskrey
 * @since 3.0.0-paper-api.1.21-R0.1-SNAPSHOT
 */
public abstract class AbstractCommandHandler implements CommandExecutor, TabCompleter {
    /**
     * The plugin instance.
     *
     * @since 3.0.0-paper-api.1.21-R0.1-SNAPSHOT
     */
    private final transient @NotNull Plugin plugin;

    /**
     * Initializes a new command handler.
     *
     * @param plugin The parent plugin.
     * @since        3.0.0-paper-api.1.21-R0.1-SNAPSHOT
     */
    @SuppressWarnings({"unused"})
    protected AbstractCommandHandler(final @NotNull Plugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Sets up this handler as the {@link CommandExecutor} and {@link TabCompleter} for a plugin command.
     *
     * @param command The command to handle and provide tab completion.
     * @return        This command handler.
     * @since         3.0.0-paper-api.1.21-R0.1-SNAPSHOT
     */
    @Contract("_ -> this")
    public final @NotNull AbstractCommandHandler register(final @NotNull PluginCommand command) {
        command.setExecutor(this);
        command.setTabCompleter(this);
        command.setUsage(this.getUsage(command));
        return this;
    }

    /**
     * Sets up this handler as the {@link CommandExecutor} and {@link TabCompleter} for a plugin command by the command
     * name.
     *
     * @param commandName The name of the command.
     * @return            This command handler.
     * @since             3.0.0-paper-api.1.21-R0.1-SNAPSHOT
     */
    @Contract("_ -> this")
    public final @NotNull AbstractCommandHandler register(final @NotNull String commandName) {
        final @NotNull PluginCommand pluginCommand = Objects.requireNonNull(this.plugin.getServer().getPluginCommand(commandName));
        return this.register(pluginCommand);
    }

    /**
     * Gets the command handler's usage hint.
     *
     * @param command The command to get usage from if it is not set on the command handler.
     * @return        The command usage hint.
     * @since         3.0.0-paper-api.1.21-R0.1-SNAPSHOT
     */
    protected @NotNull String getUsage(final @NotNull Command command) {
        return command.getUsage();
    }

    /**
     * Gets the usage hint for a required parameter
     * @param command The command.
     * @param parameter The parameter name.
     * @return The usage string.
     * @apiNote The default implementation only returns the string returned by {@link #getUsage(Command)}.
     * @implSpec It is advised to override this method.
     */
    protected @NotNull String getParameterUsage(final @NotNull Command command,
                                                final @NotNull String parameter) {
        return getUsage(command);
    }

    /**
     * Gets this command handler's parent plugin.
     *
     * @return The command handler's parent plugin.
     * @since  3.0.0-paper-api.1.21-R0.1-SNAPSHOT
     */
    public final @NotNull Plugin getPlugin() {
        return this.plugin;
    }

    //TODO: I want to redo this to support command args by key or position, like sh or powershell.
    /**
     * Validates the command's configured arguments.
     * @param sender The sender of the command.
     * @param command The command.
     * @param maximumArgsAllowed The maximum number of arguments that this command can take.
     * @param requiredArguments The required arguments on the command.
     * @param args The arguments passed to this command.
     * @return {@code true} if the arguments on the command were valid. Otherwise, {@code false}
     */
    public final boolean validateArgs(final @NotNull CommandSender sender,
                                      final @NotNull Command command,
                                      final int maximumArgsAllowed,
                                      final @Nullable Map<@NotNull Integer, @NotNull String> requiredArguments,
                                      final @NotNull String[] args) {
        if (args.length > maximumArgsAllowed) {
            sender.sendMessage(this.getUsage(command));
            return false;
        }

        if (requiredArguments != null) {
            for (int key : requiredArguments.keySet()) {
                if (args.length < key) {
                    sender.sendMessage(this.getParameterUsage(command, requiredArguments.get(key)));
                    return false;
                }
            }
        }

        return true;
    }
}
