package dev.satyrn.paperwasp.configuration.node.primitive.v1;

import dev.satyrn.paperwasp.configuration.node.v1.ClampedValueNode;
import dev.satyrn.paperwasp.configuration.node.v1.Node;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class LongNode extends ClampedValueNode<Long> {
    public LongNode(final @NotNull String name,
                       final @NotNull Node<?> parent,
                       final @Nullable Long minValue,
                       final @Nullable Long maxValue) {
        super(name, parent, minValue, maxValue);
        this.setDefaultValue(0L);
    }

    @Override
    protected @NotNull Long getConfiguredValue(final @NotNull String path,
                                                final @Nullable Long defaultValue) {
        return this.getConfiguration().getLong(path, defaultValue == null ? 0L : defaultValue);
    }
}
