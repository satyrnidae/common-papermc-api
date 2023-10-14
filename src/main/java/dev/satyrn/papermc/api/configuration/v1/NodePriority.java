package dev.satyrn.papermc.api.configuration.v1;

/**
 * Used to determine whether a node should be superceded or supercede existing items in the node child list.
 *
 * @author Isabel Maskrey
 * @since 1.10.0
 */
public enum NodePriority {
    /**
     * The lowest priority value, which is always overwritten, even by new {@code LOWEST} priority nodes.
     */
    LOWEST(-2),
    /**
     * The low priority value, which is only overwritten by {@code NORMAL} to {@code HIGHEST} priority nodes.
     */
    LOW(-1),
    /**
     * The normal priority value, which is only overwritten by {@code HIGH} or {@code HIGHEST} priority nodes.
     */
    NORMAL(0),
    /**
     * The high priority value, which is only overwritten by {@code HIGHEST} priority nodes.
     */
    HIGH(1),
    /**
     * The highest priority value, which is never overwritten.
     */
    HIGHEST(2);

    private final int value;

    NodePriority(int value) {
        this.value = value;
    }

    /**
     * Gets the priority value of this enum entry.
     *
     * @return The priority of the enum entry.
     *
     * @since 1.10.0
     */
    public int getValue() {
        return value;
    }
}
