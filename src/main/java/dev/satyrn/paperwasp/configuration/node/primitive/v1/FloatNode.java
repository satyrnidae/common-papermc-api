package dev.satyrn.paperwasp.configuration.node.primitive.v1;

import dev.satyrn.lunamoth.util.v1.MathUtil;
import dev.satyrn.paperwasp.configuration.node.v1.ClampedValueNode;
import dev.satyrn.paperwasp.configuration.node.v1.Node;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class FloatNode extends ClampedValueNode<Float> {
    public FloatNode(final @NotNull String name,
                     final @NotNull Node<?> parent,
                     final @Nullable Float minValue,
                     final @Nullable Float maxValue) {
        super(name, parent, minValue, maxValue);
        this.setDefaultValue(0.0F);
    }

    @Override
    protected @NotNull Float getConfiguredValue(@NotNull String path, @Nullable Float defaultValue) {
        final double configuredValue = MathUtil.clamp(this.getConfiguration().getDouble(path, defaultValue == null ? 0.0D : defaultValue), Float.MIN_VALUE, Float.MAX_VALUE);
        return (float)configuredValue;
    }
}
