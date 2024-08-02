package dev.satyrn.paperwasp.command.v1;

import dev.satyrn.lunamoth.i18n.v1.I18n;
import org.bukkit.command.Command;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Creates a localized command handler which implements I18n.
 *
 * @author Isabel Maskrey
 * @since 3.0.0-paper-api.1.21-R0.1-SNAPSHOT
 */
public abstract class LocalizedCommandHandler extends AbstractCommandHandler {
    private final transient @NotNull I18n i18n;
    /**
     * Initializes a new localized command handler.
     *
     * @param plugin      The parent plugin.
     * @since             3.0.0-paper-api.1.21-R0.1-SNAPSHOT
     */
    protected LocalizedCommandHandler(final @NotNull Plugin plugin,
                                      final @NotNull I18n i18n) {
        super(plugin);
        this.i18n = i18n;
    }

    /**
     * Returns the usage message for the given command, translated using the I18n instance.
     *
     * @param command The command to get the usage message for.
     * @return The translated usage message.
     * @since 3.0.0-paper-api.1.21-R0.1-SNAPSHOT
     */
    @Override
    protected @NotNull String getUsage(@NotNull Command command) {
        final @NotNull String usageSlug = command.getName().toLowerCase() + ".usage";
        return this.translate(usageSlug, super.getUsage(command));
    }

    /**
     * Returns the usage message for a missing parameter in the given command, translated using the I18n instance.
     *
     * @param command  The command to get the parameter usage message for.
     * @param parameter The missing parameter.
     * @return The translated parameter usage message.
     * @since 3.0.0-paper-api.1.21-R0.1-SNAPSHOT
     */
    @Override
    protected @NotNull String getParameterUsage(@NotNull Command command, @NotNull String parameter) {
        final @NotNull String usageSlug = command.getName().toLowerCase() + "usage.missingParameter";
        return this.translate(usageSlug, super.getUsage(command));
    }

    /**
     * Translates the given slug using the I18n instance, providing a fallback message and optional format arguments.
     *
     * @param slug     The slug to translate.
     * @param fallback The fallback message if the translation is not found.
     * @param format   Optional format arguments to be used in the translation.
     * @return The translated message.
     * @since 3.0.0-paper-api.1.21-R0.1-SNAPSHOT
     */
    protected final @NotNull String translate(final @NotNull String slug,
                                              final @NotNull String fallback,
                                              final @Nullable Object... format) {
        return this.i18n.translate(this.getPlugin().getName().toLowerCase() + ".command." + slug, fallback, format);
    }
}
