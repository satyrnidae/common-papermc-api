package dev.satyrn.paperwasp.configuration.node.primitive.v1;

import dev.satyrn.paperwasp.configuration.node.v1.ClampedValueNode;
import dev.satyrn.paperwasp.configuration.node.v1.Node;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class DoubleNode extends ClampedValueNode<Double> {

    public DoubleNode(final @NotNull String name,
                      final @NotNull Node<?> parent,
                      final @Nullable Double minValue,
                      final @Nullable Double maxValue) {
        super(name, parent, minValue, maxValue);
        this.setDefaultValue(0.0D);
    }

    @Override
    protected @NotNull Double getConfiguredValue(@NotNull String path, @Nullable Double defaultValue) {
        return this.getConfiguration().getDouble(path, defaultValue == null ? 0.0D : defaultValue);
    }
}
