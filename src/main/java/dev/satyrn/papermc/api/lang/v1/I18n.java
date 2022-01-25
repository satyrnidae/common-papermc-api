package dev.satyrn.papermc.api.lang.v1;

import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.regex.Pattern;

/**
 * Provides internationalization support for a plugin.
 *
 * @author Isabel Maskrey
 * @since 1.1-SNAPSHOT
 */
@SuppressWarnings("unused")
public class I18n {
    /**
     * The default locale to use for translation.
     */
    public static final @NotNull Locale DEFAULT_LOCALE = Locale.US;
    // Double apostrophe pattern
    private static final @NotNull Pattern DOUBLE_APOS = Pattern.compile("''");
    // The default resource bundle
    private final transient @NotNull ResourceBundle defaultBundle;
    // The plugin.
    private final transient @NotNull Plugin plugin;
    // Cache for message format instances.
    private final transient @NotNull HashMap<String, MessageFormat> messageFormatCache = new HashMap<>();
    // The current locale's resource bundle.
    private transient @Nullable ResourceBundle localeBundle;
    // The current locale to use for translation.
    private transient @NotNull Locale currentLocale = DEFAULT_LOCALE;
    // The base name of the internationalization instance.
    private final transient @NotNull String baseName;
    // The instance.
    private static @Nullable I18n instance;

    /**
     * Initializes a new I18n instance.
     *
     * @param plugin        The plugin instance.
     * @param defaultBundle The default resource bundle.
     * @since 1.1-SNAPSHOT
     */
    protected I18n(final @NotNull Plugin plugin, final @NotNull ResourceBundle defaultBundle) {
        this.plugin = plugin;
        this.defaultBundle = defaultBundle;
        this.baseName = defaultBundle.getBaseBundleName();
    }

    /**
     * Initializes a new I18n instance.
     *
     * @param plugin The plugin instance.
     * @param baseName The default resource bundle.
     */
    public I18n(final @NotNull Plugin plugin, final @NotNull String baseName) {
        this.plugin = plugin;
        this.baseName = baseName;
        this.defaultBundle = ResourceBundle.getBundle(baseName, DEFAULT_LOCALE, new Utf8LangFileControl());
    }

    /**
     * Gets the current I18n instance.
     * @return The current I18n instance.
     */
    public static @Nullable I18n getInstance() {
        return instance;
    }

    /**
     * Translates a resource key.
     *
     * @param key    The resource key.
     * @param format The formatting for the message.
     * @return The translated key.
     * @since 0.0.0-SNAPSHOT
     */
    public static String tr(final @NotNull String key, final @NotNull Object... format) {
        if (instance == null) {
            return key;
        }
        return instance.translate(key, format);
    }

    /**
     * Gets the current locale.
     *
     * @return The current locale.
     * @since 1.2-SNAPSHOT
     */
    public @NotNull Locale getCurrentLocale() {
        return currentLocale;
    }

    /**
     * Gets the plugin instance.
     *
     * @return The plugin instance.
     */
    public @NotNull Plugin getPlugin() {
        return plugin;
    }

    /**
     * Translates a string with the given formatting parts.
     *
     * @param key    The key to translate.
     * @param format The message format parts
     * @return The translated key.
     * @since 1.1-SNAPSHOT
     */
    protected final @NotNull String translate(final @NotNull String key, final @NotNull Object... format) {
        if (format.length == 0) {
            return DOUBLE_APOS.matcher(this.translate(key)).replaceAll("'");
        } else {
            return this.format(key, format);
        }
    }

    /**
     * Sets the current locale of the internationalization handler.
     *
     * @param locale The locale string.
     * @since 1.1-SNAPSHOT
     */
    public final void setLocale(final @Nullable String locale) {
        if (locale != null && !locale.isEmpty()) {
            final String[] parts = locale.split("_");
            if (parts.length == 1) {
                this.currentLocale = new Locale(parts[0]);
            } else if (parts.length >= 2) {
                this.currentLocale = new Locale(parts[0], parts[parts.length - 1]);
            }
        }
        this.localeBundle = this.getResourceBundleForLocale(this.currentLocale);
    }

    /**
     * Gets a resource bundle for the current locale.
     *
     * @param locale The current locale.
     * @return The new resource bundle.
     * @since 0.0.0-SNAPSHOT
     */
    protected @NotNull ResourceBundle getResourceBundleForLocale(final @NotNull Locale locale) {
        return ResourceBundle.getBundle(this.baseName, locale, new Utf8LangFileControl());
    }
    /**
     * Enables the internationalization handler.
     *
     * @since 1.1-SNAPSHOT
     */
    public void enable() {
        instance = this;
    }

    /**
     * Disables the internationalization handler.
     *
     * @since 1.1-SNAPSHOT
     */
    public void disable() {
        instance = null;
    }

