package dev.satyrn.paperwasp.configuration.node.primitive.v1;

import dev.satyrn.paperwasp.configuration.node.v1.ClampedValueNode;
import dev.satyrn.paperwasp.configuration.node.v1.Node;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class IntegerNode extends ClampedValueNode<Integer> {
    public IntegerNode(final @NotNull String name,
                       final @NotNull Node<?> parent,
                       final @Nullable Integer minValue,
                       final @Nullable Integer maxValue) {
        super(name, parent, minValue, maxValue);
        this.setDefaultValue(0);
    }

    @Override
    public @NotNull Integer getConfiguredValue(final @NotNull String path,
                                               final @Nullable Integer defaultValue) {
        return this.getConfiguration().getInt(path, defaultValue == null ? 0 : defaultValue);
    }
}
