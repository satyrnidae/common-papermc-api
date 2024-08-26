package dev.satyrn.paperwasp.configuration.node.v1;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class ClampedValueNode<T extends Number & Comparable<T>> extends Node<T> {
    private final @Nullable T minValue;
    private final @Nullable T maxValue;
    private T value;

    protected ClampedValueNode(final @NotNull String name,
                               final @NotNull Node<?> parent,
                               final @Nullable T minValue,
                               final @Nullable T maxValue) {
        super(name, parent);
        if (minValue != null && maxValue != null && minValue.compareTo(maxValue) > 0) {
            throw new IllegalArgumentException("minValue must be null or less than maxValue if maxValue is not also null");
        }
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    @Override
    public final @NotNull T get() {
        return this.value;
    }

    @Override
    public final void accept(final @Nullable T value) {
        if (value == null) {
            this.value = this.getDefaultValue();
        } else if (this.minValue != null && this.minValue.compareTo(value) > 0) {
            this.value = this.minValue;
        } else if (this.maxValue != null && this.maxValue.compareTo(value) < 0) {
            this.value = this.maxValue;
        } else {
            this.value = value;
        }
    }
}
