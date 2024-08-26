package dev.satyrn.paperwasp.configuration.node.primitive.v1;

import dev.satyrn.lunamoth.util.v1.MathUtil;
import dev.satyrn.paperwasp.configuration.node.v1.ClampedValueNode;
import dev.satyrn.paperwasp.configuration.node.v1.Node;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A node implementation for managing {@code Byte} values with clamping.
 * This node handles byte values with minimum and maximum bounds.
 *
 * @author Isabel Maskrey
 * @since 3.0.0-paper-api.1.21-R0.1-SNAPSHOT
 */
public final class ByteNode extends ClampedValueNode<Byte> {
    /**
     * Constructs a new {@code ByteNode} with the specified name, parent node,
     * minimum value, and maximum value. Initializes the node with a default
     * value of {@code 0x00}.
     *
     * @param name     the name of this node
     * @param parent   the parent node of this node
     * @param minValue the minimum value this node can have, or {@code null} if no minimum bound
     * @param maxValue the maximum value this node can have, or {@code null} if no maximum bound
     * @since 3.0.0-paper-api.1.21-R0.1-SNAPSHOT
     */
    public ByteNode(final @NotNull String name,
                    final @NotNull Node<?> parent,
                    final @Nullable Byte minValue,
                    final @Nullable Byte maxValue) {
        super(name, parent, minValue, maxValue);
        this.setDefaultValue((byte)0x00);
    }

    /**
     * Retrieves the configured byte value from the configuration object
     * using the specified path. The value is clamped to ensure it falls within
     * the valid byte range ({@code Byte.MIN_VALUE} to {@code Byte.MAX_VALUE}).
     * If the value is not found, the provided default value is used.
     *
     * @param path         the path in the configuration object to retrieve the value from
     * @param defaultValue the default value to return if the path does not exist
     * @return the configured byte value from the configuration, clamped within the valid range, or the default value if not found
     * @since 3.0.0-paper-api.1.21-R0.1-SNAPSHOT
     */
    @Override
    public @NotNull Byte getConfiguredValue(final @NotNull String path,
                                            final @Nullable Byte defaultValue) {
        final int configuredValue = MathUtil.clamp(this.getConfiguration().getInt(path, defaultValue == null ? 0 : defaultValue), Byte.MIN_VALUE, Byte.MAX_VALUE);
        return (byte)configuredValue;
    }
}
