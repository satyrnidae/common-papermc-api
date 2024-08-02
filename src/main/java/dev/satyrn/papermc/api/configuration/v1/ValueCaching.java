package dev.satyrn.papermc.api.configuration.v1;

public interface ValueCaching<T> {

    /**
     * Gets the cached value of the node.
     *
     * @return The current value of the node.
     *
     * @since 2.0.0
     */
    T getCachedValue();

    /**
     * Sets the current value of the node.
     *
     * @param cachedValue The current value of the node.
     *
     * @since 2.0.0
     */
    void setCachedValue(T cachedValue);
}