    /**
     * Translates a given resource key using the current locale bundle.
     *
     * @param key The resource key
     * @return The translated value from the current or default locale, or the key itself if no such key exists.
     * @since 1.1-SNAPSHOT
     */
    private @NotNull String translate(@NotNull final String key) {
        if (this.localeBundle != null) {
            try {
                return this.localeBundle.getString(key);
            } catch (MissingResourceException ex) {
                this.plugin.getLogger().log(Level.WARNING, String.format("Missing translation key \"%s\" in resource file \"%s.lang\"; falling back to default.", ex.getKey(), localeBundle.getLocale()), ex);
            }
        }
        try {
            return this.defaultBundle.getString(key);
        } catch (MissingResourceException ex) {
            this.plugin.getLogger().log(Level.SEVERE, String.format("Missing DEFAULT translation key \"%s\" in resource file \"%s.lang\"!", ex.getKey(), defaultBundle.getLocale()), ex);
            return key;
        }
    }

    /**
     * Formats a resource value using the specified formatting parts.
     *
     * @param key    The key of the resource to format.
     * @param format The format parts.
     * @return The translated and formatted key from the resource file, or the key itself if no such key exists.
     * @since 1.1-SNAPSHOT
     */
    private @NotNull String format(final @NotNull String key, final @NotNull Object... format) {
        @NotNull String translatedKey = this.translate(key);
        @Nullable MessageFormat messageFormat = this.messageFormatCache.get(translatedKey);
        if (messageFormat == null) {
            try {
                messageFormat = new MessageFormat(translatedKey);
                this.messageFormatCache.put(translatedKey, messageFormat);
            } catch (IllegalArgumentException ex) {
                this.plugin.getLogger().log(Level.WARNING, String.format("Invalid translation key for \"%s\": %s", key, ex.getMessage()), ex);
                translatedKey = translatedKey.replaceAll("\\{(\\D*?)}", "\\[$1\\]");
                try {
                    messageFormat = new MessageFormat(translatedKey);
                    this.messageFormatCache.put(translatedKey, messageFormat);
                } catch (IllegalArgumentException ex1) {
                    this.plugin.getLogger().log(Level.SEVERE, String.format("Invalid translation key for \"%s\": %s", key, ex.getMessage()), ex);
                    return key;
                }
            }
        }
        return messageFormat.format(format);
    }

    /**
     * Gathers a UTF-8 *.lang file into a resource bundle.
     *
     * @author Isabel Maskrey
     * @since 1.1-SNAPSHOT
     */
    public static class Utf8LangFileControl extends ResourceBundle.Control {
        /**
         * Creates a new bundle from the *.lang file.
         *
         * @param baseName    The base name of the file.
         * @param locale      The locale to use.
         * @param format      The file format. Unused.
         * @param classLoader The java class loader instance.
         * @param reload      Whether the stream should be reloaded or the cached class loader stream should be used.
         * @return A new ResourceBundle from the language file.
         * @throws IOException Occurs when the bundle file cannot be located or read.
         * @since 1.1-SNAPSHOT
         */
        public @Nullable ResourceBundle newBundle(final @NotNull String baseName, final @NotNull Locale locale, final @Nullable String format, final @NotNull ClassLoader classLoader, final boolean reload) throws IOException {
            final @NotNull String resourceName = this.toResourceName(this.toBundleName(baseName, locale), "lang");
            @Nullable ResourceBundle bundle = null;
            @Nullable InputStream stream = null;
            // Reload the file from the URL if reload is specified, otherwise use the class loader's cached stream.
            if (reload) {
                final @Nullable URL url = classLoader.getResource(resourceName);
                if (url != null) {
                    final @Nullable URLConnection connection = url.openConnection();
                    if (connection != null) {
                        connection.setUseCaches(false);
                        stream = connection.getInputStream();
                    }
                }
            } else {
                stream = classLoader.getResourceAsStream(resourceName);
            }

            // If we successfully found the file, create a new bundle from the stream.
            if (stream != null) {
                try {
                    bundle = new PropertyResourceBundle(new InputStreamReader(stream, StandardCharsets.UTF_8));
                } finally {
                    stream.close();
                }
            }
            return bundle;
        }

        /**
         * Gets the bundle name of this resource.
         *
         * @param baseName The fallback resource name if the locale does not have a language code.
         * @param locale   The locale.
         * @return The locale's language and country code separated by an underscore, or, if the locale lacks a language
         * code, returns the base name.
         * @since 1.1-SNAPSHOT
         */
        public @NotNull String toBundleName(final @NotNull String baseName, final @NotNull Locale locale) {
            // Root locale returns base name.
            if (locale == Locale.ROOT) {
                return baseName;
            }
            final @Nullable String language = locale.getLanguage();
            // If language is not specified return the base name.
            if (language == null || language.isEmpty()) {
                return baseName;
            }

            @Nullable String country = locale.getCountry();
            if (country != null) {
                country = country.toLowerCase(Locale.ROOT);
            }

            final @NotNull StringBuilder sb = new StringBuilder(baseName).append('.').append(language);
            if (country != null && !country.isEmpty()) {
                sb.append('_').append(country);
            }

            return sb.toString();
        }
    }
}
