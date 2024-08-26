package dev.satyrn.paperwasp.configuration.node.primitive.v1;

import dev.satyrn.lunamoth.util.v1.MathUtil;
import dev.satyrn.paperwasp.configuration.node.v1.ClampedValueNode;
import dev.satyrn.paperwasp.configuration.node.v1.Node;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class ShortNode extends ClampedValueNode<Short> {
    public ShortNode(final @NotNull String name,
                     final @NotNull Node<?> parent,
                     final @Nullable Short minValue,
                     final @Nullable Short maxValue) {
        super(name, parent, minValue, maxValue);
        this.setDefaultValue((short)0);
    }

    @Override
    public @NotNull Short getConfiguredValue(final @NotNull String path,
                                             final @Nullable Short defaultValue) {
        int configuredValue = MathUtil.clamp(this.getConfiguration().getInt(path, defaultValue == null ? 0 : defaultValue), Short.MIN_VALUE, Short.MAX_VALUE);
        return (short)configuredValue;
    }
}
