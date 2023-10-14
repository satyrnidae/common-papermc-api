package dev.satyrn.papermc.api.util.v2;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

/**
 * Utility for safe-casting between objects.
 *
 * @author Isabel Maskrey
 * @since 1.6.0
 *
 * @deprecated Since 1.9.0 versioning refactor. To be removed in a future version. Use {@link dev.satyrn.papermc.api.util.v1.Cast} instead.
 */
@Deprecated(since = "1.9.0", forRemoval = true)
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
     * @return An optional of T where the result is only present if the cast succeeded.
     */
    @SuppressWarnings("unused")
    public static <T> @NotNull Optional<T> as(@NotNull Class<T> asClass, @Nullable Object object) {
        if (object != null && asClass.isAssignableFrom(object.getClass())) {
            try {
                return Optional.of(asClass.cast(object));
            } catch (ClassCastException ex) {
                // I don't think we can actually ever get here, but we'll discard this anyways
            }
        }
        return Optional.empty();
    }
}
