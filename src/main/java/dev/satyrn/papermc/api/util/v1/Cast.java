package dev.satyrn.papermc.api.util.v1;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

/**
 * Utility for safe-casting between objects.
 *
 * @author Isabel Maskrey
 * @since 1.6.0
 */
@SuppressWarnings("unused")
public final class Cast {

    // Do not instantiate the Cast class.
    private Cast() { }

    /**
     * Safe-casts an object to another type.
     *
     * @param asClass The class to cast to.
     * @param object  The object to cast.
     * @param <T>     The type that the object will be cast to.
     *
     * @return An optional of T where the result is only present if the cast succeeded.
     *
     * @since 1.6.0
     */
    public static <T> @NotNull Optional<T> as(@NotNull Class<T> asClass, @Nullable Object object) {
        if (object != null && asClass.isAssignableFrom(object.getClass())) {
            try {
                return Optional.of(asClass.cast(object));
            } catch (ClassCastException ex) {
                if (asClass == String.class) {
                    T string = asClass.cast(object.toString());
                    return string == null ? Optional.empty() : Optional.of(string);
                }
            }
        }
        return Optional.empty();
    }

    /**
     * Performs an unsafe cast from an object to a generic type.
     *
     * @param object The object to cast.
     * @param <T> The type that the object will be cast to.
     *
     * @return An optional of T where the result is only present if the cast succeeds.
     *
     * @since 1.9.1
     */
    @SuppressWarnings("unchecked")
    public static <T> @NotNull Optional<T> as(@Nullable Object object) {
        T cast;
        try {
            // We're just blindly asserting type here
            cast = (T) object;
        } catch (ClassCastException ex) {
            try {
                // Maybe T is a string?
                cast = (T) object.toString();
            } catch (ClassCastException ex2) {
                return Optional.empty();
            }
        }
        return cast == null ? Optional.empty() : Optional.of(cast);
    }
}
