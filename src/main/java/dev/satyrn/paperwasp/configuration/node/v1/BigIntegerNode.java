package dev.satyrn.paperwasp.configuration.node.v1;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigInteger;

public final class BigIntegerNode extends ClampedValueNode<BigInteger> {
    public BigIntegerNode(final @NotNull String name,
                          final @NotNull Node<?> parent,
                          final @Nullable BigInteger minValue,
                          final @Nullable BigInteger maxValue) {
        super(name, parent, minValue, maxValue);
        this.setDefaultValue(BigInteger.ZERO);
    }

    @Override
    protected @NotNull BigInteger getConfiguredValue(final @NotNull String path,
                                                     final @Nullable BigInteger defaultValue) {
        final String stringValue = this.getConfiguration().getString(path, defaultValue == null ? BigInteger.ZERO.toString() : defaultValue.toString());
        return new BigInteger(stringValue);
    }

    @Override
    protected void setConfiguredValue(final @NotNull String path,
                                      final @Nullable BigInteger value) {
        final String stringValue = value == null ? null : value.toString();
        this.getConfiguration().set(path, stringValue);
    }
}
