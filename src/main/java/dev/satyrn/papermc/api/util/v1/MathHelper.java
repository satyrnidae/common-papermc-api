package dev.satyrn.papermc.api.util.v1;

/**
 * Provides common math functions.
 *
 * @author Isabel Maskrey
 * @since 1.5-SNAPSHOT
 */
public final class MathHelper {
    private MathHelper() { }

    /**
     * Clamps a double between a min and max value.
     * @param d The double value.
     * @param min The minimum allowed value.
     * @param max The maximum allowed value.
     * @return The clamped result.
     * @since 1.5-SNAPSHOT
     */
    public static double clampd(double d, double min, double max) {
        return Math.max(min, Math.min(d, max));
    }

    /**
     * Clamps a float between a min and max value.
     * @param f The float value.
     * @param min The minimum allowed value.
     * @param max The maximum allowed value.
     * @return The clamped result.
     * @since 1.5-SNAPSHOT
     */
    public static float clampf(float f, float min, float max) {
        return Math.max(min, Math.min(f, max));
    }

    /**
     * Clamps an integer between a min and max value.
     * @param i The integer value.
     * @param min The minimum allowed value.
     * @param max The maximum allowed value.
     * @return The clamped result.
     * @since 1.5-SNAPSHOT
     */
    public static int clamp(int i, int min, int max) {
        return Math.max(min, Math.min(i, max));
    }

    /**
     * Gets the log of a value with the specified base.
     * @param value The value.
     * @param base The base.
     * @return The resultant value.
     * @since 1.5-SNAPSHOT
     */
    public static double logb(double value, double base) {
        return Math.log(value) / Math.log(base);
    }
}
