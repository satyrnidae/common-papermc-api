package dev.satyrn.papermc.api.util.v1;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.Supplier;

/**
 * Utility for safe-casting between objects.
 *
 * @author Isabel Maskrey
 * @since 1.6.0
 *
 * @deprecated since 3.0.0-paper-api.1.21-R0.1-SNAPSHOT. Use {@link dev.satyrn.lunamoth.util.v1.Cast} instead.
 */
@Deprecated(since="3.0.0-paper-api.1.21-R0.1-SNAPSHOT", forRemoval = true)
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
     *
     * @deprecated since 3.0.0-paper-api.1.21-R0.1-SNAPSHOT. Use {@link dev.satyrn.lunamoth.util.v1.Cast#to(Class, Object)} instead.
     */
    @Deprecated(since="3.0.0-paper-api.1.21-R0.1-SNAPSHOT", forRemoval = true)
    public static <T> @NotNull Optional<T> as(@NotNull Class<T> asClass, @Nullable Object object) {
        if (object != null && asClass.isAssignableFrom(object.getClass())) {
            try {
                return Optional.of(asClass.cast(object));
            } catch (ClassCastException ex) {
                if (asClass == String.class) {
                    T string = asClass.cast(object.toString());
                    return Optional.ofNullable(string);
                }
            }
        }
        return Optional.empty();
    }

    /**
     * Performs an unchecked cast from an object to a generic type.
     *
     * @param object The object to cast.
     * @param <T> The type that the object will be cast to.
     *
     * @return An optional of T where the result is only present if the cast succeeds.
     *
     * @since 1.9.1
     *
     * @deprecated since 3.0.0-paper-api.1.21-R0.1-SNAPSHOT. Use {@link dev.satyrn.lunamoth.util.v1.Cast#to(Object)} instead.
     */
    @Deprecated(since="3.0.0-paper-api.1.21-R0.1-SNAPSHOT", forRemoval = true)
    @SuppressWarnings("unchecked")
    public static <T> @NotNull Optional<T> as(@Nullable Object object) {
        T cast = null;
        try {
            // We're just blindly asserting type here
            cast = (T) object;
        } catch (ClassCastException ex) {
            try {
                // Maybe T is a string?
                cast = (T) object.toString();
            } catch (ClassCastException ignored) { }
        }
        return Optional.ofNullable(cast);
    }

    /**
     * Performs a safe cast returning a non-null value without using an {@link Optional}.
     *
     * @param toClass The class to cast to.
     * @param object The object to cast.
     * @param supplier The supplier to use if the cast fails.
     * @param <T> The type to cast to.
     *
     * @return The object as the target type, or the supplier result if the cast fails.
     *
     * @since 1.10.2
     *
     * @deprecated since 3.0.0-paper-api.1.21-R0.1-SNAPSHOT. Use {@link dev.satyrn.lunamoth.util.v1.Cast#orElseGet(Class, Object, Supplier)} instead.
     */
    @Deprecated(since="3.0.0-paper-api.1.21-R0.1-SNAPSHOT", forRemoval = true)
    public static <T> @NotNull T as(@NotNull Class<T> toClass, @Nullable Object object, @NotNull Supplier<@NotNull T> supplier) {
        return as(toClass, object).orElseGet(supplier);
    }

    /**
     * Performs an unchecked cast returning a non-null value without using an
     * {@link Optional}.
     *
     * @param object The object to cast.
     * @param supplier The object supplier to use if the cast fails
     * @param <T> The type that the object will be cast to.
     *
     * @return The object as the target type, or the result of the supplier if the cast fails.
     *
     * @since 1.10.2
     *
     * @deprecated since 3.0.0-paper-api.1.21-R0.1-SNAPSHOT. Use {@link dev.satyrn.lunamoth.util.v1.Cast#orElseGet(Class, Object, Supplier)} instead.
     */
    @Deprecated(since="3.0.0-paper-api.1.21-R0.1-SNAPSHOT", forRemoval = true)
    @SuppressWarnings("unchecked")
    public static <T> @NotNull T as(@Nullable Object object, @NotNull Supplier<@NotNull T> supplier) {
        return (T)as(object).orElseGet(supplier);
    }

    /**
     * Performs a safe cast returning a nullable value.
     *
     * @param toClass The class to cast to.
     * @param object The object to cast.
     * @param <T> The type to cast to.
     *
     * @return The object as the target type, or null if the object could not be cast to that type.
     *
     * @since 1.10.2
     *
     * @deprecated since 3.0.0-paper-api.1.21-R0.1-SNAPSHOT. Use {@link dev.satyrn.lunamoth.util.v1.Cast#orElse(Class, Object, Object)} instead.
     */
    @Deprecated(since="3.0.0-paper-api.1.21-R0.1-SNAPSHOT", forRemoval = true)
    public static <T> @Nullable T to(@NotNull Class<T> toClass, @Nullable Object object) {
        return as(toClass, object).orElse(null);
    }

    /**
     * Performs an unchecked cast returning a potentially null value.
     *
     * @param object The object to cast.
     * @param <T> The type that the object will be cast to.
     *
     * @return The object as the target type, or null if the cast fails.
     *
     * @since 1.10.2
     *
     * @deprecated since 3.0.0-paper-api.1.21-R0.1-SNAPSHOT. Use {@link dev.satyrn.lunamoth.util.v1.Cast#orElse(Class, Object, Object)} instead.
     */
    @Deprecated(since="3.0.0-paper-api.1.21-R0.1-SNAPSHOT", forRemoval = true)
    @SuppressWarnings("unchecked")
    public static <T> @Nullable T to(@Nullable Object object) {
        return (T)as(object).orElse(null);
    }
}
