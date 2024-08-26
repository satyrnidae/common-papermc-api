package dev.satyrn.paperwasp.configuration.node.v1;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;

public final class BigDecimalNode extends ClampedValueNode<BigDecimal> {
    public BigDecimalNode(final @NotNull String name,
                          final @NotNull Node<?> parent,
                          final @Nullable BigDecimal minValue,
                          final @Nullable BigDecimal maxValue) {
        super(name, parent, minValue, maxValue);
        this.setDefaultValue(BigDecimal.ZERO);
    }

    @Override
    protected @NotNull BigDecimal getConfiguredValue(final @NotNull String path,
                                                     final @Nullable BigDecimal defaultValue) {
        final String stringValue = this.getConfiguration().getString(path, defaultValue == null ? BigDecimal.ZERO.toPlainString() : defaultValue.toPlainString());
        return new BigDecimal(stringValue);
    }

    @Override
    protected void setConfiguredValue(final @NotNull String path,
                                      final @Nullable BigDecimal value) {
        final String stringValue = value == null ? null : value.toPlainString();
        this.getConfiguration().set(path, stringValue);
    }
}
